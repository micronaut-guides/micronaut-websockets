package example.micronaut;

public interface UserRepository {

    User save(User user);
    Number count();
}
