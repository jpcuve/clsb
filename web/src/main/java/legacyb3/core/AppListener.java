package legacyb3.core;

import java.util.*;

public interface AppListener extends EventListener{ 
	public abstract void appMessage(AppEvent e);
	public abstract void appProgress(AppEvent e);
	public abstract void appTime(AppEvent e);
}
