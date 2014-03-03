package com.inpun.alt.gaecl;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.output.NullOutputStream;

import com.google.appengine.api.NamespaceManager;
import com.inpun.json.JsonArray;
import com.inpun.json.JsonObject;

public class AppEngineShell {
	/**
	 * Possible commands are:
	 * - NAMESPACE
	 * - NAMESPACE name
	 * - LIST *
	 * - LIST kind
	 * - LIST kind {"filter_name": filter_value}
	 * - LIST kind {"filter_name": filter_value} [fields]
	 * - PRINT key
	 * - PUT key {full_json_obj}
	 * - SET key {fields_to_set}
	 * - EXIT
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Options options = new Options();
		options.addOption(null, "localhost", false, "Connect to localhost instead of App Engine cloud");
		options.addOption("A", "application", true, "The application ID to use.");
		options.addOption("u", "email", true, "The email address of the Google account of an administrator for the application, for actions that require signing in. If omitted and no cookie is stored from a previous use of the command, the command will prompt for this value.");
		options.addOption("p", "password", true, "Password");
		options.addOption(null, "passin", false, "If given, the tool accepts the Google account password in stdin instead of prompting for it interactively. This allows you to invoke the tool from a script without putting your password on the command line.");
		options.addOption("q", "quiet", false, "Do not print messages when successful.");
		options.addOption("v", "verbose", false, "Print messages about what the command is doing.");
		options.addOption("h", "help", false, "Print this help message.");
		
		try {
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);
			
			if (args.length==0 || cmd.hasOption('h')) {
				printHelpAndExit(options);
			} else if (!cmd.hasOption('A') && !cmd.hasOption("localhost")) {
				System.err.println("Application ID is not specidfied, but it's required.");
				printHelpAndExit(options);
			}
			
			if (cmd.hasOption('q')) {
				System.setOut(new PrintStream(new NullOutputStream()));
			}
			
			String app_id=null;
			if (cmd.hasOption('A')) {
				app_id = cmd.getOptionValue('A');
			}
				
			String email;
			if (!cmd.hasOption('u')) {
				System.out.print("Email:");
				Scanner in = new Scanner(System.in);
				email = in.nextLine();
			} else {
				email = cmd.getOptionValue('u');
			}
	
			String password;
			if (cmd.hasOption("passin")) {
				System.out.print(String.format("Password for %s:", email));
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				password = br.readLine();
			} else if(cmd.hasOption("password")) {
				password = cmd.getOptionValue("password");
			} else {
				System.out.print(String.format("Password for %s:", email));
				// hide input
				Console console = System.console();
				if (console == null) {
					System.out.println("Couldn't get Console instance");
					System.exit(0);
				}
			    char passwordArray[] = console.readPassword("");
			    password = new String(passwordArray);
			}
			AppEngineShell shell;
			if (cmd.hasOption("localhost")) {
				shell = new AppEngineShell(cmd.hasOption('v'), email, password);
			} else {
				shell = new AppEngineShell(cmd.hasOption('v'), app_id, email, password);
			}
			shell.run();
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		}
		
	}

	private static void printHelpAndExit(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "java -jar gaeshell.jar", options );
		System.exit(0);
	}
	
	/* --- End of static members --- */
	
	private boolean verbose;
	private String app_id, email, password;
	private boolean isLocalhost;
	
	public AppEngineShell(boolean verbose, String app_id, String email, String password) {
		this.verbose = verbose;
		this.app_id = app_id;
		this.email = email;
		this.password = password;
	}

	public AppEngineShell(boolean verbose, String email, String password) {
		this.verbose = verbose;
		this.isLocalhost = true;
		this.email = email;
		this.password = password;
	}

	public void run() throws IOException {
		if (verbose) {
			System.out.println(String.format("Connected to https://appengine.google.com/dashboard?&app_id=%s as %s", app_id, email, password));
		}
		Interpreter inter = new Interpreter();
		BufferedReader script = new BufferedReader(new InputStreamReader(System.in));
		DatastoreConnection conn;
		if (isLocalhost) {
			conn = new DatastoreConnection("localhost", 8888, email, password);
		} else {
			conn = new DatastoreConnection(app_id+".appspot.com", 443, email, password);
		}
		
		boolean needp = true;
		for (String l = ""; l!=null; ){
			
			// print prompt
			String namespace = NamespaceManager.get();
			if (namespace==null)
				namespace = "";
			if (needp)
				System.out.print(namespace+"> ");
			
			// read line
			l = script.readLine();
			inter.addLine(l);
			
			// interpret the line
			try {
				
				String scommand = inter.popCommand();
				needp = scommand!=null;
				if (scommand==null){
					continue;
				}
				Parser p = new Parser(new StringReader(scommand));
				Object o=null;
				try {
					o = p.parse();
				} catch (com.inpun.json.ParseException e) {
					for (int i=0; i<p.getPosition()+1+namespace.length(); ++i) {
						System.err.print(' ');
					}
					System.err.println('^');
					System.err.println("Error in position " + p.getPosition());
					continue;
				}
				
				if (o==null || ! (o instanceof VerifiedCommand)) {
					// empty command
					continue;
				}
				
				VerifiedCommand vc = (VerifiedCommand) o;
				ExecutionResult er = vc.execute(conn);
				if (er.getError()!=0) {
					System.out.println("Error: "+er.getMessage());
				} else if (er.getObject()!=null) {
					System.out.println(er.getObject().toPrettyString());
				} else if (er.getArray()!=null) {
					JsonArray a = er.getArray();
					if (a.isEmpty()) {
						System.out.println("[]");
						continue;
					}
					System.out.println("[");
					Iterator<?> i = a.iterator();
					while (i.hasNext()) {
						String s = ((JsonObject)i.next()).toString();
						if (i.hasNext())
							System.out.println(s+",");
						else
							System.out.println(s);
					}
					System.out.println("]");
				} else if (er.getMessage()!=null) {
					System.out.println(er.getMessage());
				}
			} catch (InterpreterException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
