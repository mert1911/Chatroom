package datamodel;

import exceptions.MissingValueException;

import java.util.ArrayList;
import java.util.Iterator;

public class Movie {
    private final String id;                      // eindeutige IMDb-ID, Beispiel: tt0417741
    private final String title;                   // Titel des Films, evtl. in Fremdsprache
    private final String originalTitle;           // Original-Titel des Films
    private final int year;                       // Erscheinungsjahr des Films
    private final ArrayList<Genre> genres;        // Liste mit Genren des Films, teilweise unbekannt
    private final int duration;                   // Laenge des Films
    private final ArrayList<String> countries;    // Liste mit Laendern, in denen der Film gedreht wurde, teilweise unbekannt

    public Movie(String id, String title, String originalTitle, int year, ArrayList<Genre> genres, int length, ArrayList<String> countries) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.year = year;
        this.genres = genres;
        this.duration = length;
        this.countries = countries;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Genre> getGenres() throws MissingValueException {
        if (genres == null) {
            throw new MissingValueException("No genre-data available for movie: " + id);
        }
        return genres;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<String> getCountries() throws MissingValueException {
        if (countries == null) {
            throw new MissingValueException("No countries-data available for movie: " + id);
        }
        return countries;
    }

    public String getGenreString() throws MissingValueException {
        if (genres == null) {
            throw new MissingValueException("No genre-data available for movie: " + id);
        }
        StringBuilder stringBuffer = new StringBuilder();
        Iterator<Genre> iterator = this.genres.iterator();
        if (iterator.hasNext()) {
            stringBuffer.append(iterator.next().toString().toLowerCase());
            while (iterator.hasNext()) {
                stringBuffer.append(", ").append(iterator.next().toString().toLowerCase());
            }
        }
        return stringBuffer.toString();
    }

    public String getCountryString() throws MissingValueException {
        if (countries == null) {
            throw new MissingValueException("No countries-data available for movie: " + id);
        }
        StringBuilder stringBuffer = new StringBuilder();
        Iterator<String> iterator = this.countries.iterator();
        stringBuffer.append(iterator.next());
        while (iterator.hasNext()) {
            stringBuffer.append(", ").append(iterator.next());
        }
        return stringBuffer.toString();
    }
}
