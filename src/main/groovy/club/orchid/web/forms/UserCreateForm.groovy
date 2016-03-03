package club.orchid.web.forms

import club.orchid.domain.auth.Role
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 5:42 PM
 */
@Canonical
class UserCreateForm {
    String firstName = ""
    String lastName = ""
    String email = "";
    String password = "";
    String passwordRepeated = "";
    List<Role> roles = null;
}
