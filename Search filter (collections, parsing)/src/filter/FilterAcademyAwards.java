package filter;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import io.TextParser;
import model.Actor;
import model.Nominee;

public class FilterAcademyAwards {
	private TreeSet<Actor> actors = new TreeSet<>();
	private TreeSet<Nominee> nominees = new TreeSet<>();

	public FilterAcademyAwards() {
		this.init();
	}

	/*
	 * read and parse text from data files and create instances of the classes Actor
	 * and Nominee that are stored in the respective TreeSet instances
	 */
	public void init() {
		TextParser.parseData(TextParser.ACTORS, this.actors);
		TextParser.parseData(TextParser.AWARDS, this.nominees);
	}

	// TODO Exercise 2.a
	public Vector<String> getActorNames() {
		Vector<String> names = new Vector<>();

		for (Actor actor : this.actors) {
			names.add(actor.getName());
		}

		return names;
	}

	// TODO Exercise 2.a
	public Vector<Integer> getYears() {
		TreeSet<Integer> years = new TreeSet<>();

		for (Nominee nominee : this.nominees) {
			years.add(nominee.getYear());
		}

		return new Vector<Integer>(years);
	}

	// TODO Exercise 2.a
	private Actor getActor(String name) {
		for (Actor actor : this.actors) {
			if (actor.getName().equals(name)) {
				return actor;
			}
		}

		return null;
	}

	// TODO Exercise 2.b
	public String getAwardsActor(String name) {
		Actor actor = getActor(name);

		StringBuffer result = new StringBuffer(actor + "\n");
		StringBuffer awards = new StringBuffer(" Awards:\n");
		StringBuffer nominations = new StringBuffer(" Nominations:\n");

		boolean noAwards = true;
		boolean noNominations = true;

		for (Nominee nominee : this.nominees) {
			if (nominee.getActor().getName().equals(name)) {
				String details = " " + nominee.getYear() + ": " + nominee.getMovie() + " (" + nominee.getCharacter()
						+ "), Category: " + nominee.getCategory() + "\n";

				if (nominee.isWon()) {
					awards.append(details);
					noAwards = false;
				} else {
					nominations.append(details);
					noNominations = false;
				}
			}
		}

		if (noAwards) {
			awards.append(" ---\n");
		}

		if (noNominations) {
			nominations.append(" ---\n");
		}

		result.append(awards + "\n");
		result.append(nominations);

		return result.toString();
	}

	// TODO Exercise 2.c
	private class MovieCounter implements Comparable<MovieCounter> {

		private String name;
		private int count;

		public MovieCounter(String name) {
			this.name = name;
			this.count = 1;
		}

		@Override
		public String toString() {
			return this.name + " : " + this.count;
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof MovieCounter) {
				return this.count == ((MovieCounter) object).count;
			} else {
				return false;
			}
		}

		@Override
		public int compareTo(MovieCounter other) {
			return (this.count + this.name).compareTo(other.count + other.name);
		}

	}

	// TODO Exercise 2.c
	public String getTopThreeMovies(int year) {
		HashMap<String, MovieCounter> map = new HashMap<>();

		for (Nominee nominee : this.nominees) {
			if (nominee.getYear() == year) {
				String name = nominee.getMovie();

				if (map.containsKey(name)) {
					map.get(name).count++;
				} else {
					map.put(name, new MovieCounter(name));
				}
			}
		}

		Collection<MovieCounter> counters = map.values();
		MovieCounter[] array = new MovieCounter[counters.size()];
		counters.toArray(array);
		Arrays.sort(array);

		StringBuffer topThree = new StringBuffer();

		int j = array.length - 1;
		for (int i = 0; i < 3; i++) {
			if (j >= 0) {
				topThree.append(array[j] + "\n");
				j--;
			}
		}

		int count = array[j + 1].count;

		while (j >= 0 && array[j].count == count) {
			topThree.append(array[j] + "\n");
			j--;
		}

		return topThree.toString();
	}

	// TODO Exercise 2.d
	public String filterActors(Month month, int day) {
		List<Actor> list = new ArrayList<>();

		for (Actor actor : this.actors) {
			LocalDate birth = actor.getBirth();

			if (day != 0) {
				if (birth.getMonth().equals(month) && birth.getDayOfMonth() == day) {
					list.add(actor);
				}
			} else {
				if (birth.getMonth().equals(month)) {
					list.add(actor);
				}
			}
		}

		Collections.sort(list, new Comparator<Actor>() {
			@Override
			public int compare(Actor actorOne, Actor actorTwo) {
				return actorOne.getBirth().getYear() - actorTwo.getBirth().getYear();
			}
		});

		StringBuffer buffer = new StringBuffer();

		for (Actor actor : list) {
			buffer.append(actor + "\n");
		}

		return buffer.toString();
	}

	public static void main(String[] args) {
		FilterAcademyAwards faw = new FilterAcademyAwards();
		// System.out.println(faw.getWinners(Category.ACTRESS_LR));
		// System.out.println(faw.getTopThreeMovies(2010));
		System.out.println(faw.filterActors(Month.MARCH, 20));
	}

}
