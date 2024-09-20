package legacyb1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class ClsSimulatorFrame extends JFrame implements WindowListener, ActionListener{
	
	private ClsSimulatorModel model;
	
	private JDesktopPane desktop;
	
	private JMenu file;
	private JMenuItem exit;
	private JMenu edit;
	private JMenuItem cut;
	private JMenuItem copy;
	private JMenuItem paste;
	private JMenu view;
	private JMenuItem accounts;
	private JMenuItem currencies;
	private JMenuItem transactions;
	private JMenu help;
	private JMenuItem about;

	
	// WindowListener implementation
	public void windowOpened(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowClosing(WindowEvent e){
		exit();
	}
	public void windowClosed(WindowEvent e){}
	
	//ActionListener implementation
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		System.out.println(action);
		if(action.equals(exit.getText())){ exit(); }
		if(action.equals(accounts.getText())){ accounts(); return; }
		if(action.equals(currencies.getText())){ currencies(); return; }
		if(action.equals(transactions.getText())){ transactions(); return; }
	}
		
	public void exit(){
		dispose();
		System.exit(0);
	}
	
	public void accounts(){
		AccountTypes att = model.getAccountTypes();
		Accounts at = model.getAccounts();
		Currencies ct = model.getCurrencies();
		BalanceTypes btt = model.getBalanceTypes();
		JInternalFrame f = new JInternalFrame("Accounts", true, true, true, true);
		f.getContentPane().add(new AccountsView(att, at, ct, btt));
		f.setSize(new Dimension(500,100));
		f.setLocation(new Point(20,20));
		desktop.add(f);
		f.show();
	}
	
	public void currencies(){
		CurrencyGroups cgt = model.getCurrencyGroups();
		Currencies ct = model.getCurrencies();
		JInternalFrame f = new JInternalFrame("Currencies", true, true, true, true);
		f.getContentPane().add(new CurrenciesView(cgt, ct));
		f.setSize(new Dimension(500,100));
		f.setLocation(new Point(20,20));
		desktop.add(f);
		f.show();
	}
	
	public void transactions(){
		Queue q = model.getTransactions();
		JInternalFrame f = new JInternalFrame("Transactions", true, true, true, true);
		f.getContentPane().add(new QueueView(q));
		f.setSize(new Dimension(500,300));
		f.setLocation(new Point(20,20));
		desktop.add(f);
		f.show();
	}
	
	public void about(){
		System.out.println("About");
	}
	
	public JMenuBar buildMenu(){
		JMenuBar menuBar = new JMenuBar();
		file = new JMenu("File");
		file.setMnemonic('F');
		exit = new JMenuItem("Exit");
		edit = new JMenu("Edit");
		edit.setMnemonic('E');
		cut = new JMenuItem("Cut");
		copy = new JMenuItem("Copy");
		paste = new JMenuItem("Paste");
		view = new JMenu("View");
		view.setMnemonic('V');
		accounts = new JMenuItem("Accounts...");
		currencies = new JMenuItem("Currencies...");
		transactions = new JMenuItem("Transactions...");
		help = new JMenu("Help");
		help.setMnemonic('H');
		about = new JMenuItem("About...");
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(view);
		menuBar.add(help);
		file.add(exit);
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		view.add(accounts);
		view.add(currencies);
		view.add(transactions);
		help.add(about);
		for(int i = 0; i < menuBar.getMenuCount(); i++){
			JMenu m = menuBar.getMenu(i);
			for(int j = 0; j < m.getItemCount(); j++){
				m.getItem(j).addActionListener(this);
			}
		}
		return menuBar;
	}
		

	
	public ClsSimulatorFrame(ClsSimulatorModel model){
		super("Cls Simulator");
		setSize(new Dimension(700, 500));
		this.model = model;
		addWindowListener(this);
		desktop = new JDesktopPane();
		getContentPane().add(desktop);
		// getRootPane().setLayeredPane(desktop);
		setJMenuBar(buildMenu());
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(750, 550);
	}
	
}