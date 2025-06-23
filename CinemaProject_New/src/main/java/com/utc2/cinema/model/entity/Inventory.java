package com.utc2.cinema.model.entity;

public class Inventory {
    private int accountId;
    private int amountOfSilver;
    private int amountOfBronze;
    private int amountOfGold;
    private int amountOfTicketDiscount; // Thêm thuộc tính mới

    public int getaccountId() {
        return accountId;
    }

    public void setaccountId(int userId) {
        this.accountId = userId;
    }

    public int getAmountOfSilver() {
        return amountOfSilver;
    }

    public void setAmountOfSilver(int amountOfSilver) {
        this.amountOfSilver = amountOfSilver;
    }

    public int getAmountOfBronze() {
        return amountOfBronze;
    }

    public void setAmountOfBronze(int amountBronze) {
        this.amountOfBronze = amountBronze;
    }

    public int getAmountOfGold() {
        return amountOfGold;
    }

    public void setAmountOfGold(int amountOfGold) {
        this.amountOfGold = amountOfGold;
    }

    // Getter và Setter cho amountOfTicketDiscount
    public int getAmountOfTicketDiscount() {
        return amountOfTicketDiscount;
    }

    public void setAmountOfTicketDiscount(int amountOfTicketDiscount) {
        this.amountOfTicketDiscount = amountOfTicketDiscount;
    }

    public enum DiscountType {
        GOLD, SILVER, BRONZE, TICKET // Thêm loại TICKET
    }

    public void giveDiscount(DiscountType type, int amount) {
        switch (type) {
            case GOLD -> amountOfGold += amount;
            case SILVER -> amountOfSilver += amount;
            case BRONZE -> amountOfBronze += amount;
            case TICKET -> amountOfTicketDiscount += amount; // Xử lý cho TICKET
        }
    }

    public Inventory(int accountId, int amountOfSilver, int amountOfBronze, int amountOfGold, int amountOfTicketDiscount) {
        this.accountId = accountId;
        this.amountOfSilver = amountOfSilver;
        this.amountOfBronze = amountOfBronze;
        this.amountOfGold = amountOfGold;
        this.amountOfTicketDiscount = amountOfTicketDiscount;
    }
}
