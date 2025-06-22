package com.utc2.cinema.model.entity;

public class Discount {
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAmountOfSilver() {
        return amountOfSilver;
    }

    public void setAmountOfSilver(int amountOfSliver) {
        this.amountOfSilver = amountOfSliver;
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
    private int amountOfSilver;
    private int amountOfBronze;
    private int amountOfGold;
    public enum DiscountType {
        GOLD, SILVER, BRONZE
    }

    public void giveDiscount(DiscountType type, int amount) {
        switch (type) {
            case GOLD -> amountOfGold += amount;
            case SILVER -> amountOfSilver += amount;
            case BRONZE -> amountOfBronze += amount;
        }
    }
    public Discount(int userId, int amountOfSilver, int amountOfBronze, int amountOfGold) {
        this.userId = userId;
        this.amountOfSilver = amountOfSilver;
        this.amountOfBronze = amountOfBronze;
        this.amountOfGold = amountOfGold;
    }
}
