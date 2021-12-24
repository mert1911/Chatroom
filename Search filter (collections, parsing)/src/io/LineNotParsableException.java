package io;

public class LineNotParsableException extends Exception {

	private static final long serialVersionUID = 1L;

	public LineNotParsableException(String line) {
		super(line);
	}

}
