package example.micronaut;

import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import javax.validation.Valid;

@Validated
@Controller("/user")
public class UserController {

    protected final ApplicationEventPublisher eventPublisher;
    protected final UserRepository userRepository;

    public UserController(ApplicationEventPublisher eventPublisher,
                          UserRepository userRepository) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }


    @Post("/register")
    public HttpStatus register(@Valid @Body User user) {
        userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisteredEvent(user));
        return HttpStatus.OK;
    }
}
