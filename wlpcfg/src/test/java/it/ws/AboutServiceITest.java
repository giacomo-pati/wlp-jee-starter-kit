package it.ws;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import it.EndpointTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AboutServiceITest extends EndpointTest {

    @Test
    public void create() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest //
                .get(url("/about")) //
                .asJson();

        assertEquals(System.getProperty("war.name"), response.getBody().getObject().getString("application"));
    }

}
