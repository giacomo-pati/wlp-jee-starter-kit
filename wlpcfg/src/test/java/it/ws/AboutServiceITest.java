package it.ws;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import it.EndpointTest;

public class AboutServiceITest extends EndpointTest {

	@Test
	public void create() throws UnirestException {
		HttpResponse<JsonNode> response = Unirest //
				.get(url("/about")) //
				.asJson();

		assertEquals(System.getProperty("war.name"), response.getBody().getObject().getString("application"));
	}

}
