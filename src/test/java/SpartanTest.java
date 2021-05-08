import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class SpartanTest {

    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://18.204.3.34:8000";
        RestAssured.basePath = "/api";
    }

    @DisplayName("Testing:  /api/hello endpoint")
    @Test
    public void test1(){
      //Assertions.assertEquals(7,4+3);
        given()
                .accept(ContentType.TEXT)
                .when()
                .get("/hello").

                then()
                .statusCode(200)
                .body(is("Hello from Sparta") )
                .header("content-type", "text/plain;charset=UTF-8")
        ; }
      @Test

    public static void teardown(){
        RestAssured.reset();
        // Erber and Renaay
}
}
