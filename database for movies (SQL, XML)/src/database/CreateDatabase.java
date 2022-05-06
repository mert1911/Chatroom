package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import general.Parameters;

public class CreateDatabase {

	private Connection connection = null;

	// TODO Exercise 1.a
	private void createTableMovie() {
		String sql = "";
		sql += "CREATE TABLE Movie (";
		sql += "  imdb_id       TEXT    PRIMARY KEY,";
		sql += "  title         TEXT    NOT NULL,";
		sql += "  originalTitle TEXT    NOT NULL,";
		sql += "  year          INTEGER NOT NULL,";
		sql += "  genres        TEXT,";
		sql += "  duration      INTEGER NOT NULL,";
		sql += "  countries     TEXT";
		sql += ");";

		try (Statement statement = this.connection.createStatement();) {
			statement.executeUpdate(sql);
			System.out.println("Created table: Movie");
		} catch (SQLException sqlException) {
			System.out.println("Problem with statement: CREATE TABLE Movie");
			sqlException.printStackTrace();
			System.exit(0);
		}
	}

	// TODO Exercise 1.a
	private void createTableInformation() {
		String sql = "";
		sql += "CREATE TABLE Information (";
		sql += "  imdb_id     TEXT PRIMARY KEY,";
		sql += "  description TEXT,";
		sql += "  actors      TEXT,";
		sql += "  director    TEXT,";
		sql += "  prodCompany TEXT";
		sql += ");";

		try (Statement statement = this.connection.createStatement();) {
			statement.executeUpdate(sql);
			System.out.println("Created table: Information");
		} catch (SQLException sqlException) {
			System.out.println("Problem with statement: CREATE TABLE Information");
			sqlException.printStackTrace();
			System.exit(0);
		}
	}

	// TODO Exercise 1.a
	private void createTableRating() {
		String sql = "";
		sql += "CREATE TABLE Rating (";
		sql += "  imdb_id    TEXT    PRIMARY KEY,";
		sql += "  rating     FLOAT   NOT NULL,";
		sql += "  numRatings INTEGER NOT NULL";
		sql += ");";

		try (Statement statement = this.connection.createStatement();) {
			statement.executeUpdate(sql);
			System.out.println("Created table: Rating");
		} catch (SQLException sqlException) {
			System.out.println("Problem with statement: CREATE TABLE Rating");
			sqlException.printStackTrace();
			System.exit(0);
		}
	}

	public void connect(String filename) {
		Properties properties = new Properties();
		properties.setProperty("PRAGMA foreign_keys", "ON");

		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + filename, properties);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		System.out.println("Database opened successfully!");
	}

	private void dropAllTables() {
		String sql = "";

		try (Statement statement = connection.createStatement();) {
			for (String s : new String[] { "Movie", "Information", "Rating" }) {
				sql = "DROP TABLE IF EXISTS " + s + ";";
				statement.executeUpdate(sql);
			}
		} catch (Exception e) {
			System.out.println("Problem with statement " + sql);
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}

		System.out.println("Deleted tables!");
	}

	private void disconnect() {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public static void main(String[] args) {
		CreateDatabase cdb = new CreateDatabase();

		String filename = Parameters.databaseFile;
		cdb.connect(filename);
		cdb.dropAllTables();

		cdb.createTableMovie();
		cdb.createTableInformation();
		cdb.createTableRating();
		System.out.println("CREATED DATABASE");

		cdb.disconnect();
	}

}
