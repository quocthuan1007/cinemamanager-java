package com.utc2.cinema.dao;

import com.utc2.cinema.config.Database;
import com.utc2.cinema.model.entity.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InventoryDao {
    public static void useVoucher(int userId, String type, int amount) {
        if (amount == 0) return;

        String column = null;

        switch (type.toLowerCase()) {
            case "gold":
                column = "amount_of_gold";
                break;
            case "silver":
                column = "amount_of_silver";
                break;
            case "bronze":
                column = "amount_of_bronze";
                break;
            case "ticket":
                column = "amount_of_ticket_discount";
                break;
            default:
                System.err.println("Loại voucher không hợp lệ: " + type);
                return;
        }

        String sql = amount > 0
                ? "UPDATE inventory SET " + column + " = " + column + " + ? WHERE account_id = ?"
                : "UPDATE inventory SET " + column + " = " + column + " + ? WHERE account_id = ? AND " + column + " >= ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, amount);
            ps.setInt(2, userId);
            if (amount < 0) {
                ps.setInt(3, Math.abs(amount));
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Không thể cập nhật voucher. Có thể không đủ số dư hoặc user không tồn tại.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertInventory(Inventory inventory) {
        String sql = "INSERT INTO inventory (account_id, amount_of_silver, amount_of_bronze, amount_of_gold, amount_of_ticket_discount) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, inventory.getaccountId());
            ps.setInt(2, inventory.getAmountOfSilver());
            ps.setInt(3, inventory.getAmountOfBronze());
            ps.setInt(4, inventory.getAmountOfGold());
            ps.setInt(5, inventory.getAmountOfTicketDiscount());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateInventory(Inventory inventory) {
        String sql = "UPDATE inventory SET amount_of_silver = ?, amount_of_bronze = ?, amount_of_gold = ?, amount_of_ticket_discount = ? WHERE account_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, inventory.getAmountOfSilver());
            ps.setInt(2, inventory.getAmountOfBronze());
            ps.setInt(3, inventory.getAmountOfGold());
            ps.setInt(4, inventory.getAmountOfTicketDiscount());
            ps.setInt(5, inventory.getaccountId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteInventory(int userId) {
        String sql = "DELETE FROM inventory WHERE account_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Inventory findInventoryByUserId(int userId) {
        String sql = "SELECT * FROM inventory WHERE account_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Inventory(
                            rs.getInt("account_id"),
                            rs.getInt("amount_of_silver"),
                            rs.getInt("amount_of_bronze"),
                            rs.getInt("amount_of_gold"),
                            rs.getInt("amount_of_ticket_discount")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
