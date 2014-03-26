package chyatus;

/**
 *
 * @author mvas
 */
public interface Constants {
    
    // server port is for "server" functionality: waiting for system requests,
    // like requesting users list
    public static final int SERVER_PORT = 6761;
    
    // client port is for "client" functionality: requesting users list, messaging..
    public static final int CLIENT_PORT = 6762;
    
    // commands
    static String USERS_LIST = "HELLO";
}
