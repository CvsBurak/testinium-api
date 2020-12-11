package BasePackage;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

public class BaseClass {
	
	//VARIABLES
    private static final String API_KEY = "d64974ff";
    private static String imdbID;
    private String baseUrl = "http://www.omdbapi.com/";
    private String movieType = "movie";

    //PARAMETERS - BY SEARCH
    private String bySearchMovieTitleParamName = "s";
    private String bySearchPageNumberParamName = "page";

    //PARAMETERS - BY ID OR TITLE
    private String byIDOrTitleImdbIDParamName = "i";
    private String byIDOrTitleMovieTitleParamName = "t";
    private String byIDOrTitlePlotOptionParamName = "plot";

    //COMMON PARAMETERS - BY SEARCH & BY ID OR TITLE
    private String apikeyParamName = "apikey";
    private String resultTypeParamName = "type";
    private String releaseYearParamName = "y";
    private String dataTypeParamName = "r";
    private String jsonCallbackParamName = "callback";
    private String apiVersionParamName = "v";

    //BEFORE AND AFTER METHODS
    @BeforeClass
    public void configure() {
        RestAssured.baseURI = baseUrl;
    }

    @AfterClass
    public void close() {
        RestAssured.reset();
    }

    //REUSABLE METHODS
    public String Trim(ArrayList<String> a) {
        String s = a.get(0);
        return s;
    }

    public void getImdbIDByFilmName(String searchName, String exactTitleName) {
        ArrayList<String> getImdbID = given()
                .queryParam(apikeyParamName, API_KEY)
                .queryParam(bySearchMovieTitleParamName, searchName)
                .when()
                .get()
                .then()
                .extract()
                .path("Search.findAll{it.Title=='" + exactTitleName + "' && it.Type=='" + movieType + "'}.imdbID");
        imdbID = Trim(getImdbID);
        System.out.println(imdbID);
    }

    public void searchFilmByImdbID() {
        given()
                .queryParam(apikeyParamName, API_KEY)
                .queryParam(byIDOrTitleImdbIDParamName, imdbID)
                .queryParam(resultTypeParamName, movieType)
                .when()
                .get()
                .then()
                .log().body();
    }

    public void verifyExistenceOfTheAreas(String areaNameToBeVerified) {
        String Resp = given()
                .queryParam(apikeyParamName, API_KEY)
                .queryParam(byIDOrTitleImdbIDParamName, imdbID)
                .queryParam(resultTypeParamName, movieType)
                .when()
                .get()
                .then()
                .assertThat()
                .body("$", hasKey(areaNameToBeVerified))
                .extract().path(areaNameToBeVerified);
        System.out.println(areaNameToBeVerified + ": " + Resp);
    }

    public void verifyStatusCodeOfSearchByImdbID() {
        given()
                .queryParam(apikeyParamName, API_KEY)
                .queryParam(byIDOrTitleImdbIDParamName, imdbID)
                .queryParam(resultTypeParamName, movieType)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .log().status();
    }
    
    public void createMovie() {
    	HashMap movie = new HashMap();
    	movie.put("t", "Hababam Sınıfı");
    	movie.put("y", "1975");
    	String Resp= given()
    			.queryParam(apikeyParamName, API_KEY)
    			.when().log().ifValidationFails()
    			.body(movie)
    			.post()
    			.then().assertThat().
                statusCode(201).and().
                contentType(ContentType.JSON).and().
                extract().
                response().asString();
    	System.out.println("Response is\t" + Resp);
    }

}
