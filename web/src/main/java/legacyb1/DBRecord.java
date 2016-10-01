package legacyb1;

import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 19, 2008
 * Time: 10:46:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBRecord extends Observable {
    private DBTable table;

    public DBTable getTable() {
        return table;
    }

    public void setTable(DBTable table) {
        this.table = table;
    }

    public void setQueried(){

    }

    public void save(){
        
    }
}
