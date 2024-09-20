package legacyb3.util;

import java.util.EventListener;

public class EventMulticaster implements EventListener {
    protected final EventListener a, b;

    protected EventMulticaster(EventListener a, EventListener b) {
		this.a = a;
		this.b = b;
    }

	public static EventListener add(EventListener a, EventListener b){
		return EventMulticaster.addInternal(a, b);
	}

	public static EventListener remove(EventListener a, EventListener b){
		return EventMulticaster.removeInternal(a, b);
	}

    protected static EventListener addInternal(EventListener a, EventListener b) {
		if (a == null)  return b;
		if (b == null)  return a;
		return new EventMulticaster(a, b);
    }

    protected static EventListener removeInternal(EventListener l, EventListener oldl) {
		if (l == oldl || l == null) {
			return null;
		} else if (l instanceof EventMulticaster) {
			return ((EventMulticaster)l).remove(oldl);
		} else {
	    return l;
		}
    }

    protected EventListener remove(EventListener oldl) {
		if (oldl == a)  return b;
		if (oldl == b)  return a;
		EventListener a2 = this.removeInternal(a, oldl);
		EventListener b2 = this.removeInternal(b, oldl);
		if (a2 == a && b2 == b) {
			return this;
		}
		return EventMulticaster.addInternal(a2, b2);
    }

}
