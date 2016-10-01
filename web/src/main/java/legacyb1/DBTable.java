package legacyb1;

import java.util.Observable;
import java.util.Enumeration;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 19, 2008
 * Time: 11:11:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBTable extends Observable {
    private final Connection connection;
    private final String name;
    private DBRecord pattern;

    public DBTable(Connection connection, String name) {
        this.connection = connection;
        this.name = name;
    }

    public DBTable(Connection connection, String name, DBRecord pattern) {
        this.connection = connection;
        this.name = name;
        this.pattern = pattern;
    }

    public DBRecord getPattern() {
        return pattern;
    }

    public void setPattern(DBRecord pattern) {
        this.pattern = pattern;
    }

    void addColumnDefinition(Object o){

    }

    public DBRecord find(DBRecord rec){
        return null;
    }

    public void clearConstraints(){

    }

    public Enumeration records(){
        return null;
    }

    public DBColumn getColumnDefinition(final String columnName){
        return null;
    }

    public void save(DBRecord rec){

    }

    public void insert(DBRecord rec) throws DBIOException, SQLException {

    }

    public boolean query(DBRecord rec) throws DBIOException, SQLException {
        return false;
    }

    public void update(DBRecord rec) throws DBIOException, SQLException {
    }

    public void clear() throws DBIOException, SQLException{

    }

    public int size() throws SQLException {
        return 0;
    }
}
