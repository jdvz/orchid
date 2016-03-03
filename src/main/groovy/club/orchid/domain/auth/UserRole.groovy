package club.orchid.domain.auth

import club.orchid.anno.mapping.Id

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 7:51 PM
 */
class UserRole {
    @Id
    User user
    @Id
    Role role
}
