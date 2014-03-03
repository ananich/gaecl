package com.inpun.json;

/**
 * @author anton.ananich@inpun.com
 */
public class UnknownError extends ParseException {
	
	public UnknownError(int position) {
		super(String.format("Unkown error at position %d.", position));
		super._char = position;
	}
}
