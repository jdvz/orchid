package club.orchid.service

import club.orchid.domain.auth.Role
import club.orchid.domain.auth.User
import club.orchid.web.forms.UserObject
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 5:42 PM
 */
interface IUserService extends UserDetailsService {
    Optional<User> getUserById(long id);
    Collection<User> getAllUsers();
    User save(UserObject form);
    List<Role> roles()
}