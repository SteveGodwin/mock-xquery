package me.stuarthicks.xquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ModuleResourceURIResolverTest {

    private static final String MODULE_URI = "http://example";
    private static final String BASE_URI = "http://another-example";

    ModuleResourceURIResolver underTest;

    @Before
    public void before () {
        this.underTest = new ModuleResourceURIResolver();
    }

    @Test
    public void itShouldReturnNullIfDocumentNotInResources () throws Exception {
        assertNull(this.underTest.resolve(MODULE_URI, BASE_URI, new String[] { "/nothing-to-see-here.txt" }));
    }
    
    @Test
    public void itShouldReturnStreamOfFileThatIsInResources () throws Exception {
        //Contents of file "something-here.txt"
        String expected = "why not zoidberg?";

        StreamSource[] actual = this.underTest.resolve(MODULE_URI, BASE_URI, new String[] { "/something-here.txt" });

        assertNotNull(actual);
        assertEquals(expected, IOUtils.toString(actual[0].getInputStream()).trim());
    }

}
