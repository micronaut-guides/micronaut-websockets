package example.micronaut;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.validator.TokenValidator;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.util.Collections;

@Singleton
public class ApiKeyTokenValidator implements TokenValidator {

    private final ApplicationConfiguration applicationConfiguration;

    public ApiKeyTokenValidator(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }
    @Override
    public Publisher<Authentication> validateToken(String token) {
        if (applicationConfiguration.getApikey().equalsIgnoreCase(token)) {
            return Flowable.just(new ApplicationAuthentication(applicationConfiguration.getUsername(), Collections.emptyMap()));
        }
        return Flowable.empty();
    }
}