package com.inpun.alt.gaecl;

import com.google.appengine.api.NamespaceManager;

public class NamespaceCommand extends VerifiedCommand {
	
	private String newNamespace;
	
	public NamespaceCommand(String namespace) {
		if ("".equals(namespace)) {
			newNamespace = null;
		} else
			newNamespace = namespace;
	}

	@Override
	ExecutionResult execute(DatastoreConnection conn) {
		NamespaceManager.set(newNamespace);
		return new ExecutionResult();
	}
	
}
