package com.inpun.alt.gaecl;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.inpun.json.JsonArray;
import com.inpun.json.JsonFormat;
import com.inpun.json.JsonObject;

public class SetCommand extends VerifiedCommand {
	
	private Key key;
	private JsonObject obj;
	private JsonArray arr;
	
	public SetCommand(Key key, JsonObject obj) {
		this.key = key;
		this.obj = obj;
	}

	public SetCommand(Key key, JsonArray a) {
		this.key = key;
		this.arr = a;
	}

	@Override
	ExecutionResult execute(DatastoreConnection conn) {
		DatastoreService ds = conn.getDatastoreService();
		Entity e;
		try {
			e = ds.get(key);
		} catch (EntityNotFoundException e1) {
			return new ExecutionResult(1, "No entity with the specified Key could be found.");
		}
		if (obj != null) {
			for (String k : obj.keySet()) {
				e.setProperty(k, obj.get(k));
			}
			ds.put(e);
			return new ExecutionResult(0, "entity saved successfully at " + new JsonFormat().format(key));
		} else if (arr!=null){
			throw new UnsupportedOperationException();
		}
		throw new NullPointerException();
	}
}
