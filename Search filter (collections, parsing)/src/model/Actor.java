package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// TODO Exercise 1.a
public class Actor implements Comparable<Actor> {

	private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	private String name;
	private LocalDate birth;
	private LocalDate death;

	public Actor(String name) {
		this.name = name;
	}

	public Actor(String name, LocalDate birth) {
		this(name);
		this.birth = birth;
	}

	public Actor(String name, LocalDate birth, LocalDate death) {
		this(name, birth);
		this.death = death;
	}

	// TODO Exercise 1.a
	public String getName() {
		String[] names = this.name.split("\\s+");
		int n = names.length;

		String result = names[n - 1] + ",";
		for (int i = 0; i < n - 1; i++) {
			result += " " + names[i];
		}

		return result;
	}

	public LocalDate getBirth() {
		return this.birth;
	}

	public LocalDate getDeath() {
		return this.death;
	}

	// TODO Exercise 1.a
	@Override
	public String toString() {
		if (this.birth == null) { // birth unknown
			return this.name;
		} else if (this.death != null) { // person already died
			return this.name + " :  *" + dtf.format(this.birth) + ", \u271D " + dtf.format(this.death);
		} else { // person is still alive
			LocalDate now = LocalDate.now();
			int age = now.getYear() - this.birth.getYear();

			if (now.isBefore(this.birth.plusYears(age))) {
				age--;
			}

			return this.name + " (*" + dtf.format(this.birth) + ", age: " + age + ")";
		}
	}

	// TODO Exercise 1.a
	@Override
	public boolean equals(Object object) {
		if (object instanceof Actor) {
			return this.name.equals(((Actor) object).name);
		} else {
			return false;
		}
	}

	// TODO Exercise 1.a
	@Override
	public int compareTo(Actor other) {
		return this.getName().compareTo(other.getName());
	}

}
