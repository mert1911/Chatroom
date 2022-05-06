package general;

public class Parameters {
    public static final String separator = System.getProperty("file.separator");
    public static final String homeDirectory = System.getProperty("user.home");
    public static final String projectDirectory = System.getProperty("user.dir"); // Projektverzeichnis
    // TODO
    public static final String dataDirectory = "data"; // Verzeichnis mit den Daten-Dateien (CSV, XML)
    public static final String csvFilename = "movies.csv";
    public static final String csvPath = homeDirectory + separator + dataDirectory + separator + csvFilename; // Pfad zu CSV Datei
    public static final String databaseFile = homeDirectory + separator + dataDirectory + separator + "movies.db"; // Pfad zur Datenbank
    public static final String resourcesPath = projectDirectory + separator + "resources" + separator; // Resources Unterverzeichnis im Projektordner
}
