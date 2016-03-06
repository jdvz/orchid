package club.orchid.service

import club.orchid.dao.UserRepository
import club.orchid.domain.auth.Role
import club.orchid.domain.auth.User
import club.orchid.util.AuthenticationUtils
import club.orchid.web.forms.UserObject
import org.apache.log4j.Logger
import org.apache.tomcat.util.net.jsse.openssl.Authentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.session.SessionInformation
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

import java.util.function.Supplier

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 4:34 PM
 */
@Service('userService')
class UserService implements IUserService {
    private static final Logger log = Logger.getLogger(UserService.class.name)

    @Autowired
    @Qualifier('userRepository')
    private UserRepository userRepository

    @Autowired
    private PasswordEncoder passwordEncoder

    @Autowired
    private SessionRegistry sessionRegistry

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
    User save(UserObject form) {
        boolean updatePassword = !StringUtils.isEmpty(form.password)
        User user = form.userId > 0 ? userRepository.get(form.userId).orElseThrow({
            throw new IllegalArgumentException("User not found")
        }) : new User()

        if (updatePassword) {
            form.password = this.passwordEncoder.encode(form.password)
        }
        user = userRepository.save(user, form)
        if (user.clearAuthorization) {
            // it doesn't work
            List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
            for (Object principal : loggedUsers) {
                if(principal instanceof User) {
                    if(principal.email?.equals(user.email)) {
                        List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                        if(null != sessionsInfo && sessionsInfo.size() > 0) {
                            for (SessionInformation sessionInformation : sessionsInfo) {
                                log.info("Exprire now :" + sessionInformation.getSessionId());
                                sessionInformation.expireNow();
                                sessionRegistry.removeSessionInformation(sessionInformation.getSessionId());
                                // User is not forced to re-logging
                            }
                        }
                    }
                }
            }
        }
        return user
    }

    @Override
    List<Role> roles() {
        return userRepository.roles()
    }

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username) {
            Optional<User> user = userRepository.get([email: username, enabled: true])
            if (user.isPresent()) {
                user.get().setAuthorities(userRepository.getUserRoles(user.get().id))
            }
            return user.orElse(AuthenticationUtils.anonymous)
        } else {
            return AuthenticationUtils.anonymous
        }
    }
}
