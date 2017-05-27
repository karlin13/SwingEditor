package util;

import component.Component;
import component.ComponentType;

public interface _Observable {
	public void notifyObserver();
	public void updateObservable(Component component);
}
