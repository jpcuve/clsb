package legacyb3.data;

import java.sql.*;
import legacyb3.or2.*;
import legacyb3.core.*;

public class PartyFK extends DBField {
	private Parties m_pt;
	
	private static PartyFK PARTY = null;
	private static PartyFK AFFILIATE = null;
	private static PartyFK SM = null;
	private static PartyFK UM = null;
	
	public static PartyFK instance(Parties pt, Class cl){
		if(cl == Party.class){
			if(PARTY == null) PARTY = new PartyFK(pt, cl);
			return PARTY;
		}
		if(cl == Affiliate.class){
			if(AFFILIATE == null) AFFILIATE = new PartyFK(pt, cl);
			return AFFILIATE;
		}
		if(cl == SM.class){
			if(SM == null) SM = new PartyFK(pt, cl);
			return SM;
		}
		if(cl == UM.class){
			if(UM == null) UM = new PartyFK(pt, cl);
			return UM;
		}
		return PARTY;
	}
	
	private PartyFK(Parties pt, Class cl){
		super(cl);
		m_pt = pt;
	}
	
	public Object toUser(Object o){
		Party p = null;
		try{
			p = m_pt.get((String)o);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return p;
	}
	
	public Object toJDBC(Object o){
		return ((Party)o).getID();
	}
	
	public String toSQL(Object o){
		return "'" + ((Party)o).getID() + "'";
	}
}
