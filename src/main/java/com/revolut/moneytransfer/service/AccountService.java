package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.dao.AccountDao;
import com.revolut.moneytransfer.dao.ConnectionFactory;
import com.revolut.moneytransfer.dto.MoneyTransferDto;
import com.revolut.moneytransfer.exception.DataNotFoundException;
import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.model.TransferStatus;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AccountService {

    AccountDao accountDao = new AccountDao();

    public List<Account> getAllAccounts() throws SQLException {
        return accountDao.getAllAccounts();
    }

    public Account getAccount(int id) {
        Account account = accountDao.getAccountById(id);
        if(account == null) {
            throw new DataNotFoundException("Account with id = " + id + " not found!");
        }
        return account;
    }

    public Account addAccount(Account account) throws SQLException {
        return accountDao.addAccount(account);
    }

    public Account updateAccount(Account account) {
        if(account == null) {
            throw new DataNotFoundException("Account with id = " + account.getId() + " not found!");
        }
        accountDao.updateAccountAmountById(account.getId(), account.getAmount());
        return account;
    }

    public void removeAccount(int id) {
        accountDao.deleteById(id);
    }

    public TransferStatus transfer(MoneyTransferDto moneyTransferDto) {
        Account accountFrom = getAccount(moneyTransferDto.getFromAccountId());
        Account accountTo = getAccount(moneyTransferDto.getToAccountId());

        int transferAmount = moneyTransferDto.getTransferAmount();

        if (accountFrom.getAmount() - transferAmount < 0) {
            return new TransferStatus(Response.Status.BAD_REQUEST.name(), "Insufficient funds in the account with id = " + accountFrom.getId());
        }

        accountFrom.setAmount(accountFrom.getAmount() - transferAmount);
        accountTo.setAmount(accountTo.getAmount() + transferAmount);

        accountDao.transfer(accountFrom, accountTo);

        return new TransferStatus(Response.Status.OK.name(), "Transfer successful!");
    }
}
