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
 * constructor is not invoked unless an object of type 'QueueView'
 * created in the main() method.
 */
public class JQueueView extends Form {
/*
{
	private Queue q;
	private Currencies ct;
	private JBundleControl bc;
	
	public JQueueView(Queue q, Currencies ct)
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		this.ct = ct;
		bc = new JBundleControl();
		bc.setDock(ControlDock.FILL);
		panel1.add(bc);
		setQueue(q);
	}
	
	public void setQueue(Queue nq){
		if(q != null) q.deleteObserver(this);
		q = nq;
		q.addObserver(this);
		try{
			displayQueue();
		}catch(SQLException ex){}
	}

	*/
/**
	 * QueueView overrides dispose so it can clean up the
	 * component list.
	 */
/*
	public void dispose()
	{
		super.dispose();
		components.dispose();
		
		q.deleteObserver(this);
		try{
			JCLS f = (JCLS)this.getMDIParent();
			f.removeForm(this);
		}catch(Throwable ex){
		}
	}

	private void listView1_click(Object source, Event e)
	{
		displayBundle();
	}

	*/
/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
/*
	Container components = new Container();
	Splitter splitter1 = new Splitter();
	Panel panel1 = new Panel();
	ListView listView1 = new ListView();

	private void initForm()
	{
		this.setText("Settlement Queue");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(526, 370));

		splitter1.setDock(ControlDock.LEFT);
		splitter1.setLocation(new Point(304, 0));
		splitter1.setSize(new Point(3, 370));
		splitter1.setTabIndex(1);
		splitter1.setTabStop(false);

		panel1.setDock(ControlDock.FILL);
		panel1.setLocation(new Point(307, 0));
		panel1.setSize(new Point(219, 370));
		panel1.setTabIndex(2);
		panel1.setText("panel1");

		listView1.setDock(ControlDock.LEFT);
		listView1.setSize(new Point(304, 370));
		listView1.setTabIndex(0);
		listView1.setText("listView1");
		listView1.setView(ViewEnum.REPORT);
		listView1.addOnClick(new EventHandler(this.listView1_click));

		this.setNewControls(new Control[] {
												panel1, 
												splitter1, 
												listView1});
	}

	
	public void update(Observable obs, Object o){
		if (obs == this.q){
			try{
				displayQueue();
			}catch(SQLException ex){}
		}
	}
	
	public void displayQueue() throws SQLException{
		ListView lv = listView1;
		lv.clear();
		lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
		lv.beginUpdate();
		lv.addColumn("Ref", 112, HorizontalAlignment.LEFT);
		lv.addColumn("Parent", 80, HorizontalAlignment.LEFT);
		lv.addColumn("Ac#1", 56, HorizontalAlignment.LEFT);
		lv.addColumn("Ac#2", 56, HorizontalAlignment.LEFT);
		lv.addColumn("Settl.am.", 80, HorizontalAlignment.RIGHT);
		String[] s = new String[4];
		Currency c = ct.getBase();
		for(Enumeration e = q.transactions(); e.hasMoreElements();){
			Tr t = (Tr)e.nextElement();
			s[0] = t.getParentReference();
			s[1] = t.getFirstAccount().getName();
			s[2] = t.getSecondAccount().getName();
			s[3] = Value.formatNumber(t.getApproximateSettlementAmount(), 2, NumberFormat.GROUP_DIGITS);
			ListItem lt = new ListItem(t.getReference(), s);
			lv.addItem(lt);
		}
		lv.endUpdate();
		displayBundle();
	}
	
	public void displayBundle(){
		Bundle b = new Bundle();
		ListItem[] l = listView1.getSelectedItems();
		for(int i = 0; i < l.length; i++){
			System.out.println(l[i].toString());
			try{
				Tr t = q.get(l[i].toString());
				b.addTransaction(t);
			}catch(Throwable ex){
				ex.printStackTrace();
				System.exit(1);
			}
		}
		System.out.println(b);
		bc.setBundle(b);
	}
	
*/
}
