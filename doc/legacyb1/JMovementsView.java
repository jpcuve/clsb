package legacyb1;

/*
import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;
*/

/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'TrsView'
 * created in the main() method.
 */
public class JMovementsView extends Form {
/*

	private Movements mt;
	
	public JMovementsView(Movements mt)
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		this.mt = mt;
		mt.addObserver(this);
		displayMovements();
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
		
		mt.deleteObserver(this);
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
		this.setText("Movements");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(661, 192));

		listView1.setDock(ControlDock.FILL);
		listView1.setSize(new Point(661, 192));
		listView1.setTabIndex(0);
		listView1.setText("listView1");
		listView1.setView(ViewEnum.REPORT);

		this.setNewControls(new Control[] {
							listView1});
	}


	public void update(Observable o, Object arg){
		displayMovements();
	}
	
	public void displayMovements(){
		try{
			ListView lv = listView1;
			lv.clear();
			lv.setHeaderStyle(ColumnHeaderStyle.CLICKABLE);
			lv.beginUpdate();
			lv.addColumn("Type", 64, HorizontalAlignment.LEFT);
			lv.addColumn("Parent", 64, HorizontalAlignment.LEFT);
			lv.addColumn("DB", 64, HorizontalAlignment.LEFT);
			lv.addColumn("CR", 64, HorizontalAlignment.LEFT);
			lv.addColumn("Ccy", 64, HorizontalAlignment.RIGHT);
			lv.addColumn("Amount", 64, HorizontalAlignment.RIGHT);
			String[] s = new String[5];
			for(Enumeration e = mt.movements(); e.hasMoreElements();){
				Movement m = (Movement)e.nextElement();
				s[0] = m.getParentReference();
				s[1] = m.getDBAccount().getName();
				s[2] = m.getCRAccount().getName();
				s[3] = m.getCurrency().getName();
				s[4] = Double.toString(m.getAmount());
				ListItem lt = new ListItem(m.getMovementType().getID(), s);
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
