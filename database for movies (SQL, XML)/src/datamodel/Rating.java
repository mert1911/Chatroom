package datamodel;

public class Rating {
    private final String id;          // eindeutige IMDb-ID, Beispiel: tt0417741
    private final float rating;       // Durchschnittliche IMDb-Bewertung des Films, stand 01.01.2020
    private final long numRatings;    // Anzahl der Bewertungen des Films auf imdb.com, stand 01.01.2020

    public Rating(String id, float rating, long numRatings) {
        this.id = id;
        this.rating = rating;
        this.numRatings = numRatings;
    }

    public String getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public long getNumRatings() {
        return numRatings;
    }
}
