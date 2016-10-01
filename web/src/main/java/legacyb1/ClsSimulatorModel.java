package legacyb1;

import java.util.*;

public class ClsSimulatorModel{
	
	private Messenger m;
	private AccountTypes att;
	private Accounts at;
	private CurrencyGroups cgt;
	private Currencies ct;
	private BalanceTypes btt;
	private Balances bt;
	private CLSBank clst;
	private Queue qi;
	private Queue qo;
	private Movements mt;
	private PayOuts pt;
	private PayInSchedules pist;
	
	// constructors
	
	public ClsSimulatorModel(
		Messenger m,
		CLSBank clst, 
		AccountTypes att, 
		Accounts at, 
		CurrencyGroups cgt, 
		Currencies ct, 
		BalanceTypes btt, 
		Balances bt, 
		Queue qi,
		Queue qo,
		Movements mt,
		PayOuts pt,
		PayInSchedules pist){
		this.m = m;
		this.clst = clst;
		this.att = att;
		this.at = at;
		this.cgt = cgt;
		this.ct = ct;
		this.btt = btt;
		this.bt = bt;
		this.qi = qi;
		this.qo = qo;
		this.mt = mt;
		this.pt = pt;
		this.pist = pist;
	}
	
	// accessors
	
	public Messenger getMessenger(){
		return this.m;
	}
	
	public Clsb getClsb(){
		return clst.get();
	}
	
	public AccountTypes getAccountTypes(){
		return this.att;
	}
	
	public Accounts getAccounts(){
		return this.at;
	}
	
	public CurrencyGroups getCurrencyGroups(){
		return this.cgt;
	}
	
	public Currencies getCurrencies(){
		return this.ct;
	}
	
	public BalanceTypes getBalanceTypes(){
		return this.btt;
	}
	
	public Balances getBalances(){
		return this.bt;
	}
	
	public Queue getTransactions(){
		return this.qi;
	}
	
	public Queue getSettlementQueue(){
		return this.qo;
	}
	
	public Movements getMovements(){
		return this.mt;
	}
	
	public PayOuts getPayOuts(){
		return this.pt;
	}
	
	public PayInSchedules getPayInSchedules(){
		return this.pist;
	}
	
	public void assembleSettlementQueue(){
		try{
			qi.assemble(qo);
			qo.notifyObservers();
			mt.notifyObservers();
			at.notifyObservers();
			bt.save();
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void clearMovements(){
		try{
			mt.clear();
			mt.notifyObservers();
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void clearPayOuts(){
		try{
			pt.clear();
			pt.notifyObservers();
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void clearSettlementQueue(){
		try{
			qo.clear();
			qo.notifyObservers();
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void balancesSOD(){
		try{
			BalanceType opn = btt.get("OPN");
			BalanceType std = btt.get("STD");
			BalanceType uns = btt.get("UNS");
			BalanceType pin = btt.get("PIN");
			BalanceType pou = btt.get("POU");
			bt.load();
			Balance b = null;
			for(Enumeration e1 = bt.balances(std); e1.hasMoreElements();){
				b = (Balance)e1.nextElement();
				b.setAmount(0);
			}
			for(Enumeration e1 = bt.balances(opn); e1.hasMoreElements();){
				b = (Balance)e1.nextElement();
				Account a = b.getAccount();
				Currency c = b.getCurrency();
				a.setAmount(std, c, b.getAmount());
			}
			for(Enumeration e1 = bt.balances(uns); e1.hasMoreElements();){
				b = (Balance)e1.nextElement();
				b.setAmount(0);
			}
			for(Enumeration e1 = bt.balances(pou); e1.hasMoreElements();){
				b = (Balance)e1.nextElement();
				b.setAmount(0);
			}
			bt.save();
			at.notifyObservers();
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void balancesEOD(){
		try{
			BalanceType opn = btt.get("OPN");
			BalanceType std = btt.get("STD");
			Balance b = null;
			for(Enumeration e1 = bt.balances(std); e1.hasMoreElements();){
				b = (Balance)e1.nextElement();
				Account a = b.getAccount();
				Currency c = b.getCurrency();
				a.setAmount(opn, c, b.getAmount());
			}
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void payOut(){
		PayOutAlgorithm.payOut(this);
		at.notifyObservers();
		mt.notifyObservers();
		pt.notifyObservers();
	}
		
}
	