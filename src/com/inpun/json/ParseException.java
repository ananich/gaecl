package com.inpun.json;

/**
 * ParseException explains why and where the error occurs in source JSON text.
 * 
 * @author anton.ananich@inpun.com
 */
abstract public class ParseException extends Exception {
	
	protected int _char, _line, _column;
	
	public ParseException() {
	}

	public ParseException(String message) {
		super(message);
	}
	
	public ParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @return The character position (starting with 0) of the input where the error occurs.
	 */
	public int getPosition() {
		return _char;
	}
}
