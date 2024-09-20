package legacyb1;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class QueueView extends JSplitPane implements ListSelectionListener{
	private QueueTableModel qtm;
	private BundlePanel dbp;
	
	public QueueView(Queue q){
		this.qtm = new QueueTableModel(q);		
		JTable table = new JTable(qtm);
		ListSelectionModel lsm = table.getSelectionModel();
		lsm.addListSelectionListener(this);
		JScrollPane dtable = new JScrollPane(table);
		dtable.setMinimumSize(new Dimension(300, 100));
		dbp = new BundlePanel();
		dbp.setMinimumSize(new Dimension(100, 100));
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setOneTouchExpandable(true);
		setContinuousLayout(false);
		setLeftComponent(dtable);
		setRightComponent(dbp);
	}
	
	public void valueChanged(ListSelectionEvent e){
		Bundle b = new Bundle();
		if(!e.getValueIsAdjusting()){
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (!lsm.isSelectionEmpty()){
				int minRow = lsm.getMinSelectionIndex();
				int maxRow = lsm.getMaxSelectionIndex();
				for(int i = minRow; i <= maxRow; i++){
					if(lsm.isSelectedIndex(i))	b.addTransaction(qtm.getTransaction(i));
				}
			}
			dbp.setBundle(b);
			dbp.repaint();
		}
	}

}