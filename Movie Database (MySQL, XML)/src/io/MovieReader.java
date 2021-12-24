package io;

import datamodel.Information;
import datamodel.Movie;
import datamodel.Rating;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public abstract class MovieReader {
    protected File file;
    protected HashMap<String, Movie> movieMap;
    protected HashMap<String, Information> informationMap;
    protected HashMap<String, Rating> ratingMap;

    public MovieReader(File file) {
        this.file = file;
        this.movieMap = new HashMap<>();
        this.informationMap = new HashMap<>();
        this.ratingMap = new HashMap<>();
    }

    public abstract void parseFile();

    public Collection<Movie> getMovies() {
        return this.movieMap.values();
    }

    public HashMap<String, Information> getInformation() {
        return this.informationMap;
    }

    public HashMap<String, Rating> getRating() {
        return this.ratingMap;
    }

    public void getReaderInfo() {
        System.out.println(this.movieMap.values().size() + " Movies processed.");
        System.out.println(this.informationMap.values().size() + " Informations processed.");
        System.out.println(this.ratingMap.values().size() + " Ratings processed.");
    }
}
