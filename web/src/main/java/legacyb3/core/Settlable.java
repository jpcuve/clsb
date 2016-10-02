package legacyb3.core;

import legacyb3.or2.TimeOfDay;
import legacyb3.util.*;
import java.sql.*;
import java.util.Enumeration;

public interface Settlable{
	public SettlementAlgorithm getSettlementAlgorithm(boolean failureManagement);
	public Settlable qualify(SettlementAlgorithm sa, TimeOfDay tod) throws SQLException;
	public Enumeration prMovements(boolean direction);
}