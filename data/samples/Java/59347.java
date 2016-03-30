/** The benchmark is distributed under the Creative Commons,
* Attribution-NonCommercial-NoDerivatives. This license includes the benchmark database
* and its derivatives. For attribution, please cite this page, and our publications below.
* This data is provided free of charge for non-commercial and academic benchmarking and
* experimentation use. If you would like to contribute to the benchmark, please contact us.
* If you believe you intended usage may be restricted by the license,
* please contact us and we can discuss the possibilities.
*/

import models.Role;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

public class UserTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

    @Test
    public void createAndRetrieveUser() {
        new User("bob", "bob@gmail.com", "secret", "Bob", new Role(), "test user").save();
        User bob = User.find("byEmail", "bob@gmail.com").first();
        assertNotNull(bob);
        assertEquals("Bob", bob.fullname);
    }

    @Test
    public void testUsernameAndPass() {
        new User("bob", "bob@gmail.com", "secret", "Bob", new Role(), "test user").save();
        assertNotNull(User.findByUsernamePass("bob", "secret"));
        assertNull(User.findByUsernamePass("bob", "badpassword"));
        assertNull(User.findByUsernamePass("tom", "secret"));
    }
}
