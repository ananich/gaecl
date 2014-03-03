package com.inpun.alt.gaecl;

public class Interpreter {
		
	private StringBuffer sb = new StringBuffer();

	public void addLine(String l) {
		sb.append(' ').append(l);
	}
	
	public String popCommand() throws InterpreterException {
		boolean f1 = false, f2 = false;
		for (int i=0; i<sb.length();++i) {
			char c = sb.charAt(i);
			if (c=='"' && !f1) {
				f2 = !f2;
				continue;
			} else if (c=='\'' && !f2) {
				f1 = !f1;
				continue;
			} else if (c==';') {
				if (f1 || f2) {
					continue;
				} else {
					String sc = sb.substring(0, i);
					sb.replace(0, i+1, "");
					return sc;
				}
// начало подпорки
			} else if (c=='\r') {
				sb.replace(i, i+1, " ");
			} else if (c=='\n') {
				sb.replace(i, i+1, " ");
// конец подпорки
			}
		}
		return null;
	}
}
