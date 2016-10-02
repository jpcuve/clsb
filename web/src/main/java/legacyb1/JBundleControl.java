package legacyb1;

/*
import com.ms.wfc.core.*;
import com.ms.wfc.ui.*;
*/
// import java.awt.*;

/**
 * This class is a visual component. The entry point for class execution
 * is the constructor.
 */
public class JBundleControl extends UserControl
{
/*
	private	Bundle b;
	private Vector v;
	private Hashtable as;
	
	public JBundleControl()
	{
		super();

		// Required for Visual J++ Form Designer support
		//
		initForm();

		// TODO: Add any constructor code after initForm call
		v = new Vector();
		as = new Hashtable();
		setBackColor(Color.WHITE);
	}
	
	public void setBundle(Bundle b){
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
		invalidate();
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


	private void JBundleControl_paint(Object source, PaintEvent e)
	{
		Hashtable ht = new Hashtable();
		Point dim = getSize();
		Graphics g = e.graphics;
		int w = dim.x;
		int h = dim.y;
		g.setClip(0, 0, w, h);
		int cx = w / 2;
		int cy = h / 2;
		int s = as.size();
		int r = 5;
		int l = Math.min(cx, cy) - r;
		double a = 0;
		double ainc = Math.PI * 2 / s;
		for(Enumeration e1 = as.keys(); e1.hasMoreElements();){
			Account ac = (Account)e1.nextElement();
			int px = cx - (new Double(Math.cos(a) * l)).intValue();
			int py = cy + (new Double(Math.sin(a) * l)).intValue();
			ht.put(ac, new Point(px, py));
			g.drawEllipse(px - r, py - r, 2 * r, 2 * r);
			g.drawString(ac.getName(), px + r, py);
			a += ainc;
		}
		for(Enumeration e1 = v.elements(); e1.hasMoreElements();){
			Tr t = (Tr)e1.nextElement();
			Account a1 = t.getFirstAccount();
			Point d1 = (Point)ht.get(a1);
			Account a2 = t.getSecondAccount();
			Point d2 = (Point)ht.get(a2);
			// a = Math.atan((d2.height - d1.height) / (d2.width - d1.width));
			*/
/*
			s = t.size();
			for(Enumeration e2 = t.legs(); e2.hasMoreElements();){
				Leg ll = (Leg)e2.nextElement();
			}
			*/
/*
			g.drawLine(d1.x, d1.y, d2.x, d2.y);
			g.drawString(Double.toString(t.getApproximateSettlementAmount()), (d1.x + 3 * d2.x) / 4, (d1.y + 3 * d2.y) / 4);
		}
	}


	private void JBundleControl_resize(Object source, Event e)
	{
		invalidate();
		
	}

	*/
/**
	 * NOTE: The following code is required by the Visual J++ form
	 * designer.  It can be modified using the form editor.  Do not
	 * modify it using the code editor.
	 */
/*
	Container components = new Container();

	private void initForm()
	{
		this.setSize(new Point(300, 300));
		this.setText("BundleControl");
		this.addOnResize(new EventHandler(this.JBundleControl_resize));
		this.addOnPaint(new PaintEventHandler(this.JBundleControl_paint));
	}

	public static class ClassInfo extends UserControl.ClassInfo
	{
		// TODO: Add your property and event infos here
	}
*/
}
