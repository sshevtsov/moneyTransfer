package com.revolut.moneytransfer.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlRootElement
public class Account {

    private int id;
    private int amount;
    private int accountNumber;
    private List<Link> links = new ArrayList<>();

    public Account() { }

    public Account(int amount) {
        this.amount = amount;
    }

    public Account(int amount, int accountNumber) {
        this.amount = amount;
        this.accountNumber = accountNumber;
    }

    public Account(int id, int amount, int accountNumber) {
        this.id = id;
        this.amount = amount;
        this.accountNumber = accountNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(String url, String rel) {
        Link link = new Link();
        link.setLink(url);
        link.setRel(rel);
        links.add(link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                amount == account.amount &&
                accountNumber == account.accountNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, accountNumber);
    }
}
