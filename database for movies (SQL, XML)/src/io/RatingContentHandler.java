package io;

import datamodel.Rating;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.HashMap;

public class RatingContentHandler implements ContentHandler {

	// HashMap fuer erstellte Rating-Objekte
	private HashMap<String, Rating> ratingMap = new HashMap<String, Rating>(85855);

	private Rating ratingObject;
	private String id;
	private float rating;
	private long numRatings;

	public HashMap<String, Rating> getRatingHashMap() {
		return ratingMap;
	}

	// TODO Exercise 2.b
	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.equals("Rating")) {
			this.id = atts.getValue("id");
			this.rating = Float.parseFloat(atts.getValue("rating"));
			this.numRatings = Long.parseLong(atts.getValue("numRatings"));
		}
	}

	// TODO Exercise 2.b
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals("Rating")) {
			this.ratingObject = new Rating(id, rating, numRatings);
			this.ratingMap.put(this.id, this.ratingObject);
		}
	}

	/* ********************************************************************* */
	/* Rest wird nicht benoetigt */
	/* ********************************************************************* */

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
	public void characters(char[] ch, int start, int length) throws SAXException {

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
