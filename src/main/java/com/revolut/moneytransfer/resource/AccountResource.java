package com.revolut.moneytransfer.resource;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private AccountService accountService = new AccountService();

    @GET
    public List<Account> getAccounts() throws SQLException {
        return accountService.getAllAccounts();
    }

    @GET
    @Path("/{accountId}")
    public Account getAccount(@PathParam("accountId") int id, @Context UriInfo uriInfo) {
        Account account = accountService.getAccount(id);
        account.addLink(getUriForSelf(uriInfo, account), "self");

        return account;
    }

    @POST
    public Response addAccount(Account account, @Context UriInfo uriInfo) throws SQLException {
        Account createdAccount = accountService.addAccount(account);
        createdAccount.addLink(getUriForSelf(uriInfo, account), "self");
        String newId = String.valueOf(createdAccount.getId());
        URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(uri)
            .entity(createdAccount).build();
    }

    @PUT
    @Path("/{accountId}")
    public Account updateAccount(@PathParam("accountId") int id, Account account, @Context UriInfo uriInfo) {
        account.setId(id);
        account.addLink(getUriForSelf(uriInfo, account), "self");
        return accountService.updateAccount(account);
    }

    @DELETE
    @Path("/{accountId}")
    public void deleteAccount(@PathParam("accountId") int id) {
        accountService.removeAccount(id);
    }

    private String getUriForSelf(UriInfo uriInfo, Account account) {
        String uri = uriInfo.getBaseUriBuilder()
                .path(AccountResource.class)
                .path(Long.toString(account.getId()))
                .build()
                .toString();
        return uri;
    }
}
