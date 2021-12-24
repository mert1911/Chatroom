package gui;

import database.MovieDBManager;
import datamodel.Genre;
import datamodel.Information;
import datamodel.Movie;
import datamodel.Rating;
import exceptions.MissingValueException;
import io.MovieReader;
import io.MovieReaderXML;
import io.MovieWriter;
import io.MovieWriterXML;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class MovieGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private final MovieDBManager manager = new MovieDBManager();

    //Text-Areas, Labels und Comboboxes
    private final JTextArea textArea = new JTextArea();
    private final JTextArea yearMax = new JTextArea();
    private final JTextArea yearMin = new JTextArea();
    private final JTextArea durationMax = new JTextArea();
    private final JTextArea durationMin = new JTextArea();
    private final JTextArea ratingMax = new JTextArea();
    private final JTextArea ratingMin = new JTextArea();
    private final JTextArea votesMax = new JTextArea();
    private final JTextArea votesMin = new JTextArea();
    private JComboBox<String> genres;
    private JComboBox<String> countries = new JComboBox<String>();
    private JComboBox<String> directors = new JComboBox<String>();
    //private JComboBox<String> actors = new JComboBox<String>();
    private JTextField actorField = new JTextField(25);
    private final JLabel searchResultsLabel = new JLabel("Number of Search-Results: 0");
    private final JLabel processingLabel = new JLabel("Status: Ready for Query..");

    //Collections fuer Objekte
    private Collection<Movie> movieSelection = new HashSet<Movie>();
    private HashMap<String, Information> informationHashMap = new HashMap<>();
    private HashMap<String, Rating> ratingHashMap = new HashMap<>();

    //Collections fuer zusaetzliche Film-information
    private HashMap<String, String> descriptionsHash = new HashMap<>();
    private HashMap<String, String> actorsHash = new HashMap<>();
    private HashMap<String, String> directorsHash = new HashMap<>();
    private HashMap<String, String> companiesHash = new HashMap<>();

    //Main-Panels fuer Eingabe und Ergebnisse
    private final JPanel controlPanel = new JPanel();
    private final JPanel tablePanel = new JPanel();
    private JTable jtable;

    public MovieGUI() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.createMenu();
        this.createComponents();
        this.addWindowListener(new WindowAdapter(){
        	public void windowClosing(WindowEvent e){
        		System.out.println("Disconnect from database!");
        		manager.disconnect();
        		System.exit(0);
        	}
        });
        this.pack();
    }

    public static void main(String[] args) {
        MovieGUI gui = new MovieGUI();
        System.out.println("Application ready!");
        gui.setVisible(true);
    }

    private void createMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem xmlRead = new JMenuItem("Import XML File");
        xmlRead.addActionListener(new MenuActionListener(true));
        JMenuItem xmlWrite = new JMenuItem("Export XML File");
        xmlWrite.addActionListener(new MenuActionListener(false));
        fileMenu.add(xmlRead);
        fileMenu.add(xmlWrite);
        JMenuBar menubar = new JMenuBar();
        menubar.add(fileMenu);
        this.setJMenuBar(menubar);
    }

    private class MenuActionListener implements ActionListener {
        private final boolean read;

        public MenuActionListener(boolean read) {
            this.read = read;
        }

        public void actionPerformed(ActionEvent e) {
            if (read) {
                MovieGUI.this.readXML();
            } else {
                MovieGUI.this.exportXML();
            }
        }

    }

    private void createComponents() {
        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        createControlPanel();
        container.add(controlPanel);
        createTablePanel();
        container.add(tablePanel);
    }

    private void createControlPanel() {
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        JButton infoButton = new JButton("Show Information");
        JButton helpButton = new JButton("Help");
        JLabel year = new JLabel("YEAR");
        JLabel genre = new JLabel("GENRE");
        JLabel country = new JLabel("COUNTRY");
        JLabel director = new JLabel("DIRECTOR");
        JLabel actor = new JLabel("ACTOR");
        JLabel duration = new JLabel("DURATION (in min)");
        JLabel rating = new JLabel("RATING");
        JLabel votes = new JLabel("NUMBER OF VOTES");
        JLabel from = new JLabel("from: ");
        JLabel from2 = new JLabel("min: ");
        JLabel from3 = new JLabel("min: ");
        JLabel from4 = new JLabel("min: ");
        JLabel to = new JLabel("to: ");
        JLabel to2 = new JLabel("max: ");
        JLabel to3 = new JLabel("max: ");
        JLabel to4 = new JLabel("max: ");
        JLabel searchLabel = new JLabel("Search for a title or an imdb-id: ");
        String[] genresArray = {"", "ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY", "CRIME", "DRAMA", "FAMILY", "FANTASY", "FILMNOIR", "HISTORY", "HORROR", "MUSIC", "MUSICAL", "MYSTERY", "ROMANCE", "SCIFI", "THRILLER", "WAR", "WESTERN"};
        genres = new JComboBox<String>(genresArray);
        System.out.println("Fetching Data from Database for ComboBoxes. Please wait...");
        try {
        	Collection<String>  countryColl = manager.getCountries();
        	if (countryColl != null){
        		this.fillComboBox(countries, new Vector<String>(countryColl));
        	}
            Collection<String>  directorsColl = manager.getDirectors();
            if (directorsColl != null){
            	this.fillComboBox(directors, new Vector<String>(directorsColl));
            }
            //this.fillComboBox(actors, new Vector<String>(manager.getActors()));
            System.out.println("Successfully retrieved Data from Database for ComboBoxes.");
        } catch (SQLException e) {
            System.out.println("FAILED TO RETRIEVE DATA FROM DATABASE. ");
            countries = new JComboBox<>(new String[]{""});
            directors = new JComboBox<>(new String[]{""});
            //actors = new JComboBox<>(new String[]{""});
            MovieGUI.this.processingLabel.setText("Status: ERROR WITH DATABASE CONNECTION. NO QUERIES POSSIBLE.");

        }

        System.out.println("Building GUI. Please wait... ");
        this.controlPanel.setLayout(new BoxLayout(this.controlPanel, BoxLayout.PAGE_AXIS));
        this.controlPanel.add(searchLabel);
        this.controlPanel.add(textArea);

        JPanel controlPanel1 = new JPanel();
        controlPanel1.setLayout(new BoxLayout(controlPanel1, BoxLayout.LINE_AXIS));
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.PAGE_AXIS));
        yearPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        yearPanel.add(year);
        yearPanel.add(from);
        yearPanel.add(yearMin);
        yearPanel.add(to);
        yearPanel.add(yearMax);
        controlPanel1.add(yearPanel);
        JPanel durationPanel = new JPanel();
        durationPanel.setLayout(new BoxLayout(durationPanel, BoxLayout.PAGE_AXIS));
        durationPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        durationPanel.add(duration);
        durationPanel.add(from2);
        durationPanel.add(durationMin);
        durationPanel.add(to2);
        durationPanel.add(durationMax);
        controlPanel1.add(durationPanel);
        JPanel ratingPanel = new JPanel();
        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.PAGE_AXIS));
        ratingPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        ratingPanel.add(rating);
        ratingPanel.add(from3);
        ratingPanel.add(ratingMin);
        ratingPanel.add(to3);
        ratingPanel.add(ratingMax);
        controlPanel1.add(ratingPanel);
        JPanel votesPanel = new JPanel();
        votesPanel.setLayout(new BoxLayout(votesPanel, BoxLayout.PAGE_AXIS));
        votesPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        votesPanel.add(votes);
        votesPanel.add(from4);
        votesPanel.add(votesMin);
        votesPanel.add(to4);
        votesPanel.add(votesMax);
        controlPanel1.add(votesPanel);

        JPanel buttonPanel = new JPanel();
        JPanel buttonPanel1 = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        JPanel buttonPanel3 = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel1.setLayout(new BoxLayout(buttonPanel1, BoxLayout.LINE_AXIS));
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.LINE_AXIS));
        buttonPanel3.setLayout(new BoxLayout(buttonPanel3, BoxLayout.LINE_AXIS));
        clearButton.addActionListener(clearAction);
        searchButton.addActionListener(searchAction);
        infoButton.addActionListener(infoAction);
        helpButton.addActionListener(helpAction);
        buttonPanel1.add(searchButton);
        buttonPanel1.add(clearButton);
        buttonPanel1.add(infoButton);
        buttonPanel1.add(helpButton);
        buttonPanel2.add(searchResultsLabel);
        buttonPanel3.add(processingLabel);
        buttonPanel.add(buttonPanel1);
        buttonPanel.add(buttonPanel2);
        buttonPanel.add(buttonPanel3);
        controlPanel1.add(buttonPanel);

        JPanel controlPanel2 = new JPanel();
        controlPanel2.setLayout(new BoxLayout(controlPanel2, BoxLayout.LINE_AXIS));
        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.PAGE_AXIS));
        genrePanel.add(genre);
        genrePanel.add(genres);
        controlPanel2.add(genrePanel);
        JPanel countryPanel = new JPanel();
        countryPanel.setLayout(new BoxLayout(countryPanel, BoxLayout.PAGE_AXIS));
        countryPanel.add(country);
        countryPanel.add(countries);
        controlPanel2.add(countryPanel);
        JPanel directorPanel = new JPanel();
        directorPanel.setLayout(new BoxLayout(directorPanel, BoxLayout.PAGE_AXIS));
        directorPanel.add(director);
        directorPanel.add(directors);
        controlPanel2.add(directorPanel);
        JPanel actorsPanel = new JPanel();
        actorsPanel.setLayout(new BoxLayout(actorsPanel, BoxLayout.PAGE_AXIS));
        actorsPanel.add(actor);
        //actorsPanel.add(actors);
        actorsPanel.add(actorField);
        controlPanel2.add(actorsPanel);

        //add others to main controlPanel
        this.controlPanel.add(controlPanel1);
        this.controlPanel.add(controlPanel2);
        this.controlPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void createTablePanel() {
        this.searchResultsLabel.setText("Number of results found in database: 0");
        String[] colomnNames = {"imdb_id", "title", "originalTitle", "year", "genre", "duration", "country", "votes", "rating"};
        String[][] data = {};
        JTable table = new JTable(data, colomnNames);
        this.jtable = table;
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(1200, 300));
        JScrollPane jScrollPane = new JScrollPane(table);
        this.tablePanel.removeAll();
        this.tablePanel.add(jScrollPane);
        table.getTableHeader().setBackground(Color.WHITE);
        this.tablePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.tablePanel.validate();
        this.tablePanel.repaint();
    }

    /**
     * Aktualisiert das tablePanel mit der uebergebenen JTable.
     */
    private void updateTable(JTable newTable) {
        this.tablePanel.removeAll();
        newTable.setAutoCreateRowSorter(true);
        this.jtable = newTable;
        JScrollPane jScrollPane = new JScrollPane(newTable);
        this.tablePanel.add(jScrollPane);
        newTable.getTableHeader().setBackground(Color.WHITE);
        newTable.setPreferredScrollableViewportSize(new Dimension(1200, 300));
        this.tablePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.tablePanel.validate();
        this.tablePanel.repaint();
    }

    /**
     * Liest die Nutzereingaben aus und stellt eine Anfrage an die Datenbank
     * basierend auf den eingegebenen Daten. Bei Bedarf werden default-Werte gesetzt.
     * Anschliessend werden die Resultsets an handleResult-Methode zur Verarbeitung gegeben.
     */
    ActionListener searchAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MovieGUI.this.processingLabel.setText("Status: Processing Query, please wait...");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    String queryText = textArea.getText().equals("") ? "%" : "%" + textArea.getText() + "%";
                    //String queryActors = actors.getSelectedItem().toString().equals("") ? "%" : "%" + actors.getSelectedItem().toString() + "%";
                    String queryActors = actorField.getText().trim().equals("") ? "%" : "%" + actorField.getText().trim() + "%";
                    String queryGenre = genres.getSelectedItem().toString().equals("") ? "%" : "%" + genres.getSelectedItem().toString() + "%";
                    String queryCountry = countries.getSelectedItem().toString().equals("") ? "%" : "%" + countries.getSelectedItem().toString() + "%";
                    String queryDirector = directors.getSelectedItem().toString().equals("") ? "%" : "%" + directors.getSelectedItem().toString() + "%";

                    int yearMa = yearMax.getText().matches("\\d\\d\\d\\d") ? Integer.parseInt(yearMax.getText()) : 4000;
                    int yearMi = yearMin.getText().matches("\\d\\d\\d\\d") ? Integer.parseInt(yearMin.getText()) : 0;
                    int durationMa = durationMax.getText().matches("\\d+") ? Integer.parseInt(durationMax.getText()) : 99999999;
                    int durationMi = durationMin.getText().matches("\\d+") ? Integer.parseInt(durationMin.getText()) : 0;
                    float ratingMa = ratingMax.getText().matches("[+-]?([0-9]*[.])?[0-9]+") ? Float.parseFloat(ratingMax.getText()) : (float) 10.0;
                    float ratingMi = ratingMin.getText().matches("[+-]?([0-9]*[.])?[0-9]+") ? Float.parseFloat(ratingMin.getText()) : (float) 0.0;
                    long votesMa = votesMax.getText().matches("\\d+") ? Long.parseLong(votesMax.getText()) : 9999999999999L;
                    long votesMi = votesMin.getText().matches("\\d+") ? Long.parseLong(votesMin.getText()) : 0L;
                    ResultSet movie = manager.movieQuery(queryText, queryText, queryText, yearMa, yearMi, queryGenre, durationMa, durationMi, queryCountry);
                    ResultSet information = manager.informationQuery(queryActors, queryDirector);
                    ResultSet ratings = manager.ratingQuery(ratingMa, ratingMi, votesMa, votesMi);
                    try {
                        handleResult(movie, ratings, information);
                    } catch (SQLException exception) {
                        exception.printStackTrace();

                    }
                }
            });
        }
    };


    /**
     * Setzt saemtliche Collections zurueck, leert die Nutzereingaben
     * und die Datentabelle.
     */
    ActionListener clearAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            movieSelection = new HashSet<Movie>();
            informationHashMap = new HashMap<>();
            ratingHashMap = new HashMap<>();
            descriptionsHash = new HashMap<>();
            actorsHash = new HashMap<>();
            directorsHash = new HashMap<>();
            companiesHash = new HashMap<>();

            textArea.setText("");
            yearMax.setText("");
            yearMin.setText("");
            durationMax.setText("");
            durationMin.setText("");
            ratingMax.setText("");
            ratingMin.setText("");
            votesMax.setText("");
            votesMin.setText("");
            genres.setSelectedItem("");
            countries.setSelectedItem("");
            directors.setSelectedItem("");
            //actors.setSelectedItem("");
            actorField.setText("");
            MovieGUI.this.createTablePanel();

        }
    };


    /**
     * Sammelt Informationen aus den Hashs fuer den aktuell ausgewaehlten
     * Film in der Datentabelle und zeigt diese in einem neuen Fenster an.
     */
    ActionListener infoAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (jtable != null && jtable.getSelectedRow() != -1) {
                int row = jtable.getSelectedRow();
                String id = (String) jtable.getValueAt(row, 0);
                String name = (String) jtable.getValueAt(row, 2);
                String description = descriptionsHash.get(id);
                String actors = actorsHash.get(id);
                String director = directorsHash.get(id);
                String prodCompany = companiesHash.get(id);
                String content = "DESCRIPTION: " + description + "\n \n" + "ACTORS: " + actors + "\n \n" + "DIRECTOR: " + director + "\n \n" + "PRODUCTION COMPANY: " + prodCompany;

                JFrame infoFrame = new JFrame(name);
                infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                infoFrame.setSize(200, 500);
                JTextArea infoArea = new JTextArea();
                infoArea.setColumns(100);
                infoArea.setRows(20);
                infoArea.setLineWrap(true);
                infoArea.setWrapStyleWord(true);
                infoArea.setText(content);
                infoArea.setEditable(false);
                infoArea.setAutoscrolls(true);
                infoFrame.add(infoArea);
                infoFrame.pack();
                infoFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "You have not selected a Movie.");
            }
        }
    };


    /**
     * Zeigt ein Hilfe-Fenster an, welches Informationen zur Bedienung enthaelt.
     */
    ActionListener helpAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String content = "Format-examples: YEAR: 2007  DURATION: 120  RATING: 7.2  NUMBER OF VOTES: 10000 \n \n" +
                    "An input, which is in a wrong format, is handled as an empty input. \n \n" +
                    "Leave a min/max-field empty to set no limit. \n \n" +
                    "To sort by an attribute, click on the column-heading.";
            JOptionPane.showMessageDialog(null, content, "Help", JOptionPane.INFORMATION_MESSAGE);
        }
    };


    /**
     * Verarbeitet die Resultsets von der Datenbank. Setzt saemtliche Objekt- und Daten-Collections zurueck,
     * extrahiert und speichert Filmdaten, konsolidiert bzw. merged die drei Resultsets zu einer Ergebnismenge,
     * erstellt Vectoren mit Daten fuer die Datentabelle, erstellt Objekte und speichert diese in Collections.
     * Erstellt eine neue Datentabelle und gibt diese an die updateTable-Methode
     */
    private void handleResult(ResultSet moviesResult, ResultSet ratingsResult, ResultSet informationResult) throws SQLException {
        ResultSetMetaData metaData = moviesResult.getMetaData();

        //Zuruecksetzen
        movieSelection = new HashSet<>();
        ratingHashMap = new HashMap<>();
        informationHashMap = new HashMap<>();
        descriptionsHash = new HashMap<>();
        actorsHash = new HashMap<>();
        directorsHash = new HashMap<>();
        companiesHash = new HashMap<>();
        HashMap<String, Float> ratingsHash = new HashMap<>(); //temporaer fuer Objekterstellung und Ergebnis-mergen
        HashMap<String, Integer> votesHash = new HashMap<>(); //temporaer fuer Objekterstellung und Ergebnis-mergen

        //Extrahieren
        while (ratingsResult.next()) {
            ratingsHash.put(ratingsResult.getString("imdb_id"), ratingsResult.getFloat("rating"));
            votesHash.put(ratingsResult.getString("imdb_id"), ratingsResult.getInt("numRatings"));
        }
        while (informationResult.next()) {
            descriptionsHash.put(informationResult.getString("imdb_id"), informationResult.getString("description"));
            actorsHash.put(informationResult.getString("imdb_id"), informationResult.getString("actors"));
            directorsHash.put(informationResult.getString("imdb_id"), informationResult.getString("director"));
            companiesHash.put(informationResult.getString("imdb_id"), informationResult.getString("prodCompany"));
        }

        //Spaltennamen fuer Tabelle
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        columnNames.add("numRatings");
        columnNames.add("rating");

        //Ergebnisse mergen, Vectoren und Objekte erstellen und speichern
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (moviesResult.next()) {
            String id = moviesResult.getString("imdb_id");
            if (ratingsHash.containsKey(id) && directorsHash.containsKey(id)) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(moviesResult.getObject(columnIndex));
                }
                vector.add(votesHash.get(id));
                vector.add(ratingsHash.get(id));
                data.add(vector);

                //Daten sammeln fuer Objekterstellung
                String title = moviesResult.getString("title");
                String originalTitle = moviesResult.getString("originalTitle");
                int year = moviesResult.getInt("year");
                String[] genreStrings = moviesResult.getString("genres").split(", ");
                ArrayList<Genre> genreList = this.getGenres(genreStrings);
                int duration = moviesResult.getInt("duration");
                String[] countryStrings = moviesResult.getString("countries").split(", ");
                ArrayList<String> countryList = new ArrayList<>();
                for (String s : countryStrings) {
                    String country = s.replaceAll("\\s+", "");
                    countryList.add(country);
                }
                String[] actorStrings = actorsHash.get(id).split(", ");
                ArrayList<String> actorsList = new ArrayList<String>();
                Collections.addAll(actorsList, actorStrings);
                String description = descriptionsHash.get(id);
                String director = directorsHash.get(id);
                String prodCompany = companiesHash.get(id);
                float ratings = ratingsHash.get(id);
                int numRatings = votesHash.get(id);

                //Objekterstellung und -speicherung
                Movie movie = new Movie(id, title, originalTitle, year, genreList, duration, countryList);
                Information information = new Information(id, description, actorsList, director, prodCompany);
                Rating rating = new Rating(id, ratings, numRatings);
                movieSelection.add(movie);
                informationHashMap.put(id, information);
                ratingHashMap.put(id, rating);
            }
        }

        this.searchResultsLabel.setText("Number of results found in database: " + data.size());
        this.processingLabel.setText("Status: Ready for Query..");

        //TableModel mit Daten, Spaltennamen und entsprechenden Klassen. Nicht bearbeitbar
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
			private static final long serialVersionUID = 1L;
			final Class<?>[] types = {String.class, String.class, String.class,
                    Integer.class, String.class, Integer.class, String.class, Integer.class, Number.class};

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //Table-Erstellung und -Update
        JTable newTable = new JTable(tableModel);
        updateTable(newTable);
    }

    /**
     * Fuellt die uebergebene JComboBox mit Daten aus dem uebergebenen Vektor
     * und waehlt das erste Element aus.
     */
    private <T> void fillComboBox(JComboBox<T> box, Vector<T> items) {
        if (items != null) {
            box.removeAllItems();
            for (T item : items) {
                box.addItem(item);
            }
            if (box.getItemCount() > 0) {
                box.setSelectedIndex(0);
            }
        }
    }

    /**
     * Nutzt die Objekt-Collections, um mithilfe des xml-Writers eine XML-Datei zu erstellen.
     */
    private void exportXML() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(general.Parameters.resourcesPath));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "XML", "xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            System.out.println("Write XML file: " + f);
            MovieWriter xmlWriter = new MovieWriterXML(f, movieSelection, informationHashMap, ratingHashMap);
            xmlWriter.writeFile();
        }
    }

    /**
     * Liest eine XML-Datei mithilfe des xml-Readers und speichert entsprechende
     * Daten und Objekte in Collections.
     */
    private void readXML() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(general.Parameters.resourcesPath));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "XML", "xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            System.out.println("Read file: " + f);
            MovieReader xmlReader = new MovieReaderXML(f);
            xmlReader.parseFile();

            //Extrahiere und speichere Objekte und deren Daten mithilfe von MovieReader
            this.movieSelection = xmlReader.getMovies();
            this.ratingHashMap = xmlReader.getRating();
            this.informationHashMap = xmlReader.getInformation();
            for (Information information : informationHashMap.values()) {
                try {
                    descriptionsHash.put(information.getId(), information.getDescription());
                    actorsHash.put(information.getId(), information.getActorsString());
                    directorsHash.put(information.getId(), information.getDirector());
                    companiesHash.put(information.getId(), information.getProdCompany());
                } catch (MissingValueException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            this.generateTableFromXML(this.movieSelection, this.ratingHashMap);
        }
    }

    /**
     * Hilfsmethode, erstellt eine ArrayList mit Genre-Objekten aus dem uebergebenen String.
     * Wird verwendet bei Objekterstellung aus Datenbank-Ergebnis.
     */
    private ArrayList<Genre> getGenres(String[] genres) {
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

    /**
     * Nutzt die uebergebenen Collections, um eine entsprechende Datentabelle zu erstellen.
     * Gibt diese Datentabelle an die updateTable-Methode.
     */
    private void generateTableFromXML(Collection<Movie> movieCollection, HashMap<String, Rating> ratingHashMap) {
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        for (Object o : movieCollection) {
            try {
                Vector<Object> vector = new Vector<Object>();
                Movie movie = (Movie) o;
                Rating rating = ratingHashMap.get(movie.getId());
                vector.add(movie.getId());
                vector.add(movie.getTitle());
                vector.add(movie.getOriginalTitle());
                vector.add(movie.getYear());
                vector.add(movie.getGenreString());
                vector.add(movie.getDuration());
                vector.add(movie.getCountryString());
                vector.add(rating.getNumRatings());
                vector.add(rating.getRating());
                data.add(vector);
            } catch (MissingValueException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        Vector<String> columnNames = new Vector<String>();
        columnNames.add("imdb_id");
        columnNames.add("title");
        columnNames.add("originalTitle");
        columnNames.add("year");
        columnNames.add("genres");
        columnNames.add("duration");
        columnNames.add("countries");
        columnNames.add("numRatings");
        columnNames.add("rating");

        this.searchResultsLabel.setText("Number of results found in XML: " + data.size());
        this.processingLabel.setText("Status: Ready for Query..");

        //TableModel mit Daten, Spaltennamen und entsprechenden Klassen. Nicht bearbeitbar
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
			private static final long serialVersionUID = 1L;
			final Class<?>[] types = {String.class, String.class, String.class,
                    Integer.class, String.class, Integer.class, String.class, Integer.class, Number.class};

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //Table-Erstellung und -Update
        JTable newTable = new JTable(tableModel);
        updateTable(newTable);
    }

}
