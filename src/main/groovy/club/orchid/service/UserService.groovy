package club.orchid.service

import club.orchid.dao.UserRepository
import club.orchid.domain.auth.User
import club.orchid.util.AuthenticationUtils
import club.orchid.web.forms.UserCreateForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 4:34 PM
 */
@Service('userService')
class UserService implements IUserService {

    @Autowired
    @Qualifier('userRepository')
    private UserRepository userRepository

    @Override
    Optional<User> getUserById(long id) {
        return userRepository.get(id)
    }

    @Override
    Collection<User> getAllUsers() {
        return userRepository.list()
    }

    @Override
    @Transactional
    User create(UserCreateForm form) {
        User user = new User()
        user.email = form.email
        user.firstName = form.firstName
        user.lastName = form.lastName
        user.password = form.password

        userRepository.save(user)
        userRepository.createRole()
        return null
    }

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username) {
            Optional<User> user = userRepository.get([email: username, enabled: true], User.class)
            if (user.isPresent()) {
                user.get().setAuthorities(userRepository.getUserRoles(user.get().id))
            }
            return user.orElse(AuthenticationUtils.anonymous)
        } else {
            return AuthenticationUtils.anonymous
        }
    }
}
