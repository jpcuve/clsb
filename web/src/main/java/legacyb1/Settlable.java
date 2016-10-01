package legacyb1;

import java.util.*;

public interface Settlable{
	public SettlementAlgorithm getAlgorithm();
	public Settlable qualify(SettlementAlgorithm sa);
	public Enumeration movements(MovementType mt);
}