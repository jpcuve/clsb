package legacyb1;

import java.util.Enumeration;
import java.util.Hashtable;

public class PayOutAlgorithm{
	
	private static boolean isPOShort(Account a, Currency c){
		return (a.getPNP(c) - a.getPayInBalance(c) < 0);
	}
	
	private static boolean isPOLong(Account a, Currency c){
		return (a.getPNP(c) + a.getPayOutBalance(c) > 0);
	}
	
	private static double getCoverForEarlierClosingCurrencies(Account a, Clsb clsb, Currencies ct, TimeOfDay fctt){
		TimeOfDay now = clsb.getCurrent();
		double cmult = clsb.getCoverMultiplier();
		double totalespmtm = 0;
		double maxvm = 0;
		for(Enumeration e1 = ct.currencies(); e1.hasMoreElements();){
			Currency c = (Currency)e1.nextElement();
			if(c.getFCTT().isAfter(fctt)){
				double vm = c.getVolatilityMargin();
				if(isPOShort(a, c)){
					double esp = a.getExpectedShortPosition(c, now, fctt);
					double rate = c.getRate();
					double mult = 1 + vm * cmult;
					double espmtm = esp * mult * rate;
					totalespmtm += espmtm;
				}
			if(isPOLong(a, c)){ // "Long"
					maxvm = Math.max(maxvm, vm);
				}
			}
		}
		return totalespmtm * (1 + maxvm * cmult);
	}
	
	private static BTree fctts(Currencies ct){
		BTree fctts = new BTree(new TimeOfDayComparator());
		Hashtable h = new Hashtable();
		for(Enumeration e1 = ct.currencies(); e1.hasMoreElements();){
			Currency c = (Currency)e1.nextElement();
			TimeOfDay tod = c.getFCTT();
			fctts.addElement(tod);
			BTree bt2 = (BTree)h.get(tod);
			if(bt2 == null){
				bt2 = new BTree(new CurrencyPriorityComparator());
				h.put(tod, bt2);
			}
			bt2.addElement(c);
		}	
		for(Enumeration e1 = fctts.elements(false); e1.hasMoreElements();){
			TimeOfDay tod = (TimeOfDay)e1.nextElement();
			System.out.println(tod);
			BTree bt2 = (BTree)h.get(tod);
			for(Enumeration e2 = bt2.elements(); e2.hasMoreElements();){
				Currency c = (Currency)e2.nextElement();
				System.out.println("  " + c);
			}
		}
		return fctts;
	}
	
