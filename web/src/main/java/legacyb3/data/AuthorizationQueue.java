package legacyb3.data;

import java.sql.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import legacyb3.core.*;
import legacyb3.util.*;
import legacyb3.or2.*;

public class AuthorizationQueue extends Inputs{ 
	
	public AuthorizationQueue(Connection con, String tableName, String dealsTableName, Currencies ct, Parties pt) throws SQLException {
		super(con, tableName, dealsTableName, ct, pt);
	}
	
	// miscellaneous
	
	public void assemble(Inputs iqi, TimeOfDay tod) throws SQLException{
		this.progress("Assembling authorization queue", 0);
		InputMatcher im = new InputMatcher();
		int psize = iqi.unmatchedSize();
		int pcount = 0;
		for(DBEnumeration e1 = iqi.unmatchedInputs(); e1.hasMoreElements();){
			Input i1 = (Input)e1.nextElement();
			pcount++;
			this.progress("Processing of input " + pcount + " of " + psize, (pcount * 100) / psize);
			if(i1.getTimeOfDay().isBefore(tod)){
				i1.setStatus(Status.EXPIRED );
				i1.save();
				i1.setTable(this);
				i1.setStatus(Status.UNMATCHED );
				Vector iv = im.submit(i1);
				if(iv == null){
					im.add(i1);
				}else{
					boolean match = false;
					Input mi = null;
					for(Enumeration e2 = iv.elements(); e2.hasMoreElements() && !match;){
						Input i2 = (Input)e2.nextElement();
						match = i1.match(i2);
						if(match){
							String matchRef = i1.makeReference("TR");
							i1.setStatus(Status.MATCHED );
							i1.setMatch(matchRef);
							i2.setTable(this);
							i2.setStatus(Status.MATCHED );
							i2.setMatch(matchRef);
							i2.save();
							mi = i2;
						}
					}
					if(match) im.remove(mi);
					else im.add(i1);
				}
				this.save((Input)i1.cloneDeep());
			}

		}
		this.progress("Assembled " + pcount + " inputs", 0);
	}
					
	public BinaryTree loadAuthorisationQueueInMemory() throws SQLException {
		/* it is assumed that	- the seq order in authorisation follows the ref order
								- the BNS and MNS are implemented
								- if no DSM are found for UM a default DSM is used
		*/
		int psize = this.matchedSize() / 2;
		int pcount = 0;
		// the Inputs are load according to their reference ... Seq order
		BinaryTree memoryQueue = new BinaryTree(new MatchingInputComparator());
		/* Changed to be faster */
		for(DBEnumeration e = orderedMatchedInputs(); e.hasMoreElements();){
			pcount++;
			this.progress("Authorizing input bundle " + pcount + " of " + psize, (pcount * 100) / psize );
			Input inp1 = (Input)e.nextElement();
			MatchingInput matchInp = new MatchingInput(inp1.getReference());
			matchInp.setOrig(inp1.getOriginator());
			matchInp.setDsmOrig(inp1.getDesignatedSM());
			matchInp.setCounterp(inp1.getCounterParty());
			/* here comes case of BNS MNS 
			if( input type = BNZ or MNS  ){
				// if BNS MNS : DSM counterparty DSM = SM itself or default DSM of the UM
			 inp = (Input)matchedInputs.nextElement();
			 matchInp.setDsmCounterp(inp.getDesignatedSM());
			}
			 if(pt.getPartyType().equals(PartyType.UM)){
				UM um = (UM)pt;
				matchInp.setDsmCounterp(um.getDefaultDsm());
				}
			else {matchInp.setDsmCounterp((SM)pt);}
			}*/
			Input inp2 = (Input)e.nextElement();
			matchInp.setDsmCounterp(inp2.getDesignatedSM());
			if((matchInp.getOrig().getPartyType().equals(PartyType.SM))&&(matchInp.getCounterp().getPartyType().equals(PartyType.SM))) {
				inp1.setStatus(Status.AUTHORIZED);
				inp1.save();
				inp2.setStatus(Status.AUTHORIZED);
				inp2.save();
			}
			else {
				matchInp.setMatchedInput(inp1);
				matchInp.setMatchedInput(inp2);
				memoryQueue.add(matchInp);
			}
		}
		this.progress("Authorized " + psize + " input bundles", 0);
		return memoryQueue;
	}
	
