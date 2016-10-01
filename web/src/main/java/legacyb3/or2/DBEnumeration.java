package legacyb3.or2;

import java.sql.*;

public interface DBEnumeration{
	public abstract boolean hasMoreElements();
	public abstract Object nextElement() throws SQLException;
}
