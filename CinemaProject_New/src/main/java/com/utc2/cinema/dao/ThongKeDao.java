package com.utc2.cinema.dao;

import com.utc2.cinema.model.entity.StatisticalFilm;
import com.utc2.cinema.config.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDao {
    public List<StatisticalFilm> getFilmStatistics(LocalDate startDate, LocalDate endDate, String keyword) throws SQLException {
        List<StatisticalFilm> list = new ArrayList<>();

        String sql = """
            SELECT f.Name AS FilmName,
                   COUNT(DISTINCT ms.Id) AS ShowCount,
                   COUNT(r.Id) AS SeatsSold,
                   SUM(IFNULL(r.Cost, 0)) AS TotalRevenue
            FROM MovieShow ms
            JOIN Film f ON f.Id = ms.FilmId
            LEFT JOIN Reservation r ON r.ShowId = ms.Id
            LEFT JOIN Bill b ON b.Id = r.BillId AND b.DatePurchased BETWEEN ? AND ?
            WHERE ms.StartTime BETWEEN ? AND ?
            """ + (keyword != null && !keyword.isEmpty() ? " AND f.Name LIKE ?" : "") +
                " GROUP BY f.Name ORDER BY TotalRevenue DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String start = startDate + " 00:00:00";
            String end = endDate + " 23:59:59";

            stmt.setString(1, start);
            stmt.setString(2, end);
            stmt.setString(3, start);
            stmt.setString(4, end);

            if (keyword != null && !keyword.isEmpty()) {
                stmt.setString(5, "%" + keyword + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("FilmName");
                int shows = rs.getInt("ShowCount");
                int seats = rs.getInt("SeatsSold");
                double revenue = rs.getDouble("TotalRevenue");
                list.add(new StatisticalFilm(name, shows, seats, revenue));
            }
        }

        return list;
    }
}
