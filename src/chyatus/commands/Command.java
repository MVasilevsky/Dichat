package chyatus.commands;

import java.io.Serializable;

/**
 * Chat command
 *
 * @author mvas
 */
public class Command implements Serializable {

    public static final int TYPE_SINGLE_MESSAGE = 1;
    public static final int TYPE_MULTI_MESSAGE = 2;

    public int type;
    public String message;

    public Command(int type, String message) {
        this.type = type;
        this.message = message;
    }

}
