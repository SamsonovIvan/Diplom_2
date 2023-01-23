import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class CreateUserWithoutAnyFieldTest {
    private UserActions userActions;
    private Response nullFieldResponse;
    private static String errorMessage = "Email, password and name are required fields";

    @DataProvider
    public static Object[][] getData(){
        return new Object[][]{
                {"", "4815162342", "Hurley"},
                {"praktikum@yandex.ru", "", "Ivan"},
                {"fibonacci@yandex.ru", "1123581321", ""},
        };
    }
    @Before
    public void setUp(){
        userActions = new UserActions();
    }

    @Test
    @UseDataProvider("getData")
    public void createWithEmptyFieldTest(String email, String password, String name){
        User user = UserGenerator.getDefault(email, password, name);
        nullFieldResponse = userActions.createUser(user);
        assertThat(nullFieldResponse.getStatusCode(), equalTo(SC_FORBIDDEN));
        assertEquals(errorMessage, nullFieldResponse.body().jsonPath().getString("message"));

    }
    @After
    public void tearDown(){
        String token = nullFieldResponse.body().jsonPath().getString("accessToken");
        if (token != null){
            userActions.delete(token);
        }
    }

}
