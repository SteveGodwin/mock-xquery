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
                .withFunctionSignature(XQueryConstants.ARGUMENTS_NONE)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
                .withReturnValue(new StringValue("Hello World!"))
                .done();

        String result = xq.evaluateXQueryFile("/hello.xqy").toString();

        assertEquals("Hello World!", result);
    }

    @Test
    public void itShouldMockSingleStringArgFunctionReturningString() throws XQueryException {
        XQueryFunctionStub hello = xq.buildXQueryFunctionStub()
                .withNamespaceURI("http://example/")
                .withPrefix("example")
                .withFunctionName("hello")
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_STRING)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
                .withReturnValue(new StringValue("Hello World!"))
                .done();

        String result = xq.evaluateXQueryFile("/hello_with_arg.xqy").toString();

        assertEquals("World!", hello.getArguments().get(0));
        assertEquals(1, hello.getNumberOfInvocations());

        assertEquals("Hello World!", result);
    }

}
