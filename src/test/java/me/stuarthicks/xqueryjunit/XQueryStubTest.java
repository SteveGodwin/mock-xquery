package me.stuarthicks.xqueryjunit;

import jdk.nashorn.internal.ir.annotations.Ignore;
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

        // For some reason the quotes around the string in the XQuery doc get
        // included in the value of the Java string for that variable :(
        assertEquals("\"World!\"", hello.getArguments().get(0).toString());
        assertEquals(1, hello.getNumberOfInvocations());

        assertEquals("Hello World!", result);
    }

    @Ignore // TODO Saxon loads builtins before it loads extensions
    @Test
    public void itShouldBeAbleToReplaceBuiltins() throws XQueryException {
        xq.buildXQueryFunctionStub()
                .withNamespaceURI("http://www.w3.org/2005/xpath-functions")
                .withPrefix("fn")
                .withFunctionName("doc")
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_STRING)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
                .withReturnValue(new StringValue("whats up"))
                .done();

        String doc = xq.evaluateXQueryFile("/fn-doc.xqy").toString();

        assertEquals("whats up", doc);
    }

}
