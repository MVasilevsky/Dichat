package chyatus;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 *
 * @author mvas
 */
public class Utils {

    public static InetAddress getBroadcastAddress() throws IOException {
        InetAddress localHost = Inet4Address.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
        return networkInterface.getInterfaceAddresses().get(0).getBroadcast();
    }
}
