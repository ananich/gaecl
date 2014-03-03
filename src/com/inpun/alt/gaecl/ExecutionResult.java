package com.inpun.alt.gaecl;

import com.inpun.json.JsonArray;
import com.inpun.json.JsonObject;

public class ExecutionResult {
	private int error;
	private String message;
	private JsonObject object;
	private JsonArray array;

	public ExecutionResult(int error) {
		this.error = error;
	}

	public ExecutionResult() {
		this(0);
	}

	public ExecutionResult(int i, String message) {
		this(i);
		this.message = message;
	}

	public ExecutionResult(int i, JsonObject object) {
		this(i);
		this.object = object;
	}
	
	public ExecutionResult(int i, JsonArray array) {
		this(i);
		this.array = array;
	}

	public int getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public JsonObject getObject() {
		return object;
	}

	public JsonArray getArray() {
		return array;
	}

}
