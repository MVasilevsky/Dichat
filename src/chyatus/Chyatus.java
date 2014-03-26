package chyatus;

import chyatus.users.Users;
import chyatus.users.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Distributed chyat
 *
 * @author M.Vasileusky
 */
public class Chyatus {

    private static Client client;
    private static Server server;

    public static void main(String[] args) throws Exception {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Your username: ");
            String username = reader.readLine();
            client = new Client(username);
            Users.addUser(new User(username, InetAddress.getLocalHost()));
        } catch (IOException e) {
            System.err.println("Error while user creation: " + e.getLocalizedMessage());
            return;
        }

        client.getUsersList();

        System.out.println("users: ");
        for (User user : Users.getAll()) {
            System.out.println(user.getUsername());
        }

        // remove if
        if (Users.getAll().size() == 1) {
            server = new Server();

            try {
                server.start();
            } catch (IOException ex) {
                System.err.println("Can't start server");
                return;
            }
        }

    }

}