	public void processAutomaticAuthorisation(BinaryTree authorisationStack, boolean sdt) throws SQLException{
		boolean process = !(authorisationStack.isEmpty());
		UMPositionWithSM umPos1 = null;
		UMPositionWithSM umPos2 = null;
		UMPositionWithSM umSimul1 = null;
		UMPositionWithSM umSimul2 = null;
		while (process){
			int successCount = 0;
			for(Iterator e = authorisationStack.iterator(); e.hasNext();){
				MatchingInput matchInp = (MatchingInput)e.next();
				boolean success = false;
				if(!(matchInp.getStatus().equals(Status.AUTHORIZED ))){
					Input inp1 = matchInp.getMatchedInput(matchInp.getOrig().getID());
					Input inp2 = matchInp.getMatchedInput(matchInp.getCounterp().getID());
					
					if(matchInp.getOrig().getPartyType().equals(PartyType.UM)){
						if (inp1.isManualAuthoriseFlag()){
							success = true;
						}
						else {
							UM um1 = (UM)matchInp.getOrig();
							umPos1 = um1.getUMPos(matchInp.getDsmOrig());
							umSimul1 = (UMPositionWithSM)umPos1.cloneDeep();
							umSimul1.bookSellMvt(inp1, 1);
							umSimul1.bookSellMvt(inp2, -1);
							success = umSimul1.testUMLimits(sdt);
						}
					}
					else {success = true;}
					if(success){
						if(matchInp.getCounterp().getPartyType().equals(PartyType.UM)){
							if (inp2.isManualAuthoriseFlag()){
								success = true;
							}
							else {
								UM um2 = (UM)matchInp.getCounterp();
								umPos2 = um2.getUMPos(matchInp.getDsmCounterp());
								umSimul2 = (UMPositionWithSM)umPos2.cloneDeep();
								umSimul2.bookSellMvt(inp1, -1);
								umSimul2.bookSellMvt(inp2, 1);	
								success = umSimul2.testUMLimits(sdt);
							}
						}
						else {success = true;}
					}
				}	
				if(success){
					successCount+= 1 ;
					matchInp.setStatus(Status.AUTHORIZED);	
					if (umSimul1 != null) {
						umPos1.copyBalancesFrom(umSimul1);
						//umPos1.save(); //// added 24 November
					}
					if (umSimul2 != null){
						umPos2.copyBalancesFrom(umSimul2);
						//umPos2.save();// added 24 November
					}
					matchInp.SaveInputs();
				}
			}
			if((successCount==0))process = false;
		}
	}
	
	public void AuthoriseManually(Input inp)throws SQLException{
		//Not completed to do when I have time
		DBEnumeration e = this.matchedInputs(inp.getMatch());
		Input inpCP = (Input) e.nextElement();
		if (inpCP.getReference().equals(inp.getReference())) inpCP = (Input)e.nextElement();
		inp.setManualAuthoriseFlag();
		try {
			UM um = (UM)inp.getOriginator();
			SM sm = (SM)inp.getDesignatedSM();
			UMPositionWithSM umPos = um.getUMPos(sm);
			umPos.bookSellMvt(inp,1);
			umPos.bookSellMvt(inpCP, -1);
			umPos.save();
			inp.save();
		}
		catch (ClassCastException ClassEx) {
									 }
		
	}
	public boolean clearManualAuthorisationFlags() throws SQLException {
		Input i = new Input(null);
		i.clearManualAuthoriseFlag();
		DBPattern dbp = new DBPattern(this, i);
		dbp.addColumn(this.getColumnDefinition("Manual Authorise Flag"));
		return this.set(dbp);
	}
							
	// main
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Class.forName ("com.ms.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:test");
		Parties pt = new Parties(con);
		CurrencyGroups cgt = new CurrencyGroups(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, pt);
		Currencies ct = new Currencies(con, cgt, at);
		UMPositionsWithSM umPos = new UMPositionsWithSM(con,pt);
		umPos.load();
		BalanceTypes btt = new BalanceTypes(con);
		btt.load();
		UMBalances umBal = new UMBalances(con,umPos,pt,btt,ct);
		umBal.load();
		Inputs iqi = new Inputs(con, "Inputs", "InputDeals", ct, pt);
		AuthorizationQueue iqo = new AuthorizationQueue(con, "AuthorizationQueue", "AuthorizationQueueDeals", ct, pt);
		iqo.clear();
		System.out.println("Inputs Table contains :");
		for(DBEnumeration e = iqi.inputs(); e.hasMoreElements();){
			Input inp = (Input)e.nextElement();
			System.out.println(inp.toStringFull());
		}
		System.out.println("Authorisation Queue contains :");
		iqo.assemble(iqi, new TimeOfDay(10, 0));
		for(DBEnumeration e = iqo.matchedInputs(); e.hasMoreElements();){
			Input inp = (Input)e.nextElement();
			System.out.println(inp.toStringFull());
		}
		iqo.processAutomaticAuthorisation(iqo.loadAuthorisationQueueInMemory(), false);
		
	}

			
		
}
