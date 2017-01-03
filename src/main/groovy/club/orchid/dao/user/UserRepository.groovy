package club.orchid.dao.user

import club.orchid.dao.PersistentRepository
import club.orchid.domain.auth.Role
import club.orchid.domain.auth.User
import club.orchid.web.forms.UserObject

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 6:21 PM
 */
interface UserRepository extends PersistentRepository<User> {
    Optional<User> get(long id)
    Optional<User> get(Map<String, Object> params)
    Collection<User> list()
    List<Role> roles()
    User save(User user, UserObject userObject)
    Collection<Role> getUserRoles(long l)
}