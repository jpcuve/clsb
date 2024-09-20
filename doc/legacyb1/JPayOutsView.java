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
public class JPayOutsView extends Form {
/*

	private PayOuts pt;
	
	public JPayOutsView(PayOuts pt)
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		this.pt = pt;
		pt.addObserver(this);
		displayPayOuts();
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
		
		pt.deleteObserver(this);
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
	ListView listView1 = new ListView();

	private void initForm() { 
		this.setText("PayOuts");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(374, 261));

		listView1.setDock(ControlDock.FILL);
		listView1.setSize(new Point(374, 261));
		listView1.setTabIndex(0);
		listView1.setText("listView1");
		listView1.setView(ViewEnum.REPORT);

		this.setNewControls(new Control[] {
							listView1});
	}


	public void update(Observable o, Object arg){
		displayPayOuts();
	}
	
	public void displayPayOuts(){
		try{
			ListView lv = listView1;
			lv.clear();
			lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
			lv.beginUpdate();
			lv.addColumn("Reference", 112, HorizontalAlignment.LEFT);
			lv.addColumn("Account", 64, HorizontalAlignment.LEFT);
			lv.addColumn("Ccy", 48, HorizontalAlignment.RIGHT);
			lv.addColumn("Amount", 80, HorizontalAlignment.RIGHT);
			String[] s = new String[3];
			for(Enumeration e = pt.payOuts(); e.hasMoreElements();){
				PayOut p = (PayOut)e.nextElement();
				Currency c = p.getCurrency();
				s[0] = p.getAccount().getName();
				s[1] = c.getName();
				s[2] = Value.formatNumber(p.getAmount(), c.getPrecision(), NumberFormat.GROUP_DIGITS);
				ListItem lt = new ListItem(p.getReference(), s);
				lv.addItem(lt);
			}
			lv.endUpdate();
		}catch(Throwable ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}
		
*/
}
