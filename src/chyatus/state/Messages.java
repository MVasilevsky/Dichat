package chyatus.state;

import chyatus.User;
import chyatus.commands.Message;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Messages container.
 *
 * @author mvas
 */
public class Messages extends ApplicationState {

    private final Set<Message> allMessages = new HashSet<>();

    public synchronized Set<Message> getAll() {
        return Collections.unmodifiableSet(allMessages);
    }

    public synchronized boolean addMessage(Message message) {
        boolean added = allMessages.add(message);
        if (added) {
            notifyListeners(MESSAGES_UPDATED);
        }
        return added;
    }

    public synchronized boolean removeMessage(Message message) {
        boolean removed = allMessages.remove(message);
        if (removed) {
            notifyListeners(MESSAGES_UPDATED);
        }
        return removed;
    }

    public synchronized boolean addAll(Collection<Message> messages) {
        return allMessages.addAll(messages);
    }

    public synchronized Set<Message> findByAuthor(User user) {
        Set<Message> messages = new HashSet<>();
        for (Message message : allMessages) {
            if (message.getSender().equals(user)) {
                messages.add(message);
            }
        }
        return messages;
    }

}
