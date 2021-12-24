package model;

// TODO Exercise 1.a
public class Nominee implements Comparable<Nominee> {

	private Category category;
	private int year;
	private String movie;
	private String character;
	private boolean won;
	private Actor actor;

	public Nominee(Category c, int y, String movie, String character, boolean wonP, Actor a) {
		this.category = c;
		this.year = y;
		this.movie = movie;
		this.character = character;
		this.won = wonP;
		this.actor = a;
	}

	public Category getCategory() {
		return this.category;
	}

	public int getYear() {
		return this.year;
	}

	public String getMovie() {
		return this.movie;
	}

	public String getCharacter() {
		return this.character;
	}

	public boolean isWon() {
		return this.won;
	}

	public Actor getActor() {
		return this.actor;
	}

	// TODO Exercise 1.a
	@Override
	public boolean equals(Object object) {
		if (object instanceof Nominee) {
			return this.getActor().equals(((Nominee) object).getActor());
		} else {
			return false;
		}
	}

	// TODO Exercise 1.a
	@Override
	public int compareTo(Nominee other) {
		if (this.movie.equals(other.movie)) {
			return this.actor.compareTo(other.actor);
		} else {
			return this.movie.compareTo(other.movie);
		}
	}

}
