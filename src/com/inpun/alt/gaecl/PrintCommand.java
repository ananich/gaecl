package com.inpun.alt.gaecl;

import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.inpun.json.JsonObject;

public class PrintCommand extends VerifiedCommand {
	
	private Key key;
	
	public PrintCommand(Key key) {
		this.key = key;
	}

	@Override
	ExecutionResult execute(DatastoreConnection conn) {
		DatastoreService ds = conn.getDatastoreService();
		try {
			Entity e = ds.get(key);
			Map<String, Object> properties = e.getProperties();
			JsonObject obj = new JsonObject();
			//obj.put("__key__", e.getKey());
			obj.putAll(properties);
			return new ExecutionResult(0, obj);
		} catch (EntityNotFoundException e) {
			return new ExecutionResult(1, "No entity with the specified Key could be found.");
		}
	}
}
