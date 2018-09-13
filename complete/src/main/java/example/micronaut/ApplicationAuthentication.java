package example.micronaut;

import io.micronaut.security.authentication.Authentication;

import java.util.Map;

public class ApplicationAuthentication implements Authentication  {
    private String username;
    private Map<String, Object> attributes;

    public ApplicationAuthentication(String username, Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return username;
    }
}
