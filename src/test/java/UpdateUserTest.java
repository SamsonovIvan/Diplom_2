import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateUserTest {
    private UserActions userActions;
    private User user;
    private String token;
    private final String notAuthErrorMesage = "You should be authorised";
    private final String emailExistsError = "User with such email already exists";

    @Before
    public void setup(){
        user = UserGenerator.getRandomUser();
        userActions = new UserActions();
        userActions.createUser(user);
        Response loginResponse = userActions.login(UserData.from(user));
        token = loginResponse.body().jsonPath().getString("accessToken");
    }

    @Test
    public void changeUserWithAuth(){
        User updatedUser = UserGenerator.getRandomUser();
        Response updateUserResponse = userActions.updateUser(updatedUser, token);
        assertThat(updateUserResponse.getStatusCode(), equalTo(SC_OK));
        assertTrue(updateUserResponse.jsonPath().getBoolean("success"));
        assertEquals(updatedUser.getEmail().toLowerCase(), updateUserResponse.jsonPath().getString("user.email"));
        assertEquals(updatedUser.getName(), updateUserResponse.jsonPath().getString("user.name"));
    }

    @Test
    public void changeUserNotAuth(){
        Response updateResponse = userActions.updateUser(UserGenerator.getRandomUser(), "");
        assertThat(updateResponse.getStatusCode(), equalTo(SC_UNAUTHORIZED));
        assertEquals(notAuthErrorMesage, updateResponse.jsonPath().getString("message"));
    }

    @Test
    public void alreadyUsedEmailTest(){
        User newUser = UserGenerator.getRandomUser();
        userActions.createUser(newUser);
        Response newResponse = userActions.login(UserData.from(newUser));
        User updateUser = new User(newResponse.body().jsonPath().getString("user.email"), user.getPassword(), user.getName());
        Response updUsrResponse = userActions.updateUser(updateUser, token);
        assertThat(updUsrResponse.getStatusCode(), equalTo(SC_FORBIDDEN));
        assertEquals(emailExistsError, updUsrResponse.jsonPath().getString("message"));
    }

    @Test
    public void changeEmailTest(){
        User updEmailUsr = new User(UserGenerator.getRandomUser().getEmail(), user.getPassword(), user.getName());
        Response updateEmailResponse = userActions.updateUser(updEmailUsr, token);
        assertThat(updateEmailResponse.getStatusCode(), equalTo(SC_OK));
        assertTrue(updateEmailResponse.jsonPath().getBoolean("success"));
        assertEquals(updEmailUsr.getEmail().toLowerCase(), updateEmailResponse.jsonPath().getString("user.email"));
    }

    @Test
    public void changePasswordTest(){
        User updPswrdUsr = new User(user.getEmail(), UserGenerator.getRandomUser().getPassword(), user.getName());
        Response updatePswrdResponse = userActions.updateUser(updPswrdUsr, token);
        assertThat(updatePswrdResponse.getStatusCode(), equalTo(SC_OK));
        assertTrue(updatePswrdResponse.jsonPath().getBoolean("success"));
    }

    @After
    public void tearDown(){
        userActions.delete(token);
    }
}
