package io;

import datamodel.Genre;
import datamodel.Movie;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;

public class MovieContentHandler implements ContentHandler {

	// HashMap fuer erstellte Movie-Objekte
	private HashMap<String, Movie> movieMap = new HashMap<String, Movie>(85855);

	private Movie movie;
	private String id;
	private String title;
	private String originalTitle;
	private int year;
	private ArrayList<Genre> genres;
	private int duration;
	private ArrayList<String> countries;

	public HashMap<String, Movie> getMovieHashMap() {
		return this.movieMap;
	}

	// TODO Exercise 2.b
	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.equals("Movie")) {
			this.id = atts.getValue("id");
			this.title = atts.getValue("title");
			this.originalTitle = atts.getValue("originalTitle");
			this.year = Integer.parseInt(atts.getValue("year"));
			String[] genresArray = atts.getValue("genres").split(",");
			this.genres = this.getGenres(genresArray);
			this.duration = Integer.parseInt(atts.getValue("duration"));
			String[] countriesArray = atts.getValue("countries").split(", ");
			this.countries = this.getCountries(countriesArray);
		}
	}

	// TODO Exercise 2.b
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals("Movie")) {
			this.movie = new Movie(id, title, originalTitle, year, genres, duration, countries);
			this.movieMap.put(this.id, this.movie);
		}
	}

	/**
	 * Hilfsmethode. Gibt eine ArrayList mit Genre-Objekten zurueck, basierend auf
	 * dem uebergebenen String-Array. Dabei werden eventuelle whitespaces
	 * eliminiert.
	 */
	private ArrayList<Genre> getGenres(String[] genres) {
		ArrayList<Genre> genreArrayList = new ArrayList<Genre>();
		for (String s : genres) {
			s = s.toUpperCase().replaceAll("\\s+", "");
			for (Genre g : Genre.values()) {
				if (g.toString().equals(s)) {
					genreArrayList.add(g);
				}
			}
		}
		return genreArrayList;
	}

	/**
	 * Hilfsmethode. Gibt eine ArrayList mit Country-Strings zurueck, basierend auf
	 * dem uebergebenen String-Array. Dabei werden eventuelle whitespaces
	 * eliminiert.
	 */
	private ArrayList<String> getCountries(String[] countries) {
		ArrayList<String> countriesArrayList = new ArrayList<>();
		for (String s : countries) {
			String country = s.replaceAll("\\s+", "");
			countriesArrayList.add(country);
		}
		return countriesArrayList;
	}

	/* ********************************************************************* */
	/* Rest wird nicht benoetigt */
	/* ********************************************************************* */

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
	}

	@Override
	public void setDocumentLocator(Locator locator) {

	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {

	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {

	}

	@Override
	public void skippedEntity(String name) throws SAXException {

	}
}
