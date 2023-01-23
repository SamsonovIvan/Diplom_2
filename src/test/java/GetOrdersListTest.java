import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class GetOrdersListTest {
    private User user;
    private UserActions userActions;
    private Order order;
    private OrderActions orderActions;
    private String token;
    private final String errorMessage = "You should be authorised";

    @Before
    public void setUp(){
        orderActions = new OrderActions();
        userActions = new UserActions();
        User user = UserGenerator.getRandomUser();
        userActions.createUser(user);
        Response loginResponse = userActions.login(UserData.from(user));
        token = loginResponse.body().jsonPath().getString("accessToken");
        orderActions = new OrderActions();
        orderActions.createOrder(Order.getDefaultOrder(), token);
    }

    @Test
    public void orderListAuthTest(){
        Response listResponse = orderActions.getListOfOrders(token);
        assertThat(listResponse.getStatusCode(), equalTo(SC_OK));
        assertTrue(listResponse.jsonPath().getBoolean("success"));
        assertFalse(listResponse.jsonPath().getList("orders").isEmpty());
    }

    @Test
    public void orderListNotAuthTest(){
        Response listResponse = orderActions.getListOfOrders("");
        assertThat(listResponse.getStatusCode(), equalTo(SC_UNAUTHORIZED));
        assertFalse(listResponse.jsonPath().getBoolean("success"));
        assertEquals(errorMessage, listResponse.jsonPath().getString("message"));
    }

    @After
    public void tearDown(){
        userActions.delete(token);
    }
}
