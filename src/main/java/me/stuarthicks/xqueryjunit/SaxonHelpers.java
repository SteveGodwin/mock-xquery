package me.stuarthicks.xqueryjunit;

import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.trans.XPathException;

import javax.xml.transform.stream.StreamSource;

public class SaxonHelpers {

    /**
     * Constructs a DocumentInfo Node from the given classpath resource
     * @param fileName
     * @return
     * @throws XPathException
     */
    public static DocumentInfo nodeFromFile(String fileName) throws XPathException {
        //TODO If we ever want custom configuration, we'll need to pass the processor from the XQueryContext into here...
        return new Processor(false).getUnderlyingConfiguration().buildDocument(new StreamSource(SaxonHelpers.class.getResourceAsStream(fileName)));
    }
}
