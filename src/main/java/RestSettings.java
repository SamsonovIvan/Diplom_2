import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
public class RestSettings {

    public static final String URL = "https://stellarburgers.nomoreparties.site/api/";
    public static RequestSpecification getBaseSpecSettings(){
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(URL)
                .build();
    }
}
