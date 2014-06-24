package me.stuarthicks.xqueryjunit;

import net.sf.saxon.s9api.XdmAtomicValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XQueryStubTest {

    private XQueryTestContext xq;

    @Before
    public void before() {
        this.xq = new XQueryTestContext();
    }

    @Test
    public void itShouldMockNoArgFunctionReturningString() throws XQueryException {
        xq.mockXQueryFunction()
                .withNamespaceURI("http://example/")
                .withFunctionName("hello")
                .withFunctionSignature(XQueryConstants.SIGNATURE_ARGUMENTS_NONE)
                .withReturnType(XQueryConstants.RETURN_TYPE_SINGLE_STRING)
                .withReturnValue(new XdmAtomicValue("Hello World!"))
                .done();

        String result = xq.evaluateXQueryFile("/hello.xqy").toString();

        assertEquals("Hello World!", result);
        //verify(hello).call(XQueryConstants.CALLED_WITH_PARAMS_NONE);
    }

    @Test
    public void itShouldMockSingleStringArgFunctionReturningString() throws XQueryException {
        xq.mockXQueryFunction()
                .withNamespaceURI("http://example/")
                .withFunctionName("hello")
                .withFunctionSignature(XQueryConstants.SIGNATURE_ARGUMENTS_SINGLE_STRING)
                .withReturnType(XQueryConstants.RETURN_TYPE_SINGLE_STRING)
                .withReturnValue(new XdmAtomicValue("Hello World!"))
                .done();

        String result = xq.evaluateXQueryFile("/hello.xqy").toString();

        assertEquals("Hello World!", result);
        //verify(hello).call(XQueryConstants.CALLED_WITH_PARAMS_NONE);
    }

}
