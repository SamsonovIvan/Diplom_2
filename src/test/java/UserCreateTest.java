import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserCreateTest {
    private UserActions userActions;
    private User user;
    private String token;
    private final String duplicateUserMessage = "User already exists";

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        userActions = new UserActions();
    }

    @Test
    public void createUserTest(){
        Response regResponse = userActions.createUser(user);
        assertThat(regResponse.getStatusCode(), equalTo(SC_OK));
        assertTrue(regResponse.jsonPath().getBoolean("success"));
        token = regResponse.body().jsonPath().getString("accessToken");
        assertNotNull(token);
    }

    @Test
    public void createExistUserTest(){
        userActions.createUser(user);
        Response regTwice = userActions.createUser(user);
        token = regTwice.body().jsonPath().getString("accessToken");
        assertThat(regTwice.getStatusCode(), equalTo(SC_FORBIDDEN));
        assertThat(regTwice.jsonPath().getString("message"), equalTo(duplicateUserMessage));
    }

    @After
    public void tearDown(){
        userActions.delete(token);
    }
}
