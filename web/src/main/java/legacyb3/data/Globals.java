package legacyb3.data;
 
import java.sql.*;
import legacyb3.core.*;
import legacyb3.or2.*;

public final class Globals extends CachedServer{
	private Global m_g;
	
	// constructors
	
	public Globals(Connection con) throws SQLException{
		super(con, "Globals", new Global(), "Globals");
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "Current", "TimeOfDay"));
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "SCTT", "SCTT"));
		this.addColumnDouble(false, "Pay-out Multiplier", "PayOutMultiplier");
		this.addColumnDouble(false, "Pay-in Multiplier", "PayInMultiplier");
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "Opening", "Opening"));
		this.addColumnDefinition(new DBColumn(this, false, DBTimeOfDay.TIME_OF_DAY, "Closing", "Closing"));
		this.addColumnDouble(false, "Minimum Fraction Payable", "MinimumFractionPayable");
		this.addColumnDouble(false, "OPS Limit", "OPSLimit");
		this.checkStructure();
		m_g = (Global)find(new Global());
	}
	
	public void validate(DBRecord rec) throws SQLException{
		Global g = (Global)rec;
		if(g.getTimeOfDay() == null || g.getSCTT()== null || g.getOpening() == null || g.getClosing() == null) throw new SQLException(ApplicationError.isNull );
		if(g.getPayOutMultiplier() <= 0) throw new SQLException("pay-out multiplier" + ApplicationError.isNegativeOrZero );
		if(g.getPayInMultiplier() <= 0) throw new SQLException("pay-in multiplier" + ApplicationError.isNegativeOrZero );
		if(g.getOpening().isAfter(g.getClosing())) throw new SQLException("opening" + ApplicationError.isAfter + " closing");
		if(g.getMinimumFractionPayable() <= 0) throw new SQLException("minimum fraction payable" + ApplicationError.isNegativeOrZero );
		if(g.getOPSLimit() <= 0) throw new SQLException("OPS limit" + ApplicationError.isNegativeOrZero );
	}
	// accessors
	
	public Global get(){
		return m_g;
	}
	

}