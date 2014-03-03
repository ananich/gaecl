package com.inpun.alt.gaecl;

public class ExitCommand extends VerifiedCommand {
	@Override
	ExecutionResult execute(DatastoreConnection conn) {
		System.exit(0);
		return null;
	}
}
