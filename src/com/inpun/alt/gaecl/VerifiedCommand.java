package com.inpun.alt.gaecl;

abstract public class VerifiedCommand {
	abstract ExecutionResult execute(DatastoreConnection conn);
}
