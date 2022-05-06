package io;

import datamodel.Information;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InformationContentHandler implements ContentHandler {

	// HashMap fuer erstellte Information-Objekte
	private HashMap<String, Information> informationHashMap = new HashMap<String, Information>(85855);

	private Information information;
	private String id;
	private String description;
	private ArrayList<String> actors;
	private String director;
	private String prodCompany;

	public HashMap<String, Information> getInformationHashMap() {
		return informationHashMap;
	}

	// TODO Exercise 2.b
	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.equals("Information")) {
			this.id = atts.getValue("id");
			this.description = atts.getValue("description");
			this.actors = new ArrayList<String>();
			this.actors.addAll(Arrays.asList(atts.getValue("actors").split(", ")));
			this.director = atts.getValue("director");
			this.prodCompany = atts.getValue("prodCompany");
		}
	}

	// TODO Exercise 2.b
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals("Information")) {
			this.information = new Information(id, description, actors, director, prodCompany);
			this.informationHashMap.put(this.id, this.information);
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
