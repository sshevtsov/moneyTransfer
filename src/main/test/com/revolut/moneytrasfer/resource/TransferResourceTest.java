package com.revolut.moneytrasfer.resource;

import com.revolut.moneytransfer.dto.MoneyTransferDto;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.resource.AccountResource;
import com.revolut.moneytransfer.resource.TransferResource;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.Assert.assertEquals;

public class TransferResourceTest {
    private static final String BASE_URI = "http://localhost:8080/moneytrasfer/";
    private HttpServer server;

    @Before
    public void setUp() {
        final ResourceConfig rc = new ResourceConfig(AccountResource.class, TransferResource.class);
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testTransfer() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);

        Account account1 = new Account(100);
        Account account2 = new Account(200);

        Response response1 = target
                .path("/accounts/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(account1, MediaType.APPLICATION_JSON));

        assertEquals("Http Response should be 201: ", Response.Status.CREATED.getStatusCode(), response1.getStatus());


        Response response2 = target
                .path("/accounts/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(account2, MediaType.APPLICATION_JSON));

        assertEquals("Http Response should be 201: ", Response.Status.CREATED.getStatusCode(), response2.getStatus());

        MoneyTransferDto moneyTransferDto = new MoneyTransferDto(1,2,50);

        Response response4 = target
                .path("/transfer/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(moneyTransferDto, MediaType.APPLICATION_JSON));

        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response4.getStatus());

        Response response5 = target
                .path("/accounts/1")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(), response4.getStatus());

        Account accountFrom = response5.readEntity(Account.class);

        assertEquals(accountFrom.getAmount(), 50);

        Response response6 = target
                .path("/accounts/2")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Account accountTo = response6.readEntity(Account.class);

        assertEquals(accountTo.getAmount(), 250);
    }

    @Test
    public void testTransferInsufficientFunds () {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URI);

        Account account1 = new Account(100);
        Account account2 = new Account(200);

        Response response1 = target
                .path("/accounts/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(account1, MediaType.APPLICATION_JSON));

        assertEquals("Http Response should be 201: ", Response.Status.CREATED.getStatusCode(), response1.getStatus());

        Response response2 = target
                .path("/accounts/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(account2, MediaType.APPLICATION_JSON));

        assertEquals("Http Response should be 201: ", Response.Status.CREATED.getStatusCode(), response2.getStatus());


        MoneyTransferDto moneyTransferDto = new MoneyTransferDto(1,2,150);

        Response response4 = target
                .path("/transfer/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(moneyTransferDto, MediaType.APPLICATION_JSON));

        assertEquals("Http Response should be 400: ", Response.Status.BAD_REQUEST.getStatusCode(), response4.getStatus());
    }
}
