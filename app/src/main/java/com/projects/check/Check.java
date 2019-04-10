package com.projects.check;

public class Check {

    private String bankBranch;
    private String recipientName;
    private String amount;
    private String checkDate;

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(int jod, int fils) {
        float filsf = fils / 1000;
        float amount = jod + filsf;
        this.amount = String.valueOf(amount);
        System.out.println(this.amount);
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }
}
