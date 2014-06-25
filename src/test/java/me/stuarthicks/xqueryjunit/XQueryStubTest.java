package me.stuarthicks.xqueryjunit;

import net.sf.saxon.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XQueryStubTest {

    private XQueryContext xq;

    @Before
    public void before() {
        this.xq = new XQueryContext();
    }

    @Test
    public void itShouldMockNoArgFunctionReturningString() throws XQueryException {
        xq.buildXQueryFunctionStub()
                .withNamespaceURI("http://example/")
                .withPrefix("example")
                .withFunctionName("hello")
                .withFunctionSignature(XQueryConstants.SIGNATURE_ARGUMENTS_NONE)
                .withReturnType(XQueryConstants.RETURN_TYPE_SINGLE_STRING)
                .withReturnValue(new StringValue("Hello World!"))
                .done();

        String result = xq.evaluateXQueryFile("/hello.xqy").toString();

        assertEquals("Hello World!", result);
        //verify(hello).call(XQueryConstants.CALLED_WITH_PARAMS_NONE);
    }

    @Test
    public void itShouldMockSingleStringArgFunctionReturningString() throws XQueryException {
        XQueryFunctionStub hello = xq.buildXQueryFunctionStub()
                .withNamespaceURI("http://example/")
                .withPrefix("example")
                .withFunctionName("hello")
                .withFunctionSignature(XQueryConstants.SIGNATURE_ARGUMENTS_SINGLE_STRING)
                .withReturnType(XQueryConstants.RETURN_TYPE_SINGLE_STRING)
                .withReturnValue(new StringValue("Hello World!"))
                .done();

        String result = xq.evaluateXQueryFile("/hello_with_arg.xqy").toString();

        // For some reason the quotes around the string in the XQuery doc get
        // included in the value of the Java string for that variable :(
        assertEquals("\"World!\"", hello.getArguments().get(0).toString());
        assertEquals(1, hello.getNumberOfInvocations());

        assertEquals("Hello World!", result);
        //verify(hello).call(XQueryConstants.CALLED_WITH_PARAMS_NONE);
    }

}
