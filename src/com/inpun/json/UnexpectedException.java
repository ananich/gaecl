package com.inpun.json;

/**
 * @author anton.ananich@inpun.com
 */
public class UnexpectedException extends ParseException {
	
	public UnexpectedException(Exception unexpected) {
		super(String.format("Unexpected exception: %s", unexpected));
		super._char = -1;
	}
	
	public UnexpectedException(int position, Exception unexpected) {
		super(String.format("Unexpected exception at position %d: %s", position, unexpected));
		super._char = position;
	}
}
