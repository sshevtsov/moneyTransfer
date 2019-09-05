package com.revolut.moneytransfer.resource;

import com.revolut.moneytransfer.dto.MoneyTransferDto;
import com.revolut.moneytransfer.model.TransferStatus;
import com.revolut.moneytransfer.service.AccountService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransferResource {

    private AccountService accountService;

    public TransferResource() {
        this.accountService = new AccountService();
    }

    @POST
    public Response transferMoney(MoneyTransferDto moneyTransferDto) {
        TransferStatus transferStatus = accountService.transfer(moneyTransferDto);
        Response.Status status = Response.Status.valueOf(transferStatus.getStatus());

        return Response.status(status).entity(transferStatus).build();
    }
}
