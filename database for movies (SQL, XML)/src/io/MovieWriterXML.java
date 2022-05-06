package io;

import datamodel.Information;
import datamodel.Movie;
import datamodel.Rating;
import exceptions.MissingValueException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class MovieWriterXML extends MovieWriter {

	// TODO Exercise 2.a
	private Element createMovieElement(Movie movie, Rating rating, Information information) {
		Element movieElement = new Element("Movie");
		Element informationElement = new Element("Information");
		Element ratingElement = new Element("Rating");

		try {
			ratingElement.setAttribute("id", movie.getId());
			ratingElement.setAttribute("rating", String.valueOf(rating.getRating()));
			ratingElement.setAttribute("numRatings", String.valueOf(rating.getNumRatings()));

			informationElement.setAttribute("id", movie.getId());
			informationElement.setAttribute("description", information.getDescription());
			informationElement.setAttribute("actors", information.getActorsString());
			informationElement.setAttribute("director", information.getDirector());
			informationElement.setAttribute("prodCompany", information.getProdCompany());

			movieElement.setAttribute("id", movie.getId());
			movieElement.setAttribute("title", movie.getTitle());
			movieElement.setAttribute("originalTitle", movie.getOriginalTitle());
			movieElement.setAttribute("year", String.valueOf(movie.getYear()));
			movieElement.setAttribute("genres", movie.getGenreString());
			movieElement.setAttribute("duration", String.valueOf(movie.getDuration()));
			movieElement.setAttribute("countries", movie.getCountryString());

			movieElement.addContent(ratingElement);
			movieElement.addContent(informationElement);
		} catch (MissingValueException missingValueException) {
			missingValueException.printStackTrace();
		}

		return movieElement;
	}

	public MovieWriterXML(File file, Collection<Movie> movies, HashMap<String, Information> information,
			HashMap<String, Rating> ratings) {
		super(file, movies, information, ratings);
	}

	/**
	 * Erstellt ein vollstaendiges XML-Dokument aus den Informationen, die in den
	 * Collections der Oberklasse gespeichert sind.
	 */
	@Override
	public void writeFile() {
		Element root = new Element("MovieSelection");
		Document document = new Document(root);

		for (Movie movie : super.movieCollection) {
			Rating rating = super.ratingHashMap.get(movie.getId());
			Information information = super.informationHashMap.get(movie.getId());
			Element movieElement = this.createMovieElement(movie, rating, information);
			root.addContent(movieElement);
		}
		this.writeDoc(document);
	}

	/**
	 * Schreibt das fertige XML-Dokument in eine Datei.
	 */
	private void writeDoc(Document doc) {
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			FileWriter fw = new FileWriter(this.file);
			out.output(doc, fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
