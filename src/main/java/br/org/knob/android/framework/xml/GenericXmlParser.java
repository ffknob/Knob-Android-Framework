package br.org.knob.android.framework.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericXmlParser {
    private static final String TAG = "GenericXmlParser";

    protected static final String NAMESPACE = null;

    protected XmlPullParser parser;
    protected String startDocumentTag;
    protected String businessElementTag;

    public GenericXmlParser(String startDocumentTag, String businessElementTag) {
        this.startDocumentTag = startDocumentTag;
        this.businessElementTag = businessElementTag;
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed();
        } finally {
            in.close();
        }
    }

    private List readFeed() throws XmlPullParserException, IOException {
        List businessElements = new ArrayList();

        parser.require(XmlPullParser.START_TAG, NAMESPACE, startDocumentTag);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(businessElementTag)) {
                businessElements.add(readBusinessElement());
            } else {
                skip();
            }
        }
        return businessElements;
    }

    protected GenericXmlElement readBusinessElement() throws XmlPullParserException, IOException {
        return null;
    }

    protected void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    protected String readText() throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    protected String readBusinessAttribute(String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String businessAttribute = readText();
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return businessAttribute;
    }

    public XmlPullParser getParser() {
        return parser;
    }

    public void setParser(XmlPullParser parser) {
        this.parser = parser;
    }

    public String getStartDocumentTag() {
        return startDocumentTag;
    }

    public void setStartDocumentTag(String startDocumentTag) {
        this.startDocumentTag = startDocumentTag;
    }

    public String getBusinessElementTag() {
        return businessElementTag;
    }

    public void setBusinessElementTag(String businessElementTag) {
        this.businessElementTag = businessElementTag;
    }
}