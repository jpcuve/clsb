package legacyb1;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class JBundleCanvas extends Canvas implements MouseListener, MouseMotionListener{
	Bundle b;
	Vector v;
	Hashtable as;
	
	public JBundleCanvas(Bundle b){
		this.b = b;
		v = new Vector();
		as = new Hashtable();
		for(Enumeration e = b.transactions(); e.hasMoreElements();){
			Tr trx = (Tr)e.nextElement();
			addTransaction(trx);
			for(int i = 0; i < 2; i++){
				Side s1 = (i == 0) ? trx.getFirstSide() : trx.getSecondSide();
				Account a = s1.getAccount();
				Side s2 = (Side)as.get(a);
				if (s2 != null){
					try{
						s1.net(s2);
					}catch(IllegalAccountException e1){}
				}
				as.put(a, s1);
			}
		}

	}
	
	public void addTransaction(Tr t){
		Tr t1 = t.cloneToTr();
		for(Enumeration e = v.elements(); e.hasMoreElements();){
			Tr t2 = (Tr)e.nextElement();
			try{
				t2.net(t1);
				return;
			}catch(Throwable ex){}
		}
		v.addElement(t1);
	}
	
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}

	public void mouseDragged(MouseEvent e){}

	public void mouseMoved(MouseEvent e){}

	public void paint(Graphics g){
		Hashtable ht = new Hashtable();
		Dimension dim = getSize();
		int w = dim.width;
		int h = dim.height;
		g.setClip(0, 0, w, h);
		int cx = w / 2;
		int cy = h / 2;
		int s = as.size();
		int r = 5;
		int l = Math.min(cx, cy) - r;
		double a = 0;
		double ainc = Math.PI * 2 / s;
		for(Enumeration e = as.keys(); e.hasMoreElements();){
			Account ac = (Account)e.nextElement();
			int px = cx - (new Double(Math.cos(a) * l)).intValue();
			int py = cy + (new Double(Math.sin(a) * l)).intValue();
			ht.put(ac, new Dimension(px, py));
			g.fillOval(px - r, py - r, 2 * r, 2 * r);
			g.drawString(ac.getName(), px + r, py);
			a += ainc;
		}
		for(Enumeration e1 = v.elements(); e1.hasMoreElements();){
			Tr t = (Tr)e1.nextElement();
			Account a1 = t.getFirstAccount();
			Dimension d1 = (Dimension)ht.get(a1);
			Account a2 = t.getSecondAccount();
			Dimension d2 = (Dimension)ht.get(a2);
			// a = Math.atan((d2.height - d1.height) / (d2.width - d1.width));
			/*
			s = t.size();
			for(Enumeration e2 = t.legs(); e2.hasMoreElements();){
				Leg ll = (Leg)e2.nextElement();
			}
			*/
			g.drawLine(d1.width, d1.height, d2.width, d2.height);
			g.drawString(Double.toString(t.getApproximateSettlementAmount()), (d1.width + 3 * d2.width) / 4, (d1.height + 3 * d2.height) / 4);
		}
				
			
		// super.paint(g);
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(200, 200);
	}
	
}