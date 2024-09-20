package legacyb3.or2;

import java.sql.SQLException;

public interface DBEnumeration{
	public abstract boolean hasMoreElements();
	public abstract Object nextElement() throws SQLException;
}
