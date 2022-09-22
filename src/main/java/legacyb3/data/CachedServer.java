package legacyb3.data;

import legacyb3.core.AppEvent;
import legacyb3.core.AppEventMulticaster;
import legacyb3.core.AppListener;
import legacyb3.or2.DBCachedTable;
import legacyb3.or2.DBEnumeration;
import legacyb3.or2.DBRecord;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class CachedServer extends DBCachedTable {
	
	private String items;
	private AppListener al = null;
	
	public CachedServer(Connection c, String s, DBRecord p, String items) throws SQLException {
		super(c, s, p);
		setItemsName(items);
	}
	
	public void setItemsName(String items){
		this.items = items;
	}
	
	public String getItemsName(){
		return this.items;
	}
	
	// AppListener support
	
	public void addAppListener(AppListener l){
		al = (AppListener) AppEventMulticaster.add(al, l);
	}
	
	public void removeAppListener(AppListener l){
		al = (AppListener)AppEventMulticaster.remove(al, l);
	}
	
	public void progress(String s, int p){
		if(al != null) al.appProgress(new AppEvent(this, s, p));
	}
		
	// miscellaneous

	public void load() throws SQLException {
		int s = size();
		if( s != 0){
			int i = 0;
			this.progress("Retrieving " + items, 0);
			for(DBEnumeration e1 = records(); e1.hasMoreElements();){
				e1.nextElement();
				i++;
				this.progress("Caching " + items + "...", (i * 100) / s);
			}
		}
	}

}
