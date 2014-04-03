package chyatus.state;

import java.util.Observable;

/**
 *
 * @author mvas
 */
public class ApplicationState extends Observable {

    public static final int USERS_UPDATED = 0;
    public static final int MESSAGES_UPDATED = 1;

    protected void notifyListeners(int mode) {
        setChanged();
        notifyObservers(mode);
    }

}
