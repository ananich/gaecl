package com.inpun.json;

/**
 * @author anton.ananich@inpun.com
 */
public class UnsexpectedCharacterException extends ParseException {

	public UnsexpectedCharacterException(char unexpected) {
		super(String.format("Unexpected character ('%s')", unexpected));
		super._char = -1;
	}
	
	public UnsexpectedCharacterException(int position, char unexpected) {
		super(String.format("Unexpected character ('%s') at position %d.", unexpected, position));
		super._char = unexpected;
	}
}
