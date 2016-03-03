package club.orchid.dao

import club.orchid.domain.auth.Role
import club.orchid.domain.auth.User

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 6:21 PM
 */
interface UserRepository extends PersistentRepository<User> {
    Optional<User> get(long id)
    Collection<User> list()
    long save(User user)
    Collection<Role> getUserRoles(long l)
}