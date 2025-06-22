package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Discount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DiscountDao {

    public void InsertDiscount(Discount discount) {
        String sql = "INSERT INTO discounts (user_id, amount_of_silver, amount_of_bronze, amount_of_gold) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, discount.getUserId());
            ps.setInt(2, discount.getAmountOfSilver());
            ps.setInt(3, discount.getAmountOfBronze());
            ps.setInt(4, discount.getAmountOfGold());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateDiscount(Discount discount) {
        String sql = "UPDATE discounts SET amount_of_silver = ?, amount_of_bronze = ?, amount_of_gold = ? WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, discount.getAmountOfSilver());
            ps.setInt(2, discount.getAmountOfBronze());
            ps.setInt(3, discount.getAmountOfGold());
            ps.setInt(4, discount.getUserId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteDiscount(int userId) {
        String sql = "DELETE FROM discounts WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Discount FindDiscountByUserId(int userId) {
        String sql = "SELECT * FROM discounts WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Discount(
                            rs.getInt("user_id"),
                            rs.getInt("amount_of_silver"),
                            rs.getInt("amount_of_bronze"),
                            rs.getInt("amount_of_gold")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
