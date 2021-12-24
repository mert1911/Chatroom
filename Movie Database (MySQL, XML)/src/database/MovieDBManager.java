package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

public class MovieDBManager extends DatabaseManager {

	private PreparedStatement pstmMovie;
	private PreparedStatement pstmInformation;
	private PreparedStatement pstmRating;

	public MovieDBManager() {
		super();
		this.prepareStatements();
	}

	// TODO Exercise 1.c
	public Collection<String> getCountries() throws SQLException {
		SortedSet<String> countries = new TreeSet<String>();
		Statement stm = this.connection.createStatement();

		String sql = "";
		sql += "SELECT DISTINCT countries ";
		sql += "FROM            Movie";

		ResultSet rs = stm.executeQuery(sql);

		while (rs.next()) {
			Collections.addAll(countries, rs.getString("countries").split(", "));
		}

		return countries;
	}

	// TODO Exercise 1.c
	public Collection<String> getDirectors() throws SQLException {
		Vector<String> directors = new Vector<>();
		Statement stm = this.connection.createStatement();

		String sql = "";
		sql += "SELECT DISTINCT director ";
		sql += "FROM            Information ";
		sql += "ORDER BY        director";

		ResultSet rs = stm.executeQuery(sql);

		while (rs.next()) {
			String director = rs.getString("director");
			directors.add(director);
		}

		return directors;
	}

	// TODO Exercise 1.c
	private void prepareStatements() {
		String sqlMovie = "";
		sqlMovie += "SELECT imdb_id, title, originalTitle, year, genres, duration, countries ";
		sqlMovie += "FROM   Movie ";
		sqlMovie += "WHERE (imdb_id LIKE ? OR title LIKE ? OR originalTitle LIKE ?)";
		sqlMovie += "  AND (year <= ? AND year >= ?)";
		sqlMovie += "  AND (genres LIKE ?)";
		sqlMovie += "  AND (duration <= ? AND duration >= ?)";
		sqlMovie += "  AND (countries LIKE ?)";

		try {
			this.pstmMovie = this.connection.prepareStatement(sqlMovie);
		} catch (SQLException exception) {
			System.out.println("Problem with statement: SELECT ... FROM Movie");
			exception.printStackTrace();
			System.exit(0);
		}

		String sqlInformation = "";
		sqlInformation += "SELECT imdb_id, description, actors, director, prodCompany ";
		sqlInformation += "FROM   Information ";
		sqlInformation += "WHERE  (actors LIKE ?)";
		sqlInformation += "  AND  (director LIKE ?)";

		try {
			this.pstmInformation = this.connection.prepareStatement(sqlInformation);
		} catch (SQLException exception) {
			System.out.println("Problem with statement: SELECT ... FROM Information");
			exception.printStackTrace();
			System.exit(0);
		}

		String sqlRating = "";
		sqlRating += "SELECT imdb_id, rating, numRatings ";
		sqlRating += "FROM   Rating ";
		sqlRating += "WHERE  (rating <= ? AND rating >= ?)";
		sqlRating += "  AND  (numRatings <= ? AND numRatings >= ?)";

		try {
			this.pstmRating = this.connection.prepareStatement(sqlRating);
		} catch (SQLException exception) {
			System.out.println("Problem with statement: SELECT ... FROM Rating");
			exception.printStackTrace();
			System.exit(0);
		}
	}

	// TODO Exercise 1.c
	public ResultSet movieQuery(String id, String title, String originalTitle, int yearMax, int yearMin, String genres,
			int lengthMax, int lengthMin, String countries) {
		ResultSet resultSetMovie = null;

		try {
			this.pstmMovie.setString(1, id);
			this.pstmMovie.setString(2, title);
			this.pstmMovie.setString(3, originalTitle);
			this.pstmMovie.setInt(4, yearMax);
			this.pstmMovie.setInt(5, yearMin);
			this.pstmMovie.setString(6, genres);
			this.pstmMovie.setInt(7, lengthMax);
			this.pstmMovie.setInt(8, lengthMin);
			this.pstmMovie.setString(9, countries);

			resultSetMovie = this.pstmMovie.executeQuery();
		} catch (SQLException exception) {
			exception.printStackTrace();
			System.out.println("Error from Movie-Query!");
		}

		return resultSetMovie;
	}

	// TODO Exercise 1.c
	public ResultSet informationQuery(String actor, String director) {
		ResultSet resultSetInformation = null;

		try {
			this.pstmInformation.setString(1, actor);
			this.pstmInformation.setString(2, director);

			resultSetInformation = this.pstmInformation.executeQuery();
		} catch (SQLException exception) {
			exception.printStackTrace();
			System.out.println("Error from Information-Query!");
		}

		return resultSetInformation;
	}

	// TODO Exercise 1.c
	public ResultSet ratingQuery(float ratingMax, float ratingMin, long numRatingsMax, long numRatingsMin) {
		ResultSet resultSetRating = null;

		try {
			this.pstmRating.setFloat(1, ratingMax);
			this.pstmRating.setFloat(2, ratingMin);
			this.pstmRating.setLong(3, numRatingsMax);
			this.pstmRating.setLong(4, numRatingsMin);

			resultSetRating = this.pstmRating.executeQuery();
		} catch (SQLException exception) {
			exception.printStackTrace();
			System.out.println("Error from Rating-Query!");
		}

		return resultSetRating;
	}

}
