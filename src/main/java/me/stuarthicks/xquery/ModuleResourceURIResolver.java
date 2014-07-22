package me.stuarthicks.xquery;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.lib.ModuleURIResolver;
import net.sf.saxon.trans.XPathException;

public class ModuleResourceURIResolver implements ModuleURIResolver {

    private static final long serialVersionUID = -7507064066181935105L;

    /**
     * Attempts to find the requested file as a resource available on the
     * classpath. Stops at the first one it finds and returns it (never returns
     * multiple results). Returns null if no files are found.
     * 
     * @param moduleURI
     *            the module URI of the module to be imported; or null when
     *            loading a non-library module.
     * @param baseURI
     *            The base URI of the module containing the "import module"
     *            declaration; null if no base URI is known
     * @param locations
     *            The set of URIs specified in the "at" clause of
     *            "import module", which serve as location hints for the module.
     *            The values supplied are absolute URIs formed by resolving the
     *            relative URI appearing in the query against the base URI of
     *            the query.
     */
    @SuppressWarnings("resource") // Saxon promises the close the stream for us
    @Override
    public StreamSource[] resolve (String moduleURI, String baseURI, String[] locations) throws XPathException {
        List<String> locationHints = Arrays.asList(locations);
        for (String hint : locationHints) {
            InputStream stream = ModuleResourceURIResolver.class.getResourceAsStream(hint);
            if (stream != null) {
                StreamSource streamSource = new StreamSource(stream);
                return new StreamSource[] { streamSource };
            }
        }
        return null;
    }

}
