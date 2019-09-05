package com.revolut.moneytransfer.dao;

import com.revolut.moneytransfer.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountDao {

    private JdbcTemplate jdbcTemplate;

    public AccountDao() {
        jdbcTemplate = new JdbcTemplate();
    }

    public Account addAccount(Account account) {
        jdbcTemplate.execute("INSERT INTO accounts(amount, accountnumber) VALUES(?, ?)", account.getAmount(), account.getAccountNumber());
        Account createdAccount = jdbcTemplate.executeForObject("SELECT * FROM accounts WHERE accountNumber = ?", new AccountMapper(), account.getAccountNumber());
        return createdAccount;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = jdbcTemplate.executeForList("SELECT * FROM accounts", new AccountMapper());
        return accounts;
    }

    public Account getAccountById(int id) {
        Account account = jdbcTemplate.executeForObject("SELECT * FROM accounts WHERE id=?", new AccountMapper(), id);
        return account;
    }

    public void deleteById(int id){
        jdbcTemplate.execute("DELETE FROM accounts WHERE id = ?", id);
    }

    public void updateAccountAmountById(int id, int amount){
        jdbcTemplate.execute("UPDATE accounts SET amount = ? WHERE id = ?", amount, id);
    }

    public void transfer(Account accountFrom, Account accountTo){
        jdbcTemplate.transfer(accountFrom, accountTo);
    }

    private class AccountMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int row) throws SQLException {
            return new Account(rs.getInt("id"), rs.getInt("amount"), rs.getInt("accountNumber"));
        }
    }
}
