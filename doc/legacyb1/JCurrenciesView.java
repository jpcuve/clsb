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
 * constructor is not invoked unless an object of type 'Form1'
 * created in the main() method.
 */
public class JCurrenciesView extends Form { 
/*
	private CurrencyGroups cgt;
	private Currencies ct;
	
	private String sel;
	
	public JCurrenciesView(CurrencyGroups cgt, Currencies ct)
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		this.cgt = cgt;
		this.ct = ct;
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

	private void treeView1_afterSelect(Object source, TreeViewEvent e) {
		TreeView tv = treeView1;
		TreeNode tn = tv.getSelectedNode();
		sel = tn.getText();
		displayCurrencies();
	}

	*/
/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
/*
	Container components = new Container();
	ListView listView1 = new ListView();
	TreeView treeView1 = new TreeView();
	Splitter splitter1 = new Splitter();

	private void initForm() { 
		this.setText("Currencies");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(520, 159));

		listView1.setDock(ControlDock.FILL);
		listView1.setLocation(new Point(123, 0));
		listView1.setSize(new Point(397, 159));
		listView1.setTabIndex(0);
		listView1.setText("listView1");
		listView1.setView(ViewEnum.REPORT);

		treeView1.setDock(ControlDock.LEFT);
		treeView1.setSize(new Point(120, 159));
		treeView1.setTabIndex(1);
		treeView1.setText("treeView1");
		treeView1.setIndent(19);
		treeView1.addOnAfterSelect(new TreeViewEventHandler(this.treeView1_afterSelect));

		splitter1.setDock(ControlDock.LEFT);
		splitter1.setLocation(new Point(120, 0));
		splitter1.setSize(new Point(3, 159));
		splitter1.setTabIndex(2);
		splitter1.setTabStop(false);

		this.setNewControls(new Control[] {
							splitter1, 
							treeView1, 
							listView1});
	}

	*/
/**
	 * The main entry point for the application. 
	 *
	 * @@param args Array of parameters passed to the application
	 * via the command line.
	 */
/*

	public void displayTreeView(){
		TreeView tv = treeView1;
		TreeNode tn1 = new TreeNode("All");
		tv.addNode(tn1);
		for(Enumeration e1 = cgt.currencyGroups(); e1.hasMoreElements();){
			CurrencyGroup cg = (CurrencyGroup)e1.nextElement();
			TreeNode tn2 = new TreeNode(cg.getName());
			tn1.addNode(tn2);
		}
	}

	public void displayCurrencies(){
		ListView lv = listView1;
		lv.clear();
		lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
		lv.beginUpdate();
		lv.addColumn("ISO", 48, HorizontalAlignment.LEFT);
		lv.addColumn("Rate", 64, HorizontalAlignment.RIGHT);
		lv.addColumn("VM", 64, HorizontalAlignment.RIGHT);
		lv.addColumn("Gst", 64, HorizontalAlignment.RIGHT);
		lv.addColumn("Bal.fact.", 64, HorizontalAlignment.RIGHT);
		lv.addColumn("RTGS open", 64, HorizontalAlignment.RIGHT);
		lv.addColumn("RTGS close", 64, HorizontalAlignment.RIGHT);
		lv.addColumn("FCTT", 64, HorizontalAlignment.RIGHT);
		String[] s = new String[7];
		for(Enumeration e = ct.currenciesPerLiquidity(true); e.hasMoreElements();){
			Currency c = (Currency)e.nextElement();
			int pr = c.getPrecision();
			if(sel.equals("All") || sel.equals(c.getCurrencyGroup().getName())){
				s[0] = Value.formatNumber(c.getBaseRate(), pr + 2) + ((c.getQuote()) ? "*" : " ");
				s[1] = Value.formatNumber(c.getVolatilityMargin() * 100, 2) + "%";
				s[2] = Value.formatNumber(c.getGrossSplitThreshold(), pr, NumberFormat.GROUP_DIGITS);
				s[3] = Value.formatNumber(c.getBalanceFactor());
				s[4] = c.getRTGSOpen().toString();
				s[5] = c.getRTGSClose().toString();
				s[6] = c.getFCTT().toString();
				ListItem lt = new ListItem(c.getName(), s);
				lv.addItem(lt);
			}
		}
		lv.endUpdate();
	}
	
*/
}
