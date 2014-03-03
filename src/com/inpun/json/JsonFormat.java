package com.inpun.json;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.inpun.alt.gaecl.StringBufferWriter;

/**
 * @author anton.ananich@inpun.com
 */
public class JsonFormat {
	
	/** yyyy-MM-dd'T'HH:mm:ss.SSSZ */
	public static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    /**
     * Formats an object to produce a string.
     *
     * @param obj    The object to format
     * @return       Formatted string.
     */	
    public final String format (Object obj) {
    	StringBuffer buf = new StringBuffer();
		writeJsonString(obj, buf);
		return buf.toString();
    }
	
    /**
     * Encode a map into single-line JSON text and append it to dest buffer.
     */
	public void writeJsonString(Object src, StringBuffer dest) {
		StringBufferWriter w = new StringBufferWriter(dest);
		try {
			writeJsonString(src, w);
			w.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}
	
    /**
     * Encode a map into single-line JSON text and write it to dest stream.
     */
	public void writeJsonString(Object src, Writer dest) throws IOException {
		writeJsonString(src, dest, 1);
	}
	
	private void writeJsonString(Object src, Writer dest, int margin) throws IOException {
		if(src == null){
			dest.write("null");
			return;
		}
		
		if(src instanceof String){		
            dest.write('\"');
			escape((String)src, dest);
            dest.write('\"');
		} else if(src instanceof Double){
			if(((Double)src).isInfinite() || ((Double)src).isNaN())
				dest.write("null");
			else
				dest.write(src.toString());
		} else if(src instanceof Float){
			if(((Float)src).isInfinite() || ((Float)src).isNaN())
				dest.write("null");
			else
				dest.write(src.toString());
		} else if(src instanceof Number){
			dest.write(src.toString());
		} else if(src instanceof Boolean){
			dest.write(src.toString());
		} else if(src instanceof Map){
			@SuppressWarnings("unchecked")
			Set<Entry<String, Object>> set = ((Map<String, Object>) src).entrySet();
			
	        dest.write('{');	        
			boolean first = true;
			for (Map.Entry<String, Object> entry: set){
	            if(!first)
	                dest.write(',');
	            first = false;
	            indent(margin, dest);
	            writeJsonString(entry.getKey(), dest, margin+1);
	            dest.write(':');
	            writeJsonString(entry.getValue(), dest, margin+1);
			}
            indent(margin-1, dest);
			dest.write('}');
		} else if(src instanceof List) {
			@SuppressWarnings("unchecked")
			ArrayList<Object> list = (ArrayList<Object>) src;
			
	        dest.write('[');
			boolean first = true;
			for(Object value : list){
	            if(!first)
	                dest.write(',');
                first = false;
	            indent(margin, dest);
				writeJsonString(value, dest, margin+1);
			}
            indent(margin-1, dest);
			dest.write(']');
		} else if (src instanceof Date) {
			dest.write("date(\"");
			dest.write(DF.format(src));
			dest.write("\")");
		} else if (src instanceof Text) {
			dest.write("text(\"");
			dest.write(((Text) src).getValue());
			dest.write("\")");
		} else if (src instanceof Key) {
			dest.write("key(\"");
			dest.write(((Key) src).getKind());
			dest.write("\", ");
			if (((Key) src).getName()==null) {
				dest.write(Long.toString(((Key) src).getId()));
			} else {
				dest.write('"');
				dest.write(((Key) src).getName());
				dest.write('"');
			}
			dest.write(")");
		} else if (src instanceof User) {
			dest.write("user(\"");
			dest.write(((User) src).getEmail());
			dest.write("\", \"");
			dest.write(((User) src).getAuthDomain());
			dest.write("\")");
		} else {
			dest.write(src.toString());
		}
	}
	
	protected void indent(int margin, Writer dest) throws IOException {
		// no new lines are needed
	}
	
	/**
	 * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
	 * @throws IOException 
	 */
	private static void escape(String string, Writer out) throws IOException {
		if (string == null) {
			throw new NullPointerException();
		}

		int len = string.length();
		for (int i = 0; i < len; ++i) {
			char c = string.charAt(i);
			switch (c) {
			case '\\':
				out.append("\\\\");
				break;
			case '"':
				out.append("\\\"");
				break;
			case '/':
				out.append("\\/");
				break;
			case '\b':
				out.append("\\b");
				break;
			case '\t':
				out.append("\\t");
				break;
			case '\n':
				out.append("\\n");
				break;
			case '\f':
				out.append("\\f");
				break;
			case '\r':
				out.append("\\r");
				break;
			default:
				if (c<='\u001F')
					throw new IllegalArgumentException();
				out.append(c);
			}
		}
	}
}
