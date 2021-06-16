package stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

public class StepDefinition {
    public static Response response;
    public static RequestSpecification requestSpecification;
    public static JSONObject msgBody = new JSONObject();
    public static Map<String, String> customHeaders = new HashMap();

    @Given("I use {string} as base URI") public void iUseAsBaseUri(String baseURI) {
        RestAssured.baseURI = baseURI;
    }

    @When("I make {string} call to {string}")
    public void i_make_call_to(String method, String totalPath) {
        Map<String, String> queryParams = new HashMap<String, String>();
        JSONObject msgBody = new JSONObject();

        //Get queryParams
        if (totalPath.contains("?")) {
            String[] paramPairs = ((totalPath.split("\\?"))[1]).split("&");
            for (String pair : paramPairs) {
                String[] myPair = pair.split("=");
                if (myPair.length == 1) { //When param value is blank
                    queryParams.put(myPair[0], "");
                } else {
                    queryParams.put(myPair[0], myPair[1]);
                }
            }
        }

        //Get Path
        String path = (totalPath.split("\\?"))[0];

        RestAssured.basePath = path;
        requestSpecification = RestAssured.given().headers(customHeaders).queryParams(queryParams);
        switch (method.toUpperCase().trim()) {
            case "GET":
                response = requestSpecification.given().when().get();
                break;
            case "POST":
                response = requestSpecification.body(msgBody.toJSONString()).post();
                break;
            case "DELETE":
                response = requestSpecification.given().when().delete();
                break;
            case "PUT":
                response = requestSpecification.body(msgBody.toJSONString()).put();
                break;
            default:
                Assert.fail("Ooops!! Method [" + method + "] is not implemented.");
                break;
        }
    }

    @Then("I get response code {string}") public void i_get_response_code(String responseCode) {
        Assert.assertEquals(
            "Incorrect response code. Expected: " + responseCode + " Actual: " + response
                .statusCode(), response.statusCode(), Integer.parseInt(responseCode));
    }

    @Then("I verify at least {string} pet details is available in the response")
    public void i_verify_at_least_pet_details_is_available_in_the_response(String count)
        throws ParseException {
        List<String> myResponse = response.jsonPath().getList("$");
        Assert.assertTrue(myResponse.size() + " pet details found.",
            myResponse.size() >= Integer.parseInt(count));
    }

    @Then("I read request body from {string}") public void iReadRequestBodyFrom(String fileName)
        throws IOException, ParseException {
        msgBody.clear();
        JSONParser parser = new JSONParser();
        Object obj =
            parser.parse(new FileReader("src/test/resources/testdata/" + fileName + ".json"));
        msgBody = (JSONObject) obj;
    }

    @Given("I make REST service headers with the below fields")
    public void iMakeRestServiceHeaderWithBelowFields(DataTable headerValues)
        throws ParseException {
        customHeaders.clear();
        List<Map<String, String>> headers = headerValues.asMaps(String.class, String.class);
        Iterator myHeader = headers.iterator();

        while (myHeader.hasNext()) {
            Map<String, String> header = (Map) myHeader.next();
            customHeaders.putAll(header);
        }
    }
}
