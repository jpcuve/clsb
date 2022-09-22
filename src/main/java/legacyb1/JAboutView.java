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
 * constructor is not invoked unless an object of type 'JAboutView'
 * created in the main() method.
 */
public class JAboutView extends Form
{
/*
	public JAboutView()
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
	}

	*/
/**
	 * JAboutView overrides dispose so it can clean up the
	 * component list.
	 */
/*
	public void dispose()
	{
		super.dispose();
		components.dispose();
	}

	private void button1_click(Object source, Event e) {
		close();
	}

	*/
/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
/*
	Container components = new Container();
	Button button1 = new Button();

	private void initForm() { 
		button1.setLocation(new Point(120, 128));
		button1.setSize(new Point(56, 24));
		button1.setTabIndex(0);
		button1.setText("OK");
		button1.addOnClick(new EventHandler(this.button1_click));

		this.setText("About CLS Simulator");
		this.setAcceptButton(button1);
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(300, 156));

		this.setNewControls(new Control[] {
							button1});
	}

	*/
/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
/*
	public static void main(String args[])
	{
		Application.run(new JAboutView());
	}
*/
}
