import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;

import javax.security.auth.Subject;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SpartanTest {

    private static int idFromPostTest;
      // private static int idFromPostTest = 67 ;

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

        @DisplayName("Testing /api/spartans Endpoint")
      @Test
        public void testAllSpartans(){
         given()
                 .accept(ContentType.JSON)
         .when()
                 .get("/spartans")

         .then()
                 .statusCode(200)
                 .contentType(ContentType.JSON)
                 ;

        }
        @Order(1)  //Determines the order
        @DisplayName("Testing POST  /api/spartans   Endpoint")
        @Test
        public void testAddData(){
            Map<String, Object> spartanMap = new HashMap<>();
            spartanMap.put("name","RE-BOOTCAMP");
            spartanMap.put("gender","Male");
            spartanMap.put("phone","1234567890");

            idFromPostTest =
            given()
                    .contentType(ContentType.JSON)
                    .body(spartanMap).
                    log().all().
            when()
                    .post("/spartans").
            then()
                    .log().all()
                    .statusCode(201)
                    .contentType(ContentType.JSON)
                    .body("success", is("A Spartan is Born!"))
                    .body("data.name", is("RE-BOOTCAMP"))
                    .body("data.gender", is("Male"))
                    .body("data.phone", equalTo(1234567890))
                             .extract()
                             .body()
                             .jsonPath().getInt("data.id")

                    ;
        }
        @Order(2)
        @DisplayName("Testing GET /api/spartans/{id}  Endpoint")
        @Test

        public void testGet1Data(){
        given()
                .log().all().
                pathParam("id",idFromPostTest).
        when()
                .get("/spartans/{id}").
        then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", is(idFromPostTest))
                .body("name", is("RE-BOOTCAMP"))
                .body("gender", is("Male"))
                .body("phone", is(1234567890))
                ;
        }

        @Order(3)
        @DisplayName("Testing PUT /api/spartans/{id} Endpoint")
        @Test
        public void testUpdate1Data(){

        Map<String, Object> spartanMap = new HashMap<>();
            spartanMap.put("name", "Yucel");
            spartanMap.put("gender", "Male");
            spartanMap.put("phone", 1234567890);

            given().
                    log().all().
                    contentType(ContentType.JSON).  // tells the server the type of data
                    body(spartanMap).
            when().
                    put("/spartans/{id}",idFromPostTest)
            .then()
                    .statusCode(204);

            // now let's send another get request to make sure it actually updated
            when().
                    get("/spartan/{id}",idFromPostTest)
            .then()
                    .log().all().
                    statusCode(200)
                   .body("id",is(idFromPostTest))
                   .body("name", is(spartanMap.get("name")) )
                   .body("gender", is(spartanMap.get("gender")))
                   .body("phone", is(spartanMap.get("phone")))

                    ;
        }

           @Order(4)
           @DisplayName("Testing PATCH /api/spartans/{id} Endpoint")
           @Test
           public void testPartialUpdate1Data(){
            //just updating phone number to 2123435678
            String patchBody = "{ \"phone\": 3154964396}";

            given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(patchBody).
            when()
                    .patch("/spartans/{id}", idFromPostTest)
            .then()
                    .statusCode(204)
                    ;
            // now make sure that it actually words
               when()
                       .get("/spartans/{id}",idFromPostTest)
               .then()
                       .log().all()
                       .statusCode(200)
                       .body("phone",is(2123435678))
                       ;



    }

    public static void teardown(){
        RestAssured.reset();

}
}
