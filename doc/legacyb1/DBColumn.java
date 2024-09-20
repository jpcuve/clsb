package legacyb1;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Sep 19, 2008
 * Time: 11:18:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBColumn {
    private final boolean key;
    private final String name;

    public DBColumn(boolean key, String name) {
        this.key = key;
        this.name = name;
    }

    public void setConstraintEqual(String id) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
