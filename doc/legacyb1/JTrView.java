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
 * constructor is not invoked unless an object of type 'TrView'
 * created in the main() method.
 */
public class JTrView extends Form
{
/*
	public JTrView(Tr t)
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		this.setText("Transaction: " + t.getReference());
		edit2.setText(t.getParentReference());
		edit3.setText(t.getStatus());
		String an1 = t.getFirstAccount().getName();
		String an2 = t.getSecondAccount().getName();
		edit1.setText(Double.toString(t.getApproximateSettlementAmount()));
		ListView lv = listView1;
		lv.clear();
		lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
		lv.beginUpdate();
		lv.addColumn("Dir", 48, HorizontalAlignment.LEFT);
		lv.addColumn("Ccy", 40, HorizontalAlignment.LEFT);
		lv.addColumn("Amount", 64, HorizontalAlignment.RIGHT);
		String[] s = new String[2];
		for(Enumeration e1 = t.legs(); e1.hasMoreElements();){
			Leg l = (Leg)e1.nextElement();
			Currency c = l.getCurrency();
			s[0] = c.getName();
			s[1] = Value.formatNumber(l.getAmount(), c.getPrecision(), NumberFormat.GROUP_DIGITS);
			ListItem lt = new ListItem(l.getDir() ? (an1 + " -> " + an2) : (an2 + " -> " + an1), s);
			lv.addItem(lt);
		}
		lv.endUpdate();
	}

	*/
/**
	 * TrView overrides dispose so it can clean up the
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




	*/
/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
/*
	Container components = new Container();
	Label label3 = new Label();
	Edit edit3 = new Edit();
	Label label2 = new Label();
	Edit edit2 = new Edit();
	Edit edit1 = new Edit();
	Label label1 = new Label();
	ListView listView1 = new ListView();
	Panel panel1 = new Panel();

	private void initForm()
	{
		this.setText("Transaction");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(300, 166));

		label3.setLocation(new Point(248, 8));
		label3.setSize(new Point(16, 16));
		label3.setTabIndex(3);
		label3.setTabStop(false);
		label3.setText("St");

		edit3.setEnabled(false);
		edit3.setLocation(new Point(264, 8));
		edit3.setSize(new Point(24, 20));
		edit3.setTabIndex(2);
		edit3.setText("edit3");

		label2.setLocation(new Point(8, 8));
		label2.setSize(new Point(64, 16));
		label2.setTabIndex(0);
		label2.setTabStop(false);
		label2.setText("Parent");

		edit2.setEnabled(false);
		edit2.setLocation(new Point(72, 8));
		edit2.setSize(new Point(168, 20));
		edit2.setTabIndex(1);
		edit2.setText("edit2");

		edit1.setEnabled(false);
		edit1.setLocation(new Point(72, 32));
		edit1.setSize(new Point(168, 20));
		edit1.setTabIndex(5);
		edit1.setText("edit1");

		label1.setLocation(new Point(8, 32));
		label1.setSize(new Point(56, 16));
		label1.setTabIndex(4);
		label1.setTabStop(false);
		label1.setText("Settl am");

		listView1.setDock(ControlDock.FILL);
		listView1.setLocation(new Point(0, 64));
		listView1.setSize(new Point(300, 102));
		listView1.setTabIndex(1);
		listView1.setText("listView1");
		listView1.setView(ViewEnum.REPORT);

		panel1.setDock(ControlDock.TOP);
		panel1.setSize(new Point(300, 64));
		panel1.setTabIndex(0);
		panel1.setText("panel1");

		this.setNewControls(new Control[] {
												listView1, 
												panel1});
		panel1.setNewControls(new Control[] {
													edit1, 
													label1, 
													label3, 
													edit3, 
													label2, 
													edit2});
	}

*/
}
