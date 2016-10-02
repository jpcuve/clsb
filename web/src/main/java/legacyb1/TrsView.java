package legacyb1;

import javax.swing.*;
import java.awt.*;

public class TrsView extends JPanel{
	private Queue q;
	
	public TrsView(Queue q){
		this.q = q;
		
		JTable table = new JTable(new TrsTableModel(q));
		JScrollPane dtable = new JScrollPane(table);
		setLayout(new BorderLayout());
		add(dtable, BorderLayout.CENTER);
	}
	
}