import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserLoginTest {
    private User user;
    private UserActions userActions;
    private String token;
    private final String errorMessage = "email or password are incorrect";

    @Before
    public void setUp(){
        user = UserGenerator.getRandomUser();
        userActions = new UserActions();
        userActions.createUser(user);
    }

    @Test
    public void successloginTest(){
        Response loginResponse = userActions.login(UserData.from(user));
        assertThat(loginResponse.getStatusCode(), equalTo(SC_OK));
        assertTrue(loginResponse.body().jsonPath().getBoolean("success"));
        token = loginResponse.body().jsonPath().getString("accessToken");
        assertNotNull(token);
        String refreshToken = loginResponse.body().jsonPath().getString("accessToken");
        assertNotNull(refreshToken);
    }

    @Test
    public void invalidDataLoginTest(){
        Response loginResponse = userActions.login(UserGenerator.getRandomUserData());
        assertThat(loginResponse.getStatusCode(), equalTo(SC_UNAUTHORIZED));
        assertThat(loginResponse.body().jsonPath().getString("message"), equalTo(errorMessage));
    }

    @After
    public void tearDown(){
        userActions.delete(token);
    }
}
