package edu.virginia.sde.database;

import java.sql.*;

public class DatabaseInitializer {

    private static String DB_URL = "jdbc:sqlite:course_reviews.db";

    public static void setDbUrl(String dbUrl) {
        DB_URL = dbUrl;
    }

    public static Connection initializeDatabase() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            if (conn != null) {
                System.out.println("[INFO] Connected to the database.");
                enableForeignKeys(conn);
                createTables(conn);
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
        }
    }

    public static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")");
            stmt.execute("CREATE TABLE IF NOT EXISTS Courses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subject TEXT NOT NULL," +
                    "number INTEGER NOT NULL," +
                    "title TEXT NOT NULL" +
                    ")");
            stmt.execute("CREATE TABLE IF NOT EXISTS Reviews (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL REFERENCES Users(id)," +
                    "course_id INTEGER NOT NULL REFERENCES Courses(id)," +
                    "rating INTEGER NOT NULL," +
                    "comment TEXT," +
                    "timestamp DATETIME NOT NULL" +
                    ")");
        }
    }

    public static void populateDefaultCourses(Connection conn) {
        String checkQuery = "SELECT COUNT(*) FROM Courses";
        String insertQuery = "INSERT INTO Courses (subject, number, title) VALUES (?, ?, ?)";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {

            if (rs.next() && rs.getInt(1) == 0) { // Table is empty
                conn.setAutoCommit(false); // Begin transaction
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, "CS");
                    pstmt.setInt(2, 3140);
                    pstmt.setString(3, "Software Development Essentials");
                    pstmt.addBatch();

                    pstmt.setString(1, "MATH");
                    pstmt.setInt(2, 1210);
                    pstmt.setString(3, "Calculus I");
                    pstmt.addBatch();

                    pstmt.setString(1, "PHYS");
                    pstmt.setInt(2, 2010);
                    pstmt.setString(3, "Physics I");
                    pstmt.addBatch();

                    pstmt.executeBatch(); // Execute all insertions
                    conn.commit(); // Commit transaction
                    System.out.println("[INFO] Default courses inserted.");
                } catch (SQLException e) {
                    conn.rollback(); // Rollback transaction on error
                    throw e;
                } finally {
                    conn.setAutoCommit(true); // Restore auto-commit
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

            // Insert sample user
            stmt.execute("INSERT OR IGNORE INTO Users (username, password) VALUES ('a', '1')");

            // Insert sample courses (if not already done)
            populateDefaultCourses(conn);

            // Insert sample review
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

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] SQLite JDBC driver not found: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void printTableData(Connection conn, String tableName) {
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            System.out.println("=== " + tableName + " ===");
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + rs.getObject(i) + " | ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to fetch data from " + tableName + ": " + e.getMessage());
        }
    }
}
