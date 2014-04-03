package chyatus;

/**
 * Constants
 *
 * @author M.Vasileusky
 */
public interface Constants {
    
    // server port is for "server" functionality: waiting for system requests,
    // like requesting users list
    public int SYSTEM_PORT = 6761;
    
    // client port is for "client" functionality: requesting users list, messaging..
    public int USER_PORT = 6762;
    
    public String SEPARATOR = "_";
    
    // commands
    public byte USERS_LIST_COMMAND = 1;
    public byte BYE_COMMAND = 2;
}