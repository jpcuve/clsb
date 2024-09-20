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
 * constructor is not invoked unless an object of type 'TrsView'
 * created in the main() method.
 */
public class JTrsView extends Form
{
/*
	private Queue q;
	private Currencies ct;
	
	public JTrsView(Queue q, Currencies ct)
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		this.q = q;
		this.ct = ct;
		try{
			displayTrs();
		}catch(SQLException ex){}
	}

	*/
/**
	 * TrsView overrides dispose so it can clean up the
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


	private void listView1_doubleClick(Object source, Event e)
	{
		ListItem[] lis = listView1.getSelectedItems();
		for(int i = 0; i < lis.length; i++){
			Tr t = null;
			try{
				t = q.get(lis[i].getText());
			}catch(Throwable ex){
				ex.printStackTrace();
				System.exit(1);
			}
			JTrView trv = new JTrView(t);
			JCLS f = (JCLS)getMDIParent();
			f.addForm(trv);
			trv.setMDIParent(f);
			trv.setVisible(true);
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
	ListView listView1 = new ListView();

	private void initForm()
	{
		this.setText("Transactions");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(661, 192));

		listView1.setDock(ControlDock.FILL);
		listView1.setSize(new Point(661, 192));
		listView1.setTabIndex(0);
		listView1.setText("listView1");
		listView1.setView(ViewEnum.REPORT);
		listView1.addOnDoubleClick(new EventHandler(this.listView1_doubleClick));

		this.setNewControls(new Control[] {
												listView1});
	}


	public void displayTrs() throws SQLException{
		ListView lv = listView1;
		lv.clear();
		lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
		lv.beginUpdate();
		lv.addColumn("Ref", 48, HorizontalAlignment.LEFT);
		lv.addColumn("Status", 64, HorizontalAlignment.LEFT);
		lv.addColumn("Parent", 64, HorizontalAlignment.LEFT);
		lv.addColumn("Ac#1", 64, HorizontalAlignment.LEFT);
		lv.addColumn("Ac#2", 64, HorizontalAlignment.LEFT);
		lv.addColumn("Settl.am.", 64, HorizontalAlignment.RIGHT);
		lv.addColumn("Buy ccy", 40, HorizontalAlignment.LEFT);
		lv.addColumn("Buy amount", 64, HorizontalAlignment.RIGHT);
		lv.addColumn("Sell ccy", 40, HorizontalAlignment.LEFT);
		lv.addColumn("Sell amount", 64, HorizontalAlignment.RIGHT);
		String[] s = new String[9];
		Currency c = ct.getBase();
		for(Enumeration e = q.transactions(); e.hasMoreElements();){
			Tr t = (Tr)e.nextElement();
			s[0] = t.getStatus();
			s[1] = t.getParentReference();
			s[2] = t.getFirstAccount().getName();
			s[3] = t.getSecondAccount().getName();
			s[4] = Value.formatNumber(t.getApproximateSettlementAmount(), c.getPrecision(), NumberFormat.GROUP_DIGITS);
			if(t.getClass().getName().equals("GrossTransaction")){
				for(Enumeration e2 = t.legs(); e2.hasMoreElements();){
					Leg l = (Leg)e2.nextElement();
					int i = l.getDir() ? 2 : 0;
					Currency ccy = l.getCurrency();
					s[5 + i] = ccy.getName();
					s[6 + i] = Value.formatNumber(l.getAmount(), ccy.getPrecision(), NumberFormat.GROUP_DIGITS);
				}
			}else{
				s[5] = s[6] = s[7] = s[8] = "-";
			}
			ListItem lt = new ListItem(t.getReference(), s);
			lv.addItem(lt);
		}
		lv.endUpdate();
	}
		
*/
}
