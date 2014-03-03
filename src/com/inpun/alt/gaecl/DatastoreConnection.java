package com.inpun.alt.gaecl;

import java.io.IOException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

public class DatastoreConnection {
	
	private RemoteApiInstaller installer;

	public DatastoreConnection(String hostname, int port, String username, String password) throws IOException {
		RemoteApiOptions options = new RemoteApiOptions()
	    	.server(hostname, port)
		    .credentials(username, password);
		
		installer = new RemoteApiInstaller();
		installer.install(options);
	}
	
	public DatastoreService getDatastoreService(){
		return DatastoreServiceFactory.getDatastoreService();
	}
	
	public void close() {
		installer.uninstall();
	}
}
