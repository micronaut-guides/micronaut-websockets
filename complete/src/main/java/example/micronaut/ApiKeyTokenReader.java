package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.token.reader.TokenReader;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ApiKeyTokenReader implements TokenReader {

    @Override
    public Optional<String> findToken(HttpRequest<?> request) {
        return request.getParameters().get("apiKey", String.class);
    }
}
