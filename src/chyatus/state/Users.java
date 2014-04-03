package chyatus.state;

import chyatus.User;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Users container
 *
 * @author mvas
 */
public final class Users extends ApplicationState {

    private User myself;
    private final Set<User> allUsers = new HashSet<>();

    //
    public User getMyself() {
        return myself;
    }

    /**
     * Sets current user and add it to all users list
     * 
     * @param myself current user
     */
    public void setMyself(User myself) {
        this.myself = myself;
        addUser(myself);
    }

    //
    public synchronized Set<User> getAll() {
        return Collections.unmodifiableSet(allUsers);
    }

    public synchronized boolean addUser(User user) {
        boolean added = allUsers.add(user);
        if (added) {
            notifyListeners(USERS_UPDATED);
        }
        return added;
    }

    public synchronized boolean removeUser(User user) {
        boolean removed = allUsers.remove(user);
        if (removed) {
            notifyListeners(USERS_UPDATED);
        }
        return removed;
    }

    public synchronized boolean addAll(Collection<User> users) {
        boolean added = allUsers.addAll(users);
        if (added) {
            notifyListeners(USERS_UPDATED);
        }
        return added;
    }

    public synchronized User findByAddress(InetAddress address) {
        for (User user : allUsers) {
            if (address.equals(user.getIp())) {
                return user;
            }
        }
        return null;
    }

}
