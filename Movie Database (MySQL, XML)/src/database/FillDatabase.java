package database;

import datamodel.Information;
import datamodel.Movie;
import datamodel.Rating;
import exceptions.MissingValueException;
import general.Parameters;
import io.MovieReaderCSV;

import java.io.File;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;

public class FillDatabase {

	private Properties properties;
	private Connection connection = null;

	private PreparedStatement pstmMovie;
	private PreparedStatement pstmInformation;
	private PreparedStatement pstmRating;

	// TODO Exercise 1.b
	private void prepareStatements() {
		String sqlMovie = "";
		sqlMovie += "INSERT INTO Movie (";
		sqlMovie += "  imdb_id,";
		sqlMovie += "  title,";
		sqlMovie += "  originalTitle,";
		sqlMovie += "  year,";
		sqlMovie += "  genres,";
		sqlMovie += "  duration,";
		sqlMovie += "  countries";
		sqlMovie += ") VALUES (?,?,?,?,?,?,?);";

		try {
			this.pstmMovie = this.connection.prepareStatement(sqlMovie);
		} catch (SQLException sqlException) {
			System.out.println("Problem with statement: INSERT INTO Movie");
			sqlException.printStackTrace();
			System.exit(0);
		}

		String sqlInformation = "";
		sqlInformation += "INSERT INTO Information (";
		sqlInformation += "  imdb_id,";
		sqlInformation += "  description,";
		sqlInformation += "  actors,";
		sqlInformation += "  director,";
		sqlInformation += "  prodCompany";
		sqlInformation += ") VALUES (?,?,?,?,?);";

		try {
			this.pstmInformation = this.connection.prepareStatement(sqlInformation);
		} catch (SQLException sqlException) {
			System.out.println("Problem with statement: INSERT INTO Information");
			sqlException.printStackTrace();
			System.exit(0);
		}

		String sqlRating = "";
		sqlRating += "INSERT INTO Rating (";
		sqlRating += "  imdb_id,";
		sqlRating += "  rating,";
		sqlRating += "  numRatings";
		sqlRating += ") VALUES (?,?,?);";

		try {
			this.pstmRating = this.connection.prepareStatement(sqlRating);
		} catch (SQLException sqlException) {
			System.out.println("Problem with statement: INSERT INTO Rating");
			sqlException.printStackTrace();
			System.exit(0);
		}
	}

	// TODO Exercise 1.b
	private void insertMovie(Movie movie) {
		try {
			this.pstmMovie.setString(1, movie.getId());
			this.pstmMovie.setString(2, movie.getTitle());
			this.pstmMovie.setString(3, movie.getOriginalTitle());
			this.pstmMovie.setInt(4, movie.getYear());
			this.pstmMovie.setString(5, movie.getGenreString());
			this.pstmMovie.setInt(6, movie.getDuration());
			this.pstmMovie.setString(7, movie.getCountryString());
		} catch (SQLException | MissingValueException exception) {
			exception.printStackTrace();
		}

		try {
			this.pstmMovie.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(0);
		}
	}

	// TODO Exercise 1.b
	private void insertInfo(Information information) {
		try {
			this.pstmInformation.setString(1, information.getId());
			this.pstmInformation.setString(2, information.getDescription());
			this.pstmInformation.setString(3, information.getActorsString());
			this.pstmInformation.setString(4, information.getDirector());
			this.pstmInformation.setString(5, information.getProdCompany());
		} catch (SQLException | MissingValueException exception) {
			System.out.println(exception);
			exception.printStackTrace();
		}

		try {
			this.pstmInformation.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(0);
		}
	}

	// TODO Exercise 1.b
	private void insertRating(Rating rating) {
		try {
			this.pstmRating.setString(1, rating.getId());
			this.pstmRating.setFloat(2, rating.getRating());
			this.pstmRating.setLong(3, rating.getNumRatings());
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		try {
			this.pstmRating.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(0);
		}
	}

	private void insertMovies(Collection<Movie> movieCollection) {
		for (Movie movie : movieCollection) {
			insertMovie(movie);
		}
		try {
			this.connection.commit();
		} catch (SQLException sqlException) {
			System.out.println(sqlException);
			System.exit(0);
		}
	}

	private void insertInformation(Collection<Information> informationCollection) {
		for (Information information : informationCollection) {
			insertInfo(information);
		}
		try {
			this.connection.commit();
		} catch (SQLException sqlException) {
			System.out.println(sqlException);
			System.exit(0);
		}
	}

	private void insertRatings(Collection<Rating> ratingCollection) {
		for (Rating rating : ratingCollection) {
			insertRating(rating);
		}

		try {
			this.connection.commit();
		} catch (SQLException sqlException) {
			System.out.println(sqlException);
			System.exit(0);
		}
	}

	private void disconnect() {
		if (this.connection != null) {
			try {
				this.pstmMovie.close();
				this.pstmInformation.close();
				this.pstmRating.close();
				this.connection.close();
			} catch (SQLException sqlException) {
				System.err.println(sqlException.getClass().getName() + ": " + sqlException.getMessage());
				sqlException.printStackTrace();
				System.exit(0);
			}
		}
	}

	private void clearTable(String tablename) {
		String sql = "";

		try (Statement statement = connection.createStatement();) {
			sql = "DELETE FROM " + tablename + ";";
			statement.executeUpdate(sql);
		} catch (Exception exception) {
			System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
			exception.printStackTrace();
			System.exit(0);
		}

		System.out.println("Deleted content from table: " + tablename);
	}

	private void connect(String filename) {
		this.properties = new Properties();
		this.properties.setProperty("PRAGMA foreign_keys", "ON");

		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + filename, properties);
			this.connection.setAutoCommit(false);
			prepareStatements();
		} catch (Exception exception) {
			System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
			System.exit(0);
		}

		System.out.println("Opened database successfully");
	}

	private int getCount(String tablename) {
		int numRows = 0;
		String sql = "";

		try (Statement statement = this.connection.createStatement();) {
			sql = "SELECT COUNT (*) FROM " + tablename + ";";
			ResultSet rs = statement.executeQuery(sql);

			if (rs.next()) {
				numRows = rs.getInt(1);
			}
		} catch (Exception exception) {
			System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
			exception.printStackTrace();
			System.exit(0);
		}

		return numRows;
	}

	public static void main(String[] args) {
		FillDatabase fillDatabase = new FillDatabase();
		String databaseFilename = Parameters.databaseFile;
		MovieReaderCSV csvReader = new MovieReaderCSV(new File(Parameters.csvPath));
		csvReader.parseFile();

		Collection<Movie> movieCollection = csvReader.getMovies();
		Collection<Information> informationCollection = csvReader.getInformation().values();
		Collection<Rating> ratingCollection = csvReader.getRating().values();

		fillDatabase.connect(databaseFilename);

		fillDatabase.clearTable("Movie");
		fillDatabase.insertMovies(movieCollection);
		System.out.println(fillDatabase.getCount("Movie") + " Movies added to Database.");

		fillDatabase.clearTable("Information");
		fillDatabase.insertInformation(informationCollection);
		System.out.println(fillDatabase.getCount("Information") + " Information-Data added to Database.");

		fillDatabase.clearTable("Rating");
		fillDatabase.insertRatings(ratingCollection);
		System.out.println(fillDatabase.getCount("Rating") + " Ratings added to Database.");

		System.out.println("FILLED DATABASE");
		fillDatabase.disconnect();
	}

}
