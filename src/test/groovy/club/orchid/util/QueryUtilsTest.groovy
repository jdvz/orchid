package club.orchid.util

import club.orchid.domain.auth.User
import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.Page
import jdk.internal.util.xml.impl.Pair
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.util.CollectionUtils

import java.util.function.Supplier

import static org.junit.Assert.*
/**
 * Created with IntelliJ IDEA.
 *
 * @author: zera
 * @date: 3/2/16 10:51 AM
 */
public class QueryUtilsTest {
    @Value('${path.images:resources/images}')
    String pathImages

    @Autowired
    PasswordEncoder passwortEncoder

    public static final String VASIA = 'Vasia'

    @Test
    public void createInsertString() throws Exception {
        def query = QueryUtils.createInsertString(User.class)
        println "'$query'"
        assertTrue("Initial query insert", ("insert into users(first_name,last_name,password,email,account_non_expired,account_non_locked,credentials_non_expired,enabled,id,version)\n" +
                "    values (:first_name,:last_name,:password,:email,:account_non_expired,:account_non_locked,:credentials_non_expired,:enabled,:id,:version)").equals(query))
    }

    @Test
    public void createPersistParams() throws Exception {
        User user = new User(id: 1, firstName: VASIA)
        def query = QueryUtils.createPersistParams(user)
        assertEquals("query for $VASIA", query.first_name, VASIA)
    }

    @Test
    public void createEncoder() throws Exception {
        assertNull('Empty password encoder', passwortEncoder)
    }

    @Test
    public void createPassword() throws Exception {
        def encoder = new BCryptPasswordEncoder()
        def pwd = encoder.encode('123')
        println "encoded password $pwd"
        assertTrue('Test password', encoder.matches('123', pwd))
    }

    @Test
    public void assignable() throws Exception {
        println CmsPage.class.isAssignableFrom(Page.class)
        println Page.class.isAssignableFrom(CmsPage.class)
        println CmsPage.class.isAssignableFrom(CmsPage.class)
    }

    @Test
    public void testStreamMapp() throws Exception {
        List<Map.Entry<String, String>> pairs = [new MapEntry('test-1', 'test-1-1'), new MapEntry('test-1', 'test-1-2'), new MapEntry('test-2', 'test-2-2')]
//        pairs.stream().
    }

    @Test
    void testFileAttributes() throws Exception {
        println "$pathImages/image.realDir/image.realName"
    }
}