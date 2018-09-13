package example.micronaut;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        if(!users.containsKey(user.getUsername())) {
            users.put(user.getUsername(), user);
        }
        return users.get(user.getUsername());
    }

    @Override
    public Number count() {
        return users.keySet().size();
    }
}
