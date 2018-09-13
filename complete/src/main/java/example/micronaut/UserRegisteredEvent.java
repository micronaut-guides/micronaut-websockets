package example.micronaut;

import io.micronaut.context.event.ApplicationEvent;

public class UserRegisteredEvent extends ApplicationEvent {
    /**
     * Constructs a UserRegistered Event
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public UserRegisteredEvent(Object source) {
        super(source);
    }
}
