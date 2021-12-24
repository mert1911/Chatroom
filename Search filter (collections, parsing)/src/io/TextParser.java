package io;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.TreeSet;

import general.Parameters;
import model.Actor;
import model.Category;
import model.Nominee;

public class TextParser {

	public static final int ACTORS = 1; // Flag for file with actors
	public static final int AWARDS = 2; // Flag for file with nominees

	/*
	 * a map to store objects of class Actor, using his or her name as the key
	 */
	private static HashMap<String, Actor> aMap = new HashMap<>();

	// TODO Exercise 1.b
	@SuppressWarnings("unchecked")
	public static <T> void parseData(int objectType, TreeSet<T> set) {
		StringBuffer fileContent;
		String[] lines; // array to store lines of a text

		switch (objectType) {
		case ACTORS:
			fileContent = TextReader.getText(Parameters.actorsFile);
			lines = fileContent.toString().split("\n");

			for (String line : lines) {
				try {
					parseActorsLine(line, (TreeSet<Actor>) set);
				} catch (LineNotParsableException exception) {
					System.out.println(exception);
				} catch (IllegalArgumentException exception) {
					System.out.println(exception);
				} catch (DateTimeParseException exception) {
					System.out.println(exception);
				}
			}

			break;
		case AWARDS:
			fileContent = TextReader.getText(Parameters.nomineesFile);
			lines = fileContent.toString().split("\n");

			for (String line : lines) {
				try {
					parseNomineesLine(line, (TreeSet<Nominee>) set);
				} catch (LineNotParsableException exception) {
					System.out.println(exception);
				} catch (NumberFormatException exception) {
					System.out.println(exception);
				} catch (IndexOutOfBoundsException exception) {
					System.out.println(exception);
				}
			}

			break;
		default:
			break;
		}

	}

	// TODO Exercise 1.b
	private static void parseActorsLine(String line, TreeSet<Actor> coll) throws LineNotParsableException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String[] tokens = line.split(";");

		if (tokens.length == 2 || tokens.length == 3) {
			String name = tokens[0].trim();
			LocalDate birth = LocalDate.parse(tokens[1].trim(), dtf);

			Actor actor;
			if (tokens.length == 2) {
				actor = new Actor(name, birth);
			} else {
				LocalDate death = LocalDate.parse(tokens[2].trim(), dtf);
				actor = new Actor(name, birth, death);
			}

			coll.add(actor);
			aMap.put(name, actor);
		} else {
			throw new LineNotParsableException(line);
		}
	}

	// TODO Exercise 1.b
	private static void parseNomineesLine(String line, TreeSet<Nominee> coll) throws LineNotParsableException {
		String[] tokens = line.split(",");

		if (tokens.length == 5) {
			int year = Integer.parseInt(tokens[0].trim());
			Category category = parseCategory(tokens[1].trim());
			String name = tokens[2].trim();
			String movie = extractMovie(tokens[3].trim());
			String character = extractCharacter(tokens[3].trim());
			boolean oscar = tokens[4].trim().equals("YES");
			Actor actor = getActor(name);

			Nominee nominee = new Nominee(category, year, movie, character, oscar, actor);
			coll.add(nominee);
		} else {
			throw new LineNotParsableException(line);
		}
	}

	// TODO Exercise 1.b (helper method)
	private static Category parseCategory(String text) {
		switch (text) {
		case "Actress -- Leading Role":
			return Category.ACTRESS_LR;
		case "Actor -- Leading Role":
			return Category.ACTOR_LR;
		case "Actress -- Supporting Role":
			return Category.ACTRESS_SR;
		case "Actor -- Supporting Role":
			return Category.ACTOR_SR;
		default:
			return null;
		}
	}

	// TODO Exercise 1.b (helper method)
	private static String extractMovie(String text) {
		int pos = text.indexOf("{'");

		return text.substring(0, pos).trim();
	}

	// TODO Exercise 1.b (helper method)
	private static String extractCharacter(String text) {
		int pos1 = text.indexOf("{'") + 2;
		int pos2 = text.indexOf("'}");

		return text.substring(pos1, pos2).trim();
	}

	// TODO Exercise 1.b (helper method)
	private static Actor getActor(String name) {
		if (!aMap.containsKey(name)) {
			aMap.put(name, new Actor(name));
		}

		return aMap.get(name);
	}

	public static void main(String[] args) {
		// Test
		TextParser.parseData(ACTORS, new TreeSet<Actor>());
		TextParser.parseData(AWARDS, new TreeSet<Nominee>());
	}

}
