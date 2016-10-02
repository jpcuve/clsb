package legacyb1;

/*
import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;
import com.ms.wfc.util.*;
*/

/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'Form1' is
 * created in the main() method.
 */
public class JPayInSchedulesView extends Form  {
/*

	private Clsb clsb;
	private Accounts at;
	private Currencies ct;
	private PayInSchedules pist;
	
	private Account selAccount;
	private Currency selCurrency;
	
	public JPayInSchedulesView(Clsb clsb, Accounts at, Currencies ct, PayInSchedules pist)
	{
		// Required for Visual J++ Form Designer support
		initForm();

		// TODO: Add any constructor code after initForm call
		this.clsb = clsb;
		this.at = at;
		this.ct = ct;
		this.pist = pist;
		pist.load();
		displayTreeView();
	}

	*/
/**
	 * Form1 overrides dispose so it can clean up the
	 * component list.
	 */
/*
	public void dispose()
	{
		super.dispose();
		components.dispose();
		try{
			JCLS f = (JCLS)this.getMDIParent();
			f.removeForm(this);
		}catch(Throwable ex){
		}
	}

	private void treeView1_afterSelect(Object source, TreeViewEvent e)
	{
		TreeView tv = treeView1;
		TreeNode tn = tv.getSelectedNode();
		try{
			if(tn.getParent() == null){
				selAccount = at.get(tn.getText());
				selCurrency = null;
			}else{
				selAccount = at.get(tn.getParent().getText());
				selCurrency = ct.get(tn.getText());
			}
			displayTable();
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}

	*/
/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
/*
	Container components = new Container();
	TreeView treeView1 = new TreeView();
	Splitter splitter1 = new Splitter();
	ListView listView1 = new ListView();

	private void initForm() { 
		this.setText("PayInSchedules");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(616, 273));

		treeView1.setDock(ControlDock.LEFT);
		treeView1.setSize(new Point(121, 273));
		treeView1.setTabIndex(0);
		treeView1.setText("treeView1");
		treeView1.setIndent(19);
		treeView1.addOnAfterSelect(new TreeViewEventHandler(this.treeView1_afterSelect));

		splitter1.setDock(ControlDock.LEFT);
		splitter1.setLocation(new Point(121, 0));
		splitter1.setSize(new Point(3, 273));
		splitter1.setTabIndex(1);
		splitter1.setTabStop(false);

		listView1.setDock(ControlDock.FILL);
		listView1.setLocation(new Point(124, 0));
		listView1.setSize(new Point(492, 273));
		listView1.setTabIndex(2);
		listView1.setText("listView1");
		listView1.setAllowColumnReorder(true);
		listView1.setView(ViewEnum.REPORT);

		this.setNewControls(new Control[] {
							listView1, 
							splitter1, 
							treeView1});
	}

	*/
/**
	 * The main entry point for the application. 
	 *
	 * @@param args Array of parameters passed to the application
	 * via the command line.
	 */
/*

	public void update(Observable o, Object arg){
		displayTable();
	}
	
	public void displayTable(){
		if(selCurrency == null){
			displayPayInSummary(selAccount);
		}else{
			displayPayInSchedule(selAccount, selCurrency);
		}
	}

	public void displayTreeView(){
		TreeView tv = treeView1;
		tv.beginUpdate();
		for(Enumeration e1 = at.accounts("SM"); e1.hasMoreElements();){
			Account a = (Account)e1.nextElement();
			TreeNode tn1 = new TreeNode(a.getName());
			tv.addNode(tn1);
			for(Enumeration e2 = ct.currencies(); e2.hasMoreElements();){
				Currency c = (Currency)e2.nextElement();
				if(a.getPayInSchedule(c) != null){
					TreeNode tn2 = new TreeNode(c.getName());
					tn1.addNode(tn2);
				}
			}
		}
		tv.endUpdate();
	}
	
	public void displayPayInSummary(Account a){
		ListView lv = listView1;
		lv.clear();
		lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
		lv.beginUpdate();
		lv.addColumn("Ccy", 48, HorizontalAlignment.LEFT);
		lv.addColumn("Tot sch", 72, HorizontalAlignment.RIGHT);
		lv.addColumn("Cur sch", 72, HorizontalAlignment.RIGHT);
		lv.addColumn("Cur paid-in", 72, HorizontalAlignment.RIGHT);
		lv.addColumn("Status", 48, HorizontalAlignment.RIGHT);
		String[] s = new String[4];
		for(Enumeration e = ct.currencies(); e.hasMoreElements();){
			Currency c = (Currency)e.nextElement();
			CashFlow f = a.getPayInSchedule(c);
			if(f != null){
				int pr = c.getPrecision();
				double pisch = f.getCumulatedAmount(clsb.getCurrent());
				double piact = a.getPayInBalance(c);
				s[0] = Value.formatNumber(f.getTotalAmount(), pr, NumberFormat.GROUP_DIGITS);
				s[1] = Value.formatNumber(pisch, pr, NumberFormat.GROUP_DIGITS);
				s[2] = Value.formatNumber(piact, pr, NumberFormat.GROUP_DIGITS);
				s[3] = (piact > pisch) ? "EARLY" : ((piact < pisch) ? "LATE" : "OK");
				ListItem lt = new ListItem(c.getName(), s);
				lv.addItem(lt);
			}
		}
		lv.endUpdate();
	}
	
	public void displayPayInSchedule(Account a, Currency c){
		ListView lv = listView1;
		lv.clear();
		lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
		lv.beginUpdate();
		lv.addColumn("Time", 48, HorizontalAlignment.LEFT);
		lv.addColumn("Amount", 72, HorizontalAlignment.RIGHT);
		String[] s = new String[1];
		CashFlow cf = a.getPayInSchedule(c);
		int pr = c.getPrecision();
		for(Enumeration e = cf.flows(); e.hasMoreElements();){
			TimeOfDay tod = (TimeOfDay)e.nextElement();
			s[0] = Value.formatNumber(cf.getCumulatedAmount(tod), pr, NumberFormat.GROUP_DIGITS);
			ListItem lt = new ListItem(tod.toString(), s);
			lv.addItem(lt);
		}
		lv.endUpdate();
	}
*/

	
}