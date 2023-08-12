import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import org.json.*;

import static java.net.http.HttpClient.*;

public class EloguaMain {
    private static final String SITE_NAME = "TechnologyPartnerPutitForward";
    private static final String USERNAME = "${USER}";
    private static final String PWD = "${PWD}";
    private static final String LOGIN_URL = "https://login.eloqua.com/id";

    private static String getBase64AuthString(final String siteName,
                                              final String username,
                                              final String password) {
        return Base64.getEncoder().encodeToString(
                (siteName + '\\' + username + ':' + password).getBytes()
        );
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI(LOGIN_URL))
                .header("Authorization", "Basic " + getBase64AuthString(SITE_NAME, USERNAME, PWD))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonString = response.body(); ; //assign your JSON String here
        JSONObject obj = new JSONObject(jsonString);

       final String baseUrl = String.valueOf(obj.getJSONObject("urls").get("apis"));

       System.out.println(baseUrl);

        HttpRequest request1 = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("https://secure.p02.eloqua.com/API/REST/2.0/data/contacts"))
                .header("Content-Type","application/json")
             //   .header("Authorization", "Basic VGVjaG5vbG9neVBhcnRuZXJQdXRpdEZvcndhcmRcWXVyYS5JdmFub3Y6eDVJVUxyUHE3bw==")
                .header("Authorization", "Basic " + getBase64AuthString(SITE_NAME, USERNAME, PWD))
                .build();

        HttpResponse<String> response1 =
                client.send(request1, HttpResponse.BodyHandlers.ofString());

        System.out.println(response1);

    }
}
