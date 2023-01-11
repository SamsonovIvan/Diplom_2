import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderActions extends RestSettings {
    private final String ORDERS = "orders";

    public Response createOrder(Order order, String token){
        return (Response) given()
                .spec(getBaseSpecSettings())
                .headers("authorization", token)
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .extract();
    }

    public Response getListOfOrders(String token){
        return (Response) given()
                .spec(getBaseSpecSettings())
                .header("authorization", token)
                .when()
                .get(ORDERS)
                .then()
                .extract();
    }
}