	private static void computeAvailablePayOut(Clsb clsb, Accounts at){
		TimeOfDay now = clsb.getCurrent();
		for(Enumeration e1 = at.accounts("SM"); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			a.computeAvailablePayOut(clsb);
			System.out.println("Account " + a.getName() + " av pay " + a.getAvailablePayOut());
		}
		for(Enumeration e1 = at.accountsPerAvailablePayOut(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			System.out.println("  Account=" + a.getName() + ", Available payout=" + a.getAvailablePayOut());
		}
	}
	
	public static void payOut(ClsSimulatorModel model){
		Messenger msg = model.getMessenger();
		Clsb clsb = model.getClsb();
		Accounts at = model.getAccounts();
		Currencies ct = model.getCurrencies();
		Balances bt = model.getBalances();
		Movements mt = model.getMovements();
		PayOuts pt = model.getPayOuts();
		computeAvailablePayOut(clsb, at);
		for(Enumeration e1 = at.accountsPerAvailablePayOut(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			Hashtable maxpos = new Hashtable();
			msg.add("Ac=" + a.getName() + ", av.payout=" + a.getAvailablePayOut());
			Enumeration e2 = fctts(ct).elements(false);
			if(e2.hasMoreElements()){
				TimeOfDay fctt = (TimeOfDay)e2.nextElement();
				msg.add("  fctt=" + fctt + " (skipped)");
				while(e2.hasMoreElements()){
					fctt = (TimeOfDay)e2.nextElement();
					msg.add("  fctt=" + fctt);
					double cecc = getCoverForEarlierClosingCurrencies(a, clsb, ct, fctt);
					msg.add("    cover ecc=" + cecc);
					double totalpnpmtm = 0;
					for(Enumeration e3 = a.currencies(); e3.hasMoreElements();){
						Currency c = (Currency)e3.nextElement();
						if(isPOLong(a, c) && c.getFCTT().isAfter(fctt)){
							double pnp = a.getPNP(c);
							double pnpmtm = pnp * c.getRate();
							msg.add("      pnp=" + pnpmtm + " (" + c.getName() + " " + pnp + ")");
							totalpnpmtm += pnpmtm;
						}
					}
					msg.add("    total pnp=" + totalpnpmtm);
					double maxpo = Math.max(totalpnpmtm - cecc, 0);
					msg.add("    maximum payout=" + maxpo);
					if(!(totalpnpmtm == 0 && cecc == 0)) maxpos.put(fctt, new Double(maxpo));
				}
			}
			for(e2 = ct.currenciesPerLiquidity(true); e2.hasMoreElements();){
				Currency c = (Currency)e2.nextElement();
				if(a.getCurrentBalance(c) > 0){
					double po1 = Math.min(-c.getMirrorAccount().getCurrentBalance(c), Math.min(a.getCurrentBalance(c), Math.max(0, a.getPNP(c))));
					// System.out.println("1.1 step 4=" + po1);
					double po1mtm = po1 * c.getRate();
					double minmaxpo = Double.MAX_VALUE;
					for(Enumeration e3 = maxpos.keys(); e3.hasMoreElements();){
						TimeOfDay fctt = (TimeOfDay)e3.nextElement();
						if(c.getFCTT().isAfter(fctt)){
							double maxpo = ((Double)maxpos.get(fctt)).doubleValue();
							minmaxpo = Math.min(minmaxpo, maxpo);
						}
					}
					double po2 = Math.min(po1mtm, Math.min(minmaxpo, a.getAvailablePayOut()));
					double po = po2 / c.getRate();
					// System.out.println("1.1 step 9=" + po);
					double ncb = a.getNetCashBalance();
					double vm = c.getVolatilityMargin();
					double po2vm = po2 * (1 + vm);
					boolean test = false;
					for(Enumeration e3 = ct.currencies(); e3.hasMoreElements();){
						Currency c3 = (Currency)e3.nextElement();
						if (a.getPNP(c3) < 0) test = true;
					}
					double excessmtm = po2vm - ncb;
					if(test && excessmtm > 0){
						double excess = excessmtm / c.getRate() / (1 - vm);
						po -= excess;
					}
					if(po < c.getMinimumPayOut() && po != a.getPNP(c)){
						po = 0;
					}

					msg.add("Pay-out: ac=" + a.getName() + ", amount=" + c.getName() + " " + po);
					if(po != 0){
						long t = System.currentTimeMillis();
						while(System.currentTimeMillis() - t < 1); // you never know...
						String ref = "PO" + Long.toString(System.currentTimeMillis());
						PayOut[] p = (new PayOut(ref, a, c, po)).split();
						try{
							for(int i = 0; i < p.length; i++){
								pt.save(p[i]);
								mt.book(p[i]);
								mt.bookOut(p[i], true);
							}
						}catch(Throwable ex){
							ex.printStackTrace();
							System.exit(1);
						}	
						double pomtm = po * c.getRate() * (1 + vm);
						a.setAvailablePayOut(a.getAvailablePayOut() - pomtm);
						for(Enumeration e3 = maxpos.keys(); e3.hasMoreElements();){
							TimeOfDay fctt = (TimeOfDay)e3.nextElement();
							if(c.getFCTT().isAfter(fctt)){
								double maxpo = ((Double)maxpos.get(fctt)).doubleValue();
								maxpo -= pomtm;
								maxpos.put(fctt, new Double(maxpo));
							}
						}
					}
				}
			}
		
				
		}
		bt.save();
	}

	/*
	public static void main(String[] args)throws ClassNotFoundException, SQLException, DBIOException{
		if(args.length < 1){
			System.out.println("Error: please specify ODBC DB name.");
			System.exit(1);
		}
		Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:" + args[0]);
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		BalanceTypes btt = new BalanceTypes(con);
		Balances bt = new Balances(con, at, btt, ct);
		bt.fill();
		MovementTypes mtt = new MovementTypes(con, att, btt);
		Movements mt = new Movements(con, mtt, ct, at);
		mt.clear();
		PayOuts pt = new PayOuts(con, ct, at, mt);
		Queue qi = new Queue(con, "Trs", "TrLegs", ct, at, btt, mt);
		Queue q = new Queue(con, "SettlementQueue","SettlementQueueLegs", ct, at, btt, mt);
		PayInSchedules pist = new PayInSchedules(con, at, ct);
		pist.fill();
		Clsb clsb = cb.get();
		TimeOfDay now = clsb.getCurrent();
		
		System.out.println("Current time=" + now);
		System.out.println("--------------------------------------------------------------");
		System.out.print("Assembling settlement queue...");
		try{
			qi.assemble(q);
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
		System.out.println("done (" + q.size() + " transactions)");
		
		System.out.println("--------------------------------------------------------------");
		System.out.println("Balances:");
		at.printAll();
		
		System.out.println("--------------------------------------------------------------");
		System.out.println("Pay-in schedules:");
		for(Enumeration e1 = at.accounts(); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			for(Enumeration e2 = ct.currencies(); e2.hasMoreElements();){
				Currency c = (Currency)e2.nextElement();
				CashFlow cf = a.getPayInSchedule(c);
				if(cf != null){
					System.out.println("Ac=" + a.getName() + ", ccy=" + c.getName());
					System.out.println(cf);
				}
			}
		}

		System.out.println("--------------------------------------------------------------");
		System.out.println("Accounts per available payout:");
		for(Enumeration e1 = at.accounts("SM"); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			a.computeAvailablePayOut(clsb, now);
		}
		for(Enumeration e1 = at.accountsPerAvailablePayOut(now); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			System.out.println("  Account=" + a.getName() + ", Available payout=" + a.getAvailablePayOut());
		}
		
		System.out.println("--------------------------------------------------------------");
		System.out.println("Currencies per liquidity:");
		for(Enumeration e1 = ct.currenciesPerLiquidity(true); e1.hasMoreElements();){
			Currency c = (Currency)e1.nextElement();
			System.out.println(c);
		}
		
		System.out.println("--------------------------------------------------------------");
		System.out.println("Currencies per FCTT:");
		BTree fctts = new BTree(new TimeOfDayComparator());
		Hashtable h = new Hashtable();
		for(Enumeration e1 = ct.currencies(); e1.hasMoreElements();){
			Currency c = (Currency)e1.nextElement();
			TimeOfDay tod = c.getFCTT();
			fctts.addElement(tod);
			BTree bt2 = (BTree)h.get(tod);
			if(bt2 == null){
				bt2 = new BTree(new CurrencyPriorityComparator());
				h.put(tod, bt2);
			}
			bt2.addElement(c);
		}	
		for(Enumeration e1 = fctts.elements(false); e1.hasMoreElements();){
			TimeOfDay tod = (TimeOfDay)e1.nextElement();
			System.out.println(tod);
			BTree bt2 = (BTree)h.get(tod);
			for(Enumeration e2 = bt2.elements(); e2.hasMoreElements();){
				Currency c = (Currency)e2.nextElement();
				System.out.println("  " + c);
			}
		}
		
		System.out.println("--------------------------------------------------------------");
		System.out.println("Go for it!:");
		for(Enumeration e1 = at.accountsPerAvailablePayOut(now); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			Hashtable maxpos = new Hashtable();
			System.out.println("Ac=" + a.getName() + ", av.payout=" + a.getAvailablePayOut());
			Enumeration e2 = fctts.elements(false);
			if(e2.hasMoreElements()){
				TimeOfDay fctt = (TimeOfDay)e2.nextElement();
				System.out.println("  fctt=" + fctt + " (skipped)");
				while(e2.hasMoreElements()){
					fctt = (TimeOfDay)e2.nextElement();
					System.out.println("  fctt=" + fctt);
					double cecc = getCoverForEarlierClosingCurrencies(a, clsb, ct, now, fctt);
					System.out.println("    cover ecc=" + cecc);
					double totalpnpmtm = 0;
					for(Enumeration e3 = a.currencies(); e3.hasMoreElements();){
						Currency c = (Currency)e3.nextElement();
						if(isPOLong(a, c) && c.getFCTT().isAfter(fctt)){
							double pnp = a.getPNP(c);
							double pnpmtm = pnp * c.getRate();
							System.out.println("      pnp=" + pnpmtm + " (" + c.getName() + " " + pnp + ")");
							totalpnpmtm += pnpmtm;
						}
					}
					System.out.println("    total pnp=" + totalpnpmtm);
					double maxpo = Math.max(totalpnpmtm - cecc, 0);
					System.out.println("    maximum payout=" + maxpo);
					if(!(totalpnpmtm == 0 && cecc == 0)) maxpos.put(fctt, new Double(maxpo));
				}
			}
			for(e2 = ct.currenciesPerLiquidity(true); e2.hasMoreElements();){
				Currency c = (Currency)e2.nextElement();
				if(a.getCurrentBalance(c) > 0){
					double po1 = Math.min(-c.getMirrorAccount().getCurrentBalance(c), Math.min(a.getCurrentBalance(c), Math.max(0, a.getPNP(c))));
					// System.out.println("1.1 step 4=" + po1);
					double po1mtm = po1 * c.getRate();
					double minmaxpo = Double.MAX_VALUE;
					for(Enumeration e3 = maxpos.keys(); e3.hasMoreElements();){
						TimeOfDay fctt = (TimeOfDay)e3.nextElement();
						if(c.getFCTT().isAfter(fctt)){
							double maxpo = ((Double)maxpos.get(fctt)).doubleValue();
							minmaxpo = Math.min(minmaxpo, maxpo);
						}
					}
					double po2 = Math.min(po1mtm, Math.min(minmaxpo, a.getAvailablePayOut()));
					double po = po2 / c.getRate();
					// System.out.println("1.1 step 9=" + po);
					double ncb = a.getNetCashBalance();
					double vm = c.getVolatilityMargin();
					double po2vm = po2 * (1 + vm);
					boolean test = false;
					for(Enumeration e3 = ct.currencies(); e3.hasMoreElements();){
						Currency c3 = (Currency)e3.nextElement();
						if (a.getPNP(c3) < 0) test = true;
					}
					double excessmtm = po2vm - ncb;
					if(test && excessmtm > 0){
						double excess = excessmtm / c.getRate() / (1 - vm);
						po -= excess;
					}
					if(po < c.getMinimumPayOut() && po != a.getPNP(c)){
						po = 0;
					}

					System.out.println("Pay-out: ac=" + a.getName() + ", amount=" + c.getName() + " " + po);
					long t = System.currentTimeMillis();
					while(System.currentTimeMillis() - t < 1); // you never know...
					String ref = "PO" + Long.toString(System.currentTimeMillis());
					PayOut[] p = (new PayOut(ref, a, c, po)).split();
					for(int i = 0; i < p.length; i++){
						pt.save(p[i]);
						pt.getMovements().book(p[i]);
					}
						
					double pomtm = po * c.getRate() * (1 + vm);
					a.setAvailablePayOut(a.getAvailablePayOut() - pomtm);
					for(Enumeration e3 = maxpos.keys(); e3.hasMoreElements();){
						TimeOfDay fctt = (TimeOfDay)e3.nextElement();
						if(c.getFCTT().isAfter(fctt)){
							double maxpo = ((Double)maxpos.get(fctt)).doubleValue();
							maxpo -= pomtm;
							maxpos.put(fctt, new Double(maxpo));
						}
					}
				}
			}
		
				
		}
		System.out.println("--------------------------------------------------------------");
		System.out.println("Balances:");
		at.printAll();

		
	}
	*/
	
}