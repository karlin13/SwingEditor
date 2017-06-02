package util;

import component.ComponentType;
import component.MockComponent;

public interface _Observable {
	public void notifyObserver();
	public void updateObservable(MockComponent component);
}