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
public class JAccountsView extends Form
{
/*
	private Balances bt;
	private AccountTypes att;
	private Accounts at;
	private Currencies ct;
	private BalanceTypes btt;
	private BalanceType stdType;
	private ImageList il;
	
	private Object sel;
	
	public JAccountsView( AccountTypes att, Accounts at, BalanceTypes btt, Balances bt, Currencies ct)
	{
		// Required for Visual J++ Form Designer support
		initForm();

		// TODO: Add any constructor code after initForm call
		this.bt = bt;
		this.att = att;
		this.at = at;
		this.ct = ct;
		this.btt = btt;
		try{
			stdType = btt.get("STD");
		}catch(IllegalBalanceTypeException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		at.addObserver(this);
		
		il = new ImageList();
		il.addImage(new Icon("images\\CheckBook.ico"));
		il.addImage(new Icon("images\\Note.ico"));
		treeView1.setImageList(il);
		
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
		
		at.deleteObserver(this);
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
		if(tn.getParent() == null){
			AccountType actyp = null;
			try{
				actyp = att.get(tn.getText());
			}catch(IllegalAccountTypeException ex){
				ex.printStackTrace();
				System.exit(1);
			}
			sel = actyp;
			displayTable();
		}else{
			Account a = null;
			try{
				a = at.get(tn.getText());
			}catch(IllegalAccountException ex){
				ex.printStackTrace();
				System.exit(1);
			}
			sel = a;
			displayTable();
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
		this.setText("Accounts");
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
		try{
			Account a = (Account)sel;
			displayBalances(a);
		}catch(ClassCastException ex){
			AccountType actyp = (AccountType)sel;
			displayAccounts(actyp);
		}
	}

	public void displayTreeView(){
		TreeView tv = treeView1;
		tv.beginUpdate();
		for(Enumeration e1 = att.accountTypes(); e1.hasMoreElements();){
			AccountType actyp = (AccountType)e1.nextElement();
			TreeNode tn1 = new TreeNode(actyp.getID());
			tn1.setImageIndex(0);
			tv.addNode(tn1);
			for(Enumeration e2 = actyp.accounts(); e2.hasMoreElements();){
				Account a = (Account)e2.nextElement();
				TreeNode tn2 = new TreeNode(a.getName());
				tn2.setImageIndex(1);
				tn2.setSelectedImageIndex(1);
				tn1.addNode(tn2);
			}
		}
		tv.endUpdate();
	}
	
	public void displayAccounts(AccountType actyp){
		ListView lv = listView1;
		lv.clear();
		lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
		lv.beginUpdate();
		lv.addColumn("#", 48, HorizontalAlignment.LEFT);
		lv.addColumn("ASPL", 72, HorizontalAlignment.RIGHT);
		lv.addColumn("NCC", 72, HorizontalAlignment.RIGHT);
		lv.addColumn("Net", 72, HorizontalAlignment.RIGHT);
		lv.addColumn("Net w/v.m.", 72, HorizontalAlignment.RIGHT);
		lv.addColumn("Long", 72, HorizontalAlignment.RIGHT);
		lv.addColumn("Short", 72, HorizontalAlignment.RIGHT);
		String[] s = new String[6];
		for(Enumeration e = actyp.accounts(); e.hasMoreElements();){
			Account a = (Account)e.nextElement();
			int pr = ct.getBase().getPrecision();
			s[0] = Value.formatNumber(a.getASPL(), pr, NumberFormat.GROUP_DIGITS);
			s[1] = Value.formatNumber(a.getNonCashCollateral(), pr, NumberFormat.GROUP_DIGITS);
			s[2] = Value.formatNumber(a.getPosition("Net", false), pr, NumberFormat.GROUP_DIGITS);
			s[3] = Value.formatNumber(a.getPosition("Net", true), pr, NumberFormat.GROUP_DIGITS);
			s[4] = Value.formatNumber(a.getPosition("Long", false), pr, NumberFormat.GROUP_DIGITS);
			s[5] = Value.formatNumber(a.getPosition("Short", false), pr, NumberFormat.GROUP_DIGITS);
			ListItem lt = new ListItem(a.getName(), s);
			lv.addItem(lt);
		}
		lv.endUpdate();
	}
		
	public void displayBalances(Account a){
		ListView lv = listView1;
		lv.clear();
		lv.beginUpdate();
		lv.addColumn("Ccy", 40, HorizontalAlignment.LEFT);
		int size = 0;
		for(Enumeration e1 = btt.balanceTypes(); e1.hasMoreElements();){
			BalanceType btyp = (BalanceType)e1.nextElement();
			lv.addColumn(btyp.getName(), 72, HorizontalAlignment.RIGHT);
			size++;
		}
		String[] s = new String[size];
		BTree bt = new BTree(new CurrencyPriorityComparator());
		for(Enumeration e1 = a.balances(); e1.hasMoreElements();){
			Balance b = (Balance)e1.nextElement();
			bt.addElement(b.getCurrency());
		}
		for(Enumeration e1 = bt.elements(); e1.hasMoreElements();){
			Currency c = (Currency)e1.nextElement();
			int pr = c.getPrecision();
			int i = 0;
			for(Enumeration e2 = btt.balanceTypes(); e2.hasMoreElements();){
				BalanceType btyp = (BalanceType)e2.nextElement();
				s[i++] = Value.formatNumber(a.getAmount(btyp, c), pr, NumberFormat.GROUP_DIGITS);
			}
			ListItem lt = new ListItem(c.getName(), s);
			lv.addItem(lt);
		}
		lv.endUpdate();
	}
*/

}