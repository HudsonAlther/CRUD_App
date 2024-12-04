package edu.virginia.sde.database;

import java.sql.*;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

    public static Connection initializeDatabase() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            if (conn != null) {
                System.out.println("[INFO] Connected to the database.");
                enableForeignKeys(conn);
                createTables(conn);
                populateDefaultCourses(conn);
                System.out.println("[INFO] Database setup complete.");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    private static void enableForeignKeys(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            System.out.println("[INFO] Foreign key constraints enabled.");
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")");
            System.out.println("[INFO] Users table created or already exists.");

            stmt.execute("CREATE TABLE IF NOT EXISTS Courses (" +
                    "id INTEGER PRIMARY KEY," +
                    "subject TEXT NOT NULL," +
                    "number INTEGER NOT NULL," +
                    "title TEXT NOT NULL" +
                    ")");
            System.out.println("[INFO] Courses table created or already exists.");

            stmt.execute("CREATE TABLE IF NOT EXISTS Reviews (" +
                    "id INTEGER PRIMARY KEY," +
                    "user_id INTEGER NOT NULL REFERENCES Users(id)," +
                    "course_id INTEGER NOT NULL REFERENCES Courses(id)," +
                    "rating INTEGER NOT NULL," +
                    "comment TEXT," +
                    "timestamp DATETIME NOT NULL" +
                    ")");
            System.out.println("[INFO] Reviews table created or already exists.");
        }
    }

    public static void populateDefaultCourses(Connection conn) {
        String checkQuery = "SELECT COUNT(*) FROM Courses";
        String insertQuery = "INSERT INTO Courses (subject, number, title) VALUES (?, ?, ?)";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {

            if (rs.next() && rs.getInt(1) == 0) { // If no courses exist
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, "CS");
                    pstmt.setInt(2, 3140);
                    pstmt.setString(3, "Software Development Essentials");
                    pstmt.addBatch();

                    pstmt.setString(1, "MATH");
                    pstmt.setInt(2, 1210);
                    pstmt.setString(3, "Calculus I");
                    pstmt.addBatch();

                    pstmt.executeBatch();
                    System.out.println("[INFO] Default courses inserted.");
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to populate default courses: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void populateSampleData(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            System.out.println("[INFO] Populating sample data...");
            stmt.execute("INSERT OR IGNORE INTO Users (username, password) VALUES ('a', '1')");
            stmt.execute("INSERT OR IGNORE INTO Courses (subject, number, title) VALUES ('CS', 3140, 'Software Development Essentials')");
            stmt.execute("INSERT OR IGNORE INTO Reviews (user_id, course_id, rating, comment, timestamp) " +
                    "VALUES ((SELECT id FROM Users WHERE username = 'a'), " +
                    "(SELECT id FROM Courses WHERE subject = 'CS' AND number = 3140), " +
                    "5, 'Great course!', DATETIME('now'))");
            System.out.println("[INFO] Sample data inserted successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to insert sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
