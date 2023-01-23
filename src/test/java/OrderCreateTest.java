import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class OrderCreateTest {
    private Order order;
    private OrderActions orderActions;
    private User user;
    private UserActions userActions;
    private String token;
    private final String errorMessage = "Ingredient ids must be provided";

    @Before
    public void setUp(){
        orderActions = new OrderActions();
        userActions = new UserActions();
        User user = UserGenerator.getRandomUser();
        userActions.createUser(user);
        Response loginResponse = userActions.login(UserData.from(user));
        token = loginResponse.jsonPath().getString("accessToken");
    }

    @Test
    public void orderAuthTest(){
        order = Order.getDefaultOrder();
        Response ordResponse = orderActions.createOrder(order, token);
        assertThat(ordResponse.getStatusCode(), equalTo(SC_OK));
        assertTrue(ordResponse.jsonPath().getBoolean("success"));
        assertNotEquals(0, ordResponse.jsonPath().getInt("order.number"));
    }

    @Test
    public void orderNotAuthTest(){
        order = Order.getDefaultOrder();
        Response ordResponse = orderActions.createOrder(order, "");
        assertThat(ordResponse.getStatusCode(), equalTo(SC_OK));
        assertTrue(ordResponse.jsonPath().getBoolean("success"));
    }

    @Test
    public void nullIngredientsTest(){
        Order order = new Order(null);
        Response ordResponse = orderActions.createOrder(order, token);
        assertThat(ordResponse.getStatusCode(), equalTo(SC_BAD_REQUEST));
        assertFalse(ordResponse.jsonPath().getBoolean("success"));
    }

    @Test
    public void invalidIngredientHashTest(){
        order = Order.getIncorrectOrder();
        Response ordResponse = orderActions.createOrder(order, token);
        assertThat(ordResponse.getStatusCode(), equalTo(SC_INTERNAL_SERVER_ERROR));
    }

    @After
    public void tearDown(){
        userActions.delete(token);
    }
}
