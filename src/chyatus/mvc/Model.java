package chyatus.mvc;

import chyatus.users.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author mvas
 */
public class Model extends Observable {

    private List<User> users = new ArrayList<>();
    
    public void addUser(User u) {
        users.add(u);
        setChanged();
        notifyObservers();
    }

    public User getUser() {
        return users.get(0);
    }
}
