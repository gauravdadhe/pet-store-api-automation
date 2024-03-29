package Cucumber.options;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class) @CucumberOptions(features = "src/test/java/features", glue = {
    "stepDefinitions"}, tags = "@pet", monochrome = true, plugin = {"pretty",
    "json:target/Cucumber.json"}) public class TestRunner {

    @AfterClass public static void uploadReportToCalliope() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file[]",
                "/C:/appl/gitRepos/rahul/SwaggerPetStore/target/Cucumber.json", RequestBody
                    .create(MediaType.parse("application/octet-stream"),
                        new File("/C:/appl/gitRepos/rahul/SwaggerPetStore/target/Cucumber.json")))
            .build();
        Request request = new Builder().url(
            "https://app.calliope.pro/api/v2/profile/3505/import?os=myos&platform=myplatform&build=mybuild")
            .method("POST", body).addHeader("x-api-key",
                "YzgzZmVhZGFhZmM3MTcyMzllYmIwYjgyYWIyMzBlZDkyN2FkNzliNGQ1NmQ4NGNhMzQ1MjBlMDgwMzVlZjcyYjkw")
            .build();
        Response response = client.newCall(request).execute();
    }
}
