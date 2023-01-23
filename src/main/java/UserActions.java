import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class UserActions extends RestSettings {
    private static final String LOGIN = "auth/login/";
    private static final String REGISTER = "auth/register/";
    private static final String DEL_OR_UPD = "auth/user/";

    public Response createUser(User user){
        return (Response) given()
                .spec(getBaseSpecSettings())
                .body(user)
                .when()
                .post(REGISTER)
                .then()
                .extract();
    }

    public static Response login(UserData data){
        return (Response) given()
                .spec(getBaseSpecSettings())
                .body(data)
                .when()
                .post(LOGIN)
                .then()
                .extract();
    }

    public Response updateUser(User user, String token){
        return (Response) given()
                .spec(getBaseSpecSettings())
                .header("authorization", token)
                .body(user)
                .when()
                .patch(DEL_OR_UPD)
                .then()
                .extract();
    }

    public void delete(String token) {
        if (token == null) {
            return;
        }
        given()
                .spec(getBaseSpecSettings())
                .header("authorization", token)
                .when()
                .delete(DEL_OR_UPD)
                .then()
                .assertThat()
                .statusCode(202)
                .extract()
                .path("ok");
    }

}
