package org.csrdu.apex.helpers;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlUtils {

	public static void beginDocument(XmlPullParser parser, String firstElementName) 
			throws XmlPullParserException, IOException {
		int type;
		while ((type = parser.next()) != parser.START_TAG && type != parser.END_DOCUMENT) {
			;
		}

		if (type != parser.START_TAG) {
			throw new XmlPullParserException("No start tag found");
		}

		if (!parser.getName().equals(firstElementName)) {
			throw new XmlPullParserException("Unexpected start tag: found "
					+ parser.getName() + ", expected " + firstElementName);
		}
	}

}
