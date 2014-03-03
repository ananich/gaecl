package com.inpun.json;

import java.io.IOException;
import java.io.Writer;

public class JsonPrettyFormat extends JsonFormat {

	@Override
	protected void indent(int margin, Writer dest) throws IOException {
        dest.write("\n");
		for (int i=0; i<margin; ++i)
			dest.write('\t');
	}

}
