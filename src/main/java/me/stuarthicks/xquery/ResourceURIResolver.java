package me.stuarthicks.xquery;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

public class ResourceURIResolver implements URIResolver {

    @SuppressWarnings("resource") // Saxon promises to close this for us
    @Override
    public Source resolve (String href, String base) throws TransformerException {
        InputStream resource = ResourceURIResolver.class.getResourceAsStream(base);
        if (null == resource) return null;
        return new StreamSource(resource);
    }

}
