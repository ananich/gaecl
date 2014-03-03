package com.inpun.json;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A JSON object. Key value pairs are ordered. JSONObject supports java.util.Map interface.
 * 
 * @author anton.ananich@inpun.com
 */
public class JsonObject extends LinkedHashMap<String, Object> {

	public JsonObject() {
	}

	/**
	 * Creates a JSONObject from a Map by copying all the pairs from src (copy constructor)
	 * 
	 * @param map
	 */
	public JsonObject(Map<String, Object> src) {
		super(src);
	}
	
	@Override
	public String toString(){
		return new JsonFormat().format(this);
	}

	public String toPrettyString(){
		return new JsonPrettyFormat().format(this);
	}
	
	public JsonObject getJsonObject(String key) {
		return (JsonObject) super.get(key);
	}

	public void setJsonObject(String key, JsonObject value) {
		super.put(key, value);
	}
	
	public JsonArray getJsonArray(String key) {
		return (JsonArray) super.get(key);
	}

	public void setJsonArray(String key, JsonArray value) {
		super.put(key, value);
	}

	public String getString(String key){
		return (String) super.get(key);
	}

	public void setString(String key, String value){
		super.put(key, value);
	}

	public Boolean getBoolean(String key){
		return (Boolean) super.get(key);
	}

	public void setBoolean(String key, boolean value){
		super.put(key, value);
	}
	
	public void setDate(String key, Date value) {
		super.put(key, value);
	}
	
	public long getLong(String string) {
		return (Long) super.get(string);
	}
}
