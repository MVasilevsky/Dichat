package chyatus.users;

import chyatus.users.User;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Users container
 *
 * @author mvas
 */
public final class Users {

    private static Set<User> allUsers = new HashSet<>();

    public static synchronized Set<User> getAll() {
        return new HashSet<>(allUsers);
    }

    public static synchronized boolean addUser(User user) {
        return allUsers.add(user);
    }

    public static synchronized boolean removeUser(User user) {
        return allUsers.remove(user);
    }

    public static synchronized boolean addAll(Collection<User> users) {
        return allUsers.addAll(users);
    }
}
