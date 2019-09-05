package com.revolut.moneytransfer.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MoneyTransferDto {
    private int fromAccountId;
    private int toAccountId;
    private int transferAmount;

    public MoneyTransferDto() {
    }

    public MoneyTransferDto(int fromAccountId, int toAccountId, int transferAmount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transferAmount = transferAmount;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public int getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(int transferAmount) {
        this.transferAmount = transferAmount;
    }
}
