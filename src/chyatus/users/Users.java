package chyatus.users;

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
public final class Users {

    private static final Set<User> allUsers = new HashSet<>();

    public static synchronized Set<User> getAll() {
        return Collections.unmodifiableSet(allUsers);
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

    public static synchronized User findByAddress(InetAddress address) {
        for (User user : allUsers) {
            if (address.equals(user.getIp())) {
                return user;
            }
        }
        return null;
    }
}
