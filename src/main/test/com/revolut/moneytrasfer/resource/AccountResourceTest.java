package com.revolut.moneytrasfer.resource;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.resource.AccountResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class AccountResourceTest{

    private static final String BASE_URI = "http://localhost:8080/moneytrasfer/";
    private HttpServer server;

    @Before
    public void setUp() {
        final ResourceConfig rc = new ResourceConfig(AccountResource.class);
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    @After
    public void tearDown() {
        server.stop();
    }


    @Test
    public void tesGetAllAccounts() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);

        Response response = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Http Content-Type should be: ", MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    public void tesAddAccount() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        Response response = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(new Account(200), MediaType.APPLICATION_JSON));

        assertEquals(response.getStatus(),201);
    }

    @Test
    public void tesDeleteAccount() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        Response response = target
                .path("/accounts/1")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals("Http Response should be 204: ",response.getStatus(),204);
    }

    @Test
    public void tesUpdateAccount() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);
        Response response = target
                .path("/accounts/1")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(new Account(200), MediaType.APPLICATION_JSON));

        assertEquals("Http Response should be 200: ",response.getStatus(),200);
    }
}
