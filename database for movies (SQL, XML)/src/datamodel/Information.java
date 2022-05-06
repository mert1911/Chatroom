package datamodel;

import exceptions.MissingValueException;

import java.util.ArrayList;
import java.util.Iterator;

public class Information {
    private final String id;                  // eindeutige IMDb-ID, Beispiel: tt0417741
    private final String description;         // kurze Beschreibung der Handlung, teilweise unbekannt
    private final ArrayList<String> actors;   // Liste mit beteiligten Schauspielern, teilweise unbekannt
    private final String director;            // Produzent, teilweise unbekannt
    private final String prodCompany;         // Produktions-Gesellschaft, teilweise unbekannt

    public Information(String id, String description, ArrayList<String> actors, String director, String prodCompany) {
        this.id = id;
        this.description = description;
        this.actors = actors;
        this.director = director;
        this.prodCompany = prodCompany;
    }

    public String getId() {
        return id;
    }

    public String getDescription() throws MissingValueException {
        if (description == null) {
            throw new MissingValueException("No description-data available for movie: " + id);
        }
        return description;
    }

    public ArrayList<String> getActors() throws MissingValueException {
        if (actors == null) {
            throw new MissingValueException("No actors-data available for movie: " + id);
        }
        return actors;
    }

    public String getDirector() throws MissingValueException {
        if (director == null) {
            throw new MissingValueException("No director-data available for movie: " + id);
        }
        return director;
    }

    public String getProdCompany() throws MissingValueException {
        if (prodCompany == null) {
            throw new MissingValueException("No actors-data available for movie: " + id);
        }
        return prodCompany;
    }

    public String getActorsString() throws MissingValueException {
        if (actors == null) {
            throw new MissingValueException("No actors-data available for movie: " + id);
        }
        StringBuilder stringBuffer = new StringBuilder();
        Iterator<String> iterator = this.actors.iterator();
        stringBuffer.append(iterator.next());
        while (iterator.hasNext()) {
            stringBuffer.append(", ").append(iterator.next());
        }
        return stringBuffer.toString();
    }
}
