package legacyb1;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 19, 2008
 * Time: 11:12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBCachedTable extends DBTable {
    private double size;

    public DBCachedTable(Connection connection, String name, DBRecord template) {
        super(connection, name, template);
    }

    public DBCachedTable(Connection connection, String name, DBRecord template, double size) {
        super(connection, name, template);
        this.size = size;
    }

    public void save(){

    }

    public void load(){

    }

    public void fill(){

    }
}
