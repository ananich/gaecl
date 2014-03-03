package com.inpun.alt.gaecl;

import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.inpun.json.JsonArray;
import com.inpun.json.JsonObject;

public class ListCommand extends VerifiedCommand {
	
	private String kind;
	private JsonObject filters;
	private JsonArray fields;
	
	public ListCommand(String kind, JsonObject filters, JsonArray fields) {
		this.kind = kind;
		this.filters = filters;
		this.fields = fields;
	}

	@Override
	ExecutionResult execute(DatastoreConnection conn) {
		DatastoreService ds = conn.getDatastoreService();
		Query q = "*".equals(kind) ? new Query() : new Query(kind);
		if (fields==null)
			q.setKeysOnly();
		if (filters!=null) {
			for (Object k : filters.keySet()) {
				q.addFilter((String)k, FilterOperator.EQUAL, filters.get(k));
			}
		}
		PreparedQuery pq = ds.prepare(q);
		Iterable<Entity> i = pq.asIterable();
		JsonArray a = new JsonArray();
		for (Entity result : i) {
			JsonObject obj = new JsonObject();
			obj.put("__key__", result.getKey());
			if (fields != null) {
				Map<String, Object> props = result.getProperties();
				for (Object f : fields) {
					Object val = props.get(f);
					obj.put((String) f, val);
				}
			}
			a.add(obj);
		}
		return new ExecutionResult(0, a);
	}
}
