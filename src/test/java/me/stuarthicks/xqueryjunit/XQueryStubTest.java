package me.stuarthicks.xqueryjunit;

import net.sf.saxon.s9api.ExtensionFunction;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmAtomicValue;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class XQueryStubTest {

    private XQueryTestHelper xq;

    @Before
    public void before() {
        this.xq = new XQueryTestHelper();
    }

    @Test
    public void itShouldMockNoArgFunctionReturningString() throws SaxonApiException, IOException {
        ExtensionFunction hello = xq.mockXQueryFunction("http://exmple/", "hello", XQueryConstants.MOCK_ARGUMENTS_NONE, XQueryConstants.RETURNS_SINGLE_STRING);
        when(hello.call(XQueryConstants.WITH_PARAMS_NONE)).thenReturn(new XdmAtomicValue("Hello World!"));
        xq.registerXQueryFunction(hello);

        String result = xq.evaluateXQuery("/hello.xqy").toString();

        assertEquals("Hello World!", result);
        verify(hello).call(XQueryConstants.WITH_PARAMS_NONE);
    }

}
