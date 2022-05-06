package io;

import datamodel.Information;
import datamodel.Movie;
import datamodel.Rating;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public abstract class MovieWriter {
    protected Collection<Movie> movieCollection;
    protected HashMap<String, Information> informationHashMap;
    protected HashMap<String, Rating> ratingHashMap;
    protected File file;

    public MovieWriter(File file, Collection<Movie> movies, HashMap<String, Information> information, HashMap<String, Rating> ratings) {
        this.file = file;
        this.movieCollection = movies;
        this.informationHashMap = information;
        this.ratingHashMap = ratings;
    }

    public abstract void writeFile();
}
