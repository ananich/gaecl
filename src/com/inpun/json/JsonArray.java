package com.inpun.json;

import java.util.ArrayList;


/**
 * A JSON array. JSONObject supports java.util.List interface.
 * 
 * @author anton.ananich@inpun.com
 */
public class JsonArray extends ArrayList<Object> {

	@Override
	public String toString(){
		return new JsonFormat().format(this);
	}

	public String toPrettyString(){
		return new JsonPrettyFormat().format(this);
	}

}
