package io;

import datamodel.*;
import general.Parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MovieReaderCSV extends MovieReader {

    public MovieReaderCSV(File file) {
        super(file);
    }

    public static void main(String[] args) {
        MovieReaderCSV csvR = new MovieReaderCSV(new File(Parameters.csvPath));
        csvR.parseFile();
    }

    /**
     * Liest Datei zeilenweise ein und uebergibt einzelne Zeile an parseLine-Methode.
     */
    @Override
    public void parseFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file))) {
            String line;
            bufferedReader.readLine(); // erste Zeile ueberspringen, da nur Ueberschrift

            while ((line = bufferedReader.readLine()) != null) {
                // Falls eingelesene Zeilen ausgegeben werden sollen
                // System.out.println(line);
                parseLine(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(this.movieMap.size() + " Movies processed.");
        System.out.println(this.informationMap.size() + " Informations processed.");
        System.out.println(this.ratingMap.size() + " Ratings processed");
    }

    /**
     * Extrahiert aus der uebergebenen CSV-Zeile saemtliche Daten, erstellt entsprechende
     * Objekte und speichert diese in den HashMaps.
     */
    public void parseLine(String line) {
        String[] tokens = getTokens(line);
        String id = tokens[0];
        String title = tokens[1];
        String originalTitle = tokens[2];
        int year = Integer.parseInt(tokens[3]);
        String[] genres = tokens[4].split(", ");
        ArrayList<Genre> genreArrayList = this.getGenres(genres);
        int duration = Integer.parseInt(tokens[5]);
        String[] countries = tokens[6].split(", ");
        ArrayList<String> countriesArrayList = new ArrayList<String>();
        for (String country : countries) {
            if (!countriesArrayList.contains(country)) {
                countriesArrayList.add(country);
            }
        }
        String director = tokens[7];
        String companies = tokens[8];
        String[] actors = tokens[9].split(", ");
        ArrayList<String> actorsArrayList = new ArrayList<String>(Arrays.asList(actors));
        String description = tokens[10];
        float avgVote = Float.parseFloat(tokens[11]);
        int numRatings = Integer.parseInt(tokens[12]);

        Movie movie = new Movie(id, title, originalTitle, year, genreArrayList, duration, countriesArrayList);
        Information information = new Information(id, description, actorsArrayList, director, companies);
        Rating rating = new Rating(id, avgVote, numRatings);

        super.movieMap.put(id, movie);
        super.informationMap.put(id, information);
        super.ratingMap.put(id, rating);
    }

    /**
     * Teilt eine CSV-Zeile in je ein Token pro Spalte, beseitigt zusaetzliche Anfuehrungszeichen.
     */
    public String[] getTokens(String line) {
        return line.replaceAll("^\"|\"$", "").split("\",\"");
    }

    /**
     * Hilfsmethode, erstellt eine ArrayList mit Genre-Objekten aus dem uebergebenen String.
     * Wird verwendet bei Objekterstellung aus Datenbank-Ergebnis.
     */
    public ArrayList<Genre> getGenres(String[] genres) {
        ArrayList<Genre> genreArrayList = new ArrayList<Genre>();
        for (String s : genres) {
            s = s.toUpperCase();
            for (Genre g : Genre.values()) {
                if (g.toString().equals(s)) {
                    genreArrayList.add(g);
                }
            }
        }
        return genreArrayList;
    }
}
