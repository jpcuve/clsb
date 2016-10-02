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
 * constructor is not invoked unless an object of type 'JMessagesView'
 * created in the main() method.
 */
public class JMessagesView extends Form {
/*

	private Messenger msg;
	
	public JMessagesView(Messenger msg){
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		
		this.msg = msg;
		for(Enumeration e = msg.elements(); e.hasMoreElements();){
			listBox1.addItem(e.nextElement());
		}
		msg.addObserver(this);
	}

	*/
/**
	 * JMessagesView overrides dispose so it can clean up the
	 * component list.
	 */
/*
	public void dispose()
	{
		super.dispose();
		components.dispose();
		msg.deleteObserver(this);
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
	ListBox listBox1 = new ListBox();

	private void initForm() { 
		this.setText("Messages");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(300, 300));

		listBox1.setDock(ControlDock.FILL);
		listBox1.setSize(new Point(300, 290));
		listBox1.setTabIndex(0);
		listBox1.setText("listBox1");
		listBox1.setIntegralHeight(false);
		listBox1.setUseTabStops(true);

		this.setNewControls(new Control[] {
							listBox1});
	}

	*/
/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
/*

	public void update(Observable o, Object arg){
		listBox1.addItem(msg.getLast());
	}
*/
}
