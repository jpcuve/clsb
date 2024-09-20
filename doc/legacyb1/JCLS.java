package legacyb1;

/*
import com.ms.wfc.app.*;
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
import com.ms.wfc.html.*;
*/
// import java.awt.Cursor;

/**
 * This class can take a variable number of parameters on the command
 * line. Program execution begins with the main() method. The class
 * constructor is not invoked unless an object of type 'Form1'
 * created in the main() method.
 */
public class JCLS extends Form
{
/*
	private ClsSimulatorModel model;
	private Vector forms;
	
	public JCLS(ClsSimulatorModel model)
	{
		super();

		// Required for Visual J++ Form Designer support
		initForm();		

		// TODO: Add any constructor code after initForm call
		this.model = model;
		forms = new Vector();
	}
	
	public void addForm(Form f){
		forms.addElement(f);
	}
	
	public void removeForm(Form f){
		forms.removeElement(f);
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
	}

	private void menuItem4_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		Balances bt = model.getBalances();
		Accounts at = model.getAccounts();
		AccountTypes att = model.getAccountTypes();
		BalanceTypes btt = model.getBalanceTypes();
		Currencies ct = model.getCurrencies();
		JAccountsView av = new JAccountsView(att, at, btt, bt, ct);
		addForm(av);
		av.setMDIParent(this);
		av.setVisible(true);
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem3_click(Object source, Event e){
		Queue q = model.getSettlementQueue();
		setCursor(Cursor.WAIT);
		try{
			q.processSequentially();
		}catch(Throwable ex){
			System.exit(1);
		}finally{
			setCursor(Cursor.DEFAULT);
		}
	}

	private void menuItem6_click(Object source, Event e){
		setCursor(Cursor.WAIT);
		CurrencyGroups cgt = model.getCurrencyGroups();
		Currencies ct = model.getCurrencies();
		JCurrenciesView cv = new JCurrenciesView(cgt, ct);
		addForm(cv);
		cv.setMDIParent(this);
		cv.setVisible(true);
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem5_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		JTrsView tv = new JTrsView(model.getTransactions(), model.getCurrencies());
		addForm(tv);
		tv.setMDIParent(this);
		tv.setVisible(true);
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem8_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		JQueueView qv = new JQueueView(model.getSettlementQueue(), model.getCurrencies());
		addForm(qv);
		qv.setMDIParent(this);
		qv.setVisible(true);
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem10_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		Movements mt = model.getMovements();
		JMovementsView mv = new JMovementsView(mt);
		addForm(mv);
		mv.setMDIParent(this);
		mv.setVisible(true);
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem13_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		model.assembleSettlementQueue();
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem20_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		model.clearMovements();
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem11_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		model.balancesSOD();
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem18_click(Object source, Event e){
		JAboutView about = new JAboutView();
		about.showDialog();
	}

	private void menuItem21_click(Object source, Event e){
		setCursor(Cursor.WAIT);
		PayOuts pt = model.getPayOuts();
		JPayOutsView pv = new JPayOutsView(pt);
		addForm(pv);
		pv.setMDIParent(this);
		pv.setVisible(true);
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem25_click(Object source, Event e){
		setCursor(Cursor.WAIT);
		model.payOut();
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem24_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		model.clearPayOuts();
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem27_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		Clsb clsb = model.getClsb();
		Accounts at = model.getAccounts();
		Currencies ct = model.getCurrencies();
		PayInSchedules pist = model.getPayInSchedules();
		JPayInSchedulesView pisv = new JPayInSchedulesView(clsb, at, ct, pist);
		addForm(pisv);
		pisv.setMDIParent(this);
		pisv.setVisible(true);
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem28_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		Messenger msg = model.getMessenger();
		JMessagesView msgv = new JMessagesView(msg);
		addForm(msgv);
		msgv.setMDIParent(this);
		msgv.setVisible(true);
		setCursor(Cursor.DEFAULT);
	}

	private void menuItem22_click(Object source, Event e) {
		setCursor(Cursor.WAIT);
		model.clearSettlementQueue();
		setCursor(Cursor.DEFAULT);
	}

	*/
/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
/*
	Container components = new Container();
	MainMenu mainMenu1 = new MainMenu();
	MenuItem menuItem1 = new MenuItem();
	MenuItem menuItem2 = new MenuItem();
	MenuItem menuItem3 = new MenuItem();
	MenuItem menuItem4 = new MenuItem();
	MenuItem menuItem5 = new MenuItem();
	MenuItem menuItem12 = new MenuItem();
	MenuItem menuItem11 = new MenuItem();
	MenuItem menuItem6 = new MenuItem();
	MenuItem menuItem7 = new MenuItem();
	MenuItem menuItem8 = new MenuItem();
	MenuItem menuItem9 = new MenuItem();
	MenuItem menuItem10 = new MenuItem();
	MenuItem menuItem13 = new MenuItem();
	MenuItem menuItem14 = new MenuItem();
	MenuItem menuItem15 = new MenuItem();
	MenuItem menuItem16 = new MenuItem();
	MenuItem menuItem17 = new MenuItem();
	MenuItem menuItem18 = new MenuItem();
	StatusBar statusBar1 = new StatusBar();
	MenuItem menuItem19 = new MenuItem();
	MenuItem menuItem20 = new MenuItem();
	MenuItem menuItem21 = new MenuItem();
	MenuItem menuItem22 = new MenuItem();
	MenuItem menuItem23 = new MenuItem();
	MenuItem menuItem24 = new MenuItem();
	MenuItem menuItem25 = new MenuItem();
	MenuItem menuItem26 = new MenuItem();
	MenuItem menuItem27 = new MenuItem();
	MenuItem menuItem28 = new MenuItem();
	MenuItem menuItem29 = new MenuItem();
	MenuItem menuItem30 = new MenuItem();
	MenuItem menuItem31 = new MenuItem();
	MenuItem menuItem32 = new MenuItem();
	MenuItem menuItem33 = new MenuItem();
	MenuItem menuItem34 = new MenuItem();
	MenuItem menuItem35 = new MenuItem();
	MenuItem menuItem36 = new MenuItem();
	MenuItem menuItem37 = new MenuItem();
	MenuItem menuItem38 = new MenuItem();
	MenuItem menuItem39 = new MenuItem();
	MenuItem menuItem40 = new MenuItem();
	Rebar rebar1 = new Rebar();
	DateTimePicker dateTimePicker1 = new DateTimePicker();
	DateTimePicker dateTimePicker2 = new DateTimePicker();

	private void initForm() { 
		menuItem4.setText("&Accounts");
		menuItem4.addOnClick(new EventHandler(this.menuItem4_click));

		menuItem5.setText("&Transactions");
		menuItem5.addOnClick(new EventHandler(this.menuItem5_click));

		menuItem12.setText("&EOD");

		menuItem11.setText("&SOD");
		menuItem11.addOnClick(new EventHandler(this.menuItem11_click));

		menuItem3.setMenuItems(new MenuItem[] {
							   menuItem11, 
							   menuItem12});
		menuItem3.setText("&Balances");
		menuItem3.addOnClick(new EventHandler(this.menuItem3_click));

		menuItem6.setText("&Currencies");
		menuItem6.addOnClick(new EventHandler(this.menuItem6_click));

		menuItem7.setMDIList(true);
		menuItem7.setText("&Window");

		menuItem8.setText("&Settlement Queue");
		menuItem8.addOnClick(new EventHandler(this.menuItem8_click));

		menuItem10.setText("&Movements");
		menuItem10.addOnClick(new EventHandler(this.menuItem10_click));

		menuItem13.setText("&Assemble");
		menuItem13.addOnClick(new EventHandler(this.menuItem13_click));

		menuItem14.setText("Settle &once");

		menuItem15.setText("Settle &repeat");

		menuItem18.setText("&About...");
		menuItem18.addOnClick(new EventHandler(this.menuItem18_click));

		menuItem17.setMenuItems(new MenuItem[] {
								menuItem18});
		menuItem17.setText("&Help");

		statusBar1.setBackColor(Color.CONTROL);
		statusBar1.setLocation(new Point(0, 511));
		statusBar1.setSize(new Point(687, 16));
		statusBar1.setTabIndex(2);
		statusBar1.setText("statusBar1");

		menuItem20.setText("&Clear");
		menuItem20.addOnClick(new EventHandler(this.menuItem20_click));

		menuItem19.setMenuItems(new MenuItem[] {
								menuItem20});
		menuItem19.setText("&Movements");

		menuItem21.setText("&PayOuts");
		menuItem21.addOnClick(new EventHandler(this.menuItem21_click));

		menuItem22.setText("&Clear");
		menuItem22.addOnClick(new EventHandler(this.menuItem22_click));

		menuItem23.setText("-");

		menuItem9.setMenuItems(new MenuItem[] {
							   menuItem22, 
							   menuItem23, 
							   menuItem13, 
							   menuItem14, 
							   menuItem15});
		menuItem9.setText("&Settlement queue");

		menuItem24.setText("&Clear");
		menuItem24.addOnClick(new EventHandler(this.menuItem24_click));

		menuItem25.setText("&Pay out");
		menuItem25.addOnClick(new EventHandler(this.menuItem25_click));

		menuItem26.setText("-");

		menuItem16.setMenuItems(new MenuItem[] {
								menuItem24, 
								menuItem26, 
								menuItem25});
		menuItem16.setText("&PayOuts");

		menuItem2.setMenuItems(new MenuItem[] {
							   menuItem3, 
							   menuItem19, 
							   menuItem9, 
							   menuItem16});
		menuItem2.setText("&Tools");

		menuItem27.setText("Pay&InSchedules");
		menuItem27.addOnClick(new EventHandler(this.menuItem27_click));

		menuItem28.setText("Me&ssages");
		menuItem28.addOnClick(new EventHandler(this.menuItem28_click));

		menuItem29.setText("-");

		menuItem1.setMenuItems(new MenuItem[] {
							   menuItem28, 
							   menuItem29, 
							   menuItem4, 
							   menuItem6, 
							   menuItem5, 
							   menuItem8, 
							   menuItem10, 
							   menuItem21, 
							   menuItem27});
		menuItem1.setText("&View");

		menuItem32.setShortcut(Shortcut.CTRL_O);
		menuItem32.setText("&Open model...");

		menuItem33.setText("&Close model");

		menuItem34.setText("E&xit");

		menuItem35.setText("-");

		menuItem30.setMenuItems(new MenuItem[] {
								menuItem32, 
								menuItem33, 
								menuItem35, 
								menuItem34});
		menuItem30.setText("&File");

		menuItem36.setShortcut(Shortcut.CTRL_X);
		menuItem36.setText("Cu&t");

		menuItem37.setShortcut(Shortcut.CTRL_C);
		menuItem37.setText("&Copy");

		menuItem38.setShortcut(Shortcut.CTRL_V);
		menuItem38.setText("&Paste");

		menuItem39.setShortcut(Shortcut.CTRL_A);
		menuItem39.setText("Select &All");

		menuItem40.setText("-");

		menuItem31.setMenuItems(new MenuItem[] {
								menuItem36, 
								menuItem37, 
								menuItem38, 
								menuItem40, 
								menuItem39});
		menuItem31.setText("&Edit");

		mainMenu1.setMenuItems(new MenuItem[] {
							   menuItem30, 
							   menuItem31, 
							   menuItem1, 
							   menuItem2, 
							   menuItem7, 
							   menuItem17});
		*/
/* @designTimeOnly mainMenu1.setLocation(new Point(24, 64)); */
/*

		this.setText("CLS simulator");
		this.setAutoScaleBaseSize(new Point(5, 13));
		this.setClientSize(new Point(687, 527));
		this.setIsMDIContainer(true);
		this.setMenu(mainMenu1);

		rebar1.setSize(new Point(687, 30));
		rebar1.setTabIndex(3);
		rebar1.setText("rebar1");
		rebar1.setBands(new RebarBand[] {});
		rebar1.setImageList(null);

		dateTimePicker1.setLocation(new Point(80, 8));
		dateTimePicker1.setSize(new Point(72, 20));
		dateTimePicker1.setTabIndex(5);
		dateTimePicker1.setValue(new Time(599310960461400000l));
		dateTimePicker1.setShowUpDown(true);
		dateTimePicker1.setFormat(DateTimePickerFormat.TIME);

		dateTimePicker2.setLocation(new Point(8, 8));
		dateTimePicker2.setSize(new Point(72, 20));
		dateTimePicker2.setTabIndex(4);
		dateTimePicker2.setValue(new Time(599310960461400000l));
		dateTimePicker2.setFormat(DateTimePickerFormat.SHORT);

		this.setNewControls(new Control[] {
							dateTimePicker2, 
							dateTimePicker1, 
							rebar1, 
							statusBar1});
	}

	*/
/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
/*
	public static void main(String args[])throws ClassNotFoundException, SQLException, DBIOException
	{
		Class.forName ("com.ms.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:cls");
		CLSBank cb = new CLSBank(con);
		AccountTypes att = new AccountTypes(con);
		Accounts at = new Accounts(con, att, cb);
		CurrencyGroups cgt = new CurrencyGroups(con);
		Currencies ct = new Currencies(con, cgt, at);
		BalanceTypes btt = new BalanceTypes(con);
		Balances bt = new Balances(con, at, btt, ct);
		bt.load();
		MovementTypes mtt = new MovementTypes(con, att, btt);
		Movements mt = new Movements(con, mtt, ct, at);
		PayOuts pt = new PayOuts(con, ct, at, mt);
		Queue qi = new Queue(con, "Trs", "TrLegs", ct, at, btt, mt);
		Queue qo = new Queue(con, "SettlementQueue", "SettlementQueueLegs", ct, at, btt, mt);
		PayInSchedules pist = new PayInSchedules(con, at, ct);
		pist.load();
		Messenger m = new Messenger();
		ClsSimulatorModel model = new ClsSimulatorModel(m, cb, att, at, cgt, ct, btt, bt, qi, qo, mt, pt, pist);
		Application.run(new JCLS(model));
	}
*/
}
