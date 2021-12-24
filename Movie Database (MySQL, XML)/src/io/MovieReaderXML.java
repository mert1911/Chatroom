package io;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class MovieReaderXML extends MovieReader {

	public MovieReaderXML(File file) {
		super(file);
	}

	/**
	 * Nutzt die verschiedenen ContentHandler, um aus der XML-Datei Informationen zu
	 * extrahieren und Objekte zu erstellen und speichert diese in den HashMaps.
	 */
	@Override
	public void parseFile() {
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			FileReader reader = new FileReader(super.file);
			InputSource inputSource = new InputSource(reader);
			// optional: DTD
			// inputSource.setSystemId(Parameter.resourcesPath + "movie.dtd");

			// MovieContentHandler - MovieElemente einlesen
			MovieContentHandler handler1 = new MovieContentHandler();
			xmlReader.setContentHandler(handler1);
			xmlReader.parse(inputSource);

			// InformationContentHandler - Stream neu oeffnen und InformationElemente
			// einlesen
			reader = new FileReader(super.file);
			inputSource = new InputSource(reader);
			InformationContentHandler handler2 = new InformationContentHandler();
			xmlReader.setContentHandler(handler2);
			xmlReader.parse(inputSource);

			// RatingContentHandler - Stream neu oeffnen und RatingElemente einlesen
			reader = new FileReader(super.file);
			inputSource = new InputSource(reader);
			RatingContentHandler handler3 = new RatingContentHandler();
			xmlReader.setContentHandler(handler3);
			xmlReader.parse(inputSource);

			super.movieMap = handler1.getMovieHashMap();
			super.informationMap = handler2.getInformationHashMap();
			super.ratingMap = handler3.getRatingHashMap();
			super.getReaderInfo();
		} catch (IOException | SAXException e) {
			e.printStackTrace();
		}
	}
}
