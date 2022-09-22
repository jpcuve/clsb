package legacyb1;

import java.util.Enumeration;

public interface Settlable{
	public SettlementAlgorithm getAlgorithm();
	public Settlable qualify(SettlementAlgorithm sa);
	public Enumeration movements(MovementType mt);
}