package club.orchid.web.forms

import club.orchid.anno.mapping.Primitive
import club.orchid.domain.auth.Role
import club.orchid.domain.auth.User
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 5:42 PM
 */
@Canonical
class UserObject {
    long userId
    String firstName = ""
    String lastName = ""
    String email = "";
    String password = "";
    String passwordRepeated = "";

    boolean accountNonExpired
    boolean accountNonLocked
    boolean credentialsNonExpired
    boolean enabled

    String[] ownRoles;
    Map<Role, Boolean> allRoles = [:];

    UserObject() {
    }

    UserObject(User user) {
        this.userId = user.id
        this.firstName = user.firstName
        this.lastName = user.lastName
        this.email = user.email

        accountNonExpired = user.accountNonExpired
        accountNonLocked = user.accountNonLocked
        credentialsNonExpired = user.credentialsNonExpired
        enabled = user.enabled

        this.ownRoles = user.roles?.split(/\s?,\s?/)
    }

    void initRoles(final List<Role> roles) {
        roles.each { Role r ->
            allRoles.put(r, this.ownRoles?.contains(r.roleName))
        }
    }
}