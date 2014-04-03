package chyatus.mvc.model;

import java.util.Observable;

/**
 * Abstract observable model for GUI MVC
 *
 * @author mvas
 */
public abstract class Model extends Observable {

    protected void notifyListeners(int mode) {
        setChanged();
        notifyObservers(mode);
    }

}
