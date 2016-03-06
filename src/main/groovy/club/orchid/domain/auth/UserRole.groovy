package club.orchid.domain.auth

import club.orchid.anno.mapping.Id
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 7:51 PM
 */
@Canonical
class UserRole {
    @Id('user_id')
    long userId
    @Id('role_id')
    long roleId
}
