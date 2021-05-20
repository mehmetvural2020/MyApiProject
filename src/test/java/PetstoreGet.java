import com.aventstack.extentreports.gherkin.model.Given;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class PetstoreGet {
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    public void getFindByStatus(){
        given().
                log().all().
                queryParam("status", "available,sold").
        when().
                get("/pet/findByStatus").
        then().
                log().all().
                statusCode(200);
    }

    @Test
    public void getFindByPetId(){
        Response response = given().
                pathParam("id", 501871).
            when().
                get("/pet/{id}");


            assertEquals(response.statusCode(),200);
            assertEquals(response.contentType(), "application/json");

        JsonPath jsonPath = response.jsonPath();
        jsonPath.prettyPrint();

        // get id, name, status : keys in the top level object

        // get the base, date: keys in the top level object
        String base = jsonPath.getString("base");
        System.out.println("base = " + base);

//        given().
//                pathParam("id", 501871).
//        when().
//                get("/pet/{id}").
//        then().
//                log().body().
//                statusCode(200);

    }

    @Test
    public void postPet(){
        Response response = given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .body("{\n" +
                        "            \"id\": 108,\n" +
                        "                \"category\": {\n" +
                        "            \"id\": 200,\n" +
                        "                    \"name\": \"Dog\"\n" +
                        "        },\n" +
                        "            \"name\": \"Max\",\n" +
                        "                \"photoUrls\": [\n" +
                        "            \"string\"\n" +
                        "  ],\n" +
                        "            \"tags\": [\n" +
                        "            {\n" +
                        "                \"id\": 0,\n" +
                        "                    \"name\": \"string\"\n" +
                        "            }\n" +
                        "  ],\n" +
                        "            \"status\": \"available\"\n" +
                        "        }")
                .when().post("/pet/");
        assertEquals(response.statusCode(), 200);
        assertEquals(response.contentType(), "application/json");
        JsonPath json = response.jsonPath();

        assertEquals(json.getString("pet.name"), "Dog");
        assertEquals(json.getInt("pet.id"), 102);
        assertEquals(json.getString("pet.status"), "available");

    }
}
