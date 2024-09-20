package legacyb1;

import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 19, 2008
 * Time: 11:28:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBRecordIterator implements Enumeration {
    private DBTable table;

    public DBRecordIterator(DBTable table) {
        this.table = table;
    }

    public boolean hasMoreElements() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object nextElement() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
