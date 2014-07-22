package me.stuarthicks.xquery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ResourceURIResolverTest {

    private static final String HREF = "http://example";

    private ResourceURIResolver underTest;

    @Before
    public void before () {
        this.underTest = new ResourceURIResolver();
    }

    @Test
    public void itReturnsNullIfFileNotThere () throws Exception {
        assertNull(this.underTest.resolve(HREF, "/nothing-to-see-here.txt"));
    }

    @Test
    public void itReturnsStreamOfFileIfThere () throws Exception {
        String expected = "why not zoidberg?";

        StreamSource actual = (StreamSource) this.underTest.resolve(HREF, "/something-here.txt");

        assertEquals(expected, IOUtils.toString(actual.getInputStream()).trim());
    }
}
