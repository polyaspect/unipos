package unipos.auth.components.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created by Dominik on 27.05.2015.
 */
public interface UserService {
    void createUser(User user);
    void deleteUser(User user);
    List<User> findAllUsers();

    User findUserByUsername(String username);

    void deleteAllUsers();

    User findUserById(String id);

    void deleteUser(String id);

    void deleteUserByUsername(String username);

    void deleteByGuid(String guid);

    void updateUser(User user);

    User findUserByGuid(String guid);

    User findUserByUserId(Long userId);
}
