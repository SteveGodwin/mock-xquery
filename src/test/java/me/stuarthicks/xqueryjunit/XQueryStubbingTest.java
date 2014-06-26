package me.stuarthicks.xqueryjunit;

import me.stuarthicks.xqueryjunit.exceptions.XQueryException;
import me.stuarthicks.xqueryjunit.stubbing.XQueryFunctionStub;
import me.stuarthicks.xqueryjunit.stubbing.XQueryFunctionStubBuilder;
import net.sf.saxon.value.FloatValue;
import net.sf.saxon.value.Int64Value;
import net.sf.saxon.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XQueryStubbingTest {

    private XQueryContext xq;

    @Before
    public void before() {
        this.xq = new XQueryContext();
    }

    @Test
    public void itShouldMockNoArgFunctionReturningString() throws XQueryException {
        helloStubbber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_NONE)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
                .withReturnValue(new StringValue("Hello World!"))
                .done();

        String result = xq.evaluateXQueryFile("/hello.xqy").toString();

        assertEquals("Hello World!", result);
    }

    @Test
    public void itShouldMockSingleStringArgFunctionReturningString() throws XQueryException {
        XQueryFunctionStub hello = helloStubbber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_STRING)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
                .withReturnValue(new StringValue("Hello World!"))
                .done();

        String result = xq.evaluateXQueryFile("/hello_with_arg.xqy").toString();

        assertEquals("World!", hello.getArguments().get(0));
        assertEquals(1, hello.getNumberOfInvocations());

        assertEquals("Hello World!", result);
    }

    @Test
    public void itShouldMockSingleIntArgFunctionReturningInt() throws XQueryException {
        XQueryFunctionStub hello = helloStubbber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_INT)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_INT)
                .withReturnValue(new Int64Value(2))
                .done();

        String result = xq.evaluateXQueryFile("/hello_with_int.xqy").toString();

        assertEquals(2, hello.getArguments().get(0));
        assertEquals(String.valueOf(2), result);
    }

    @Test
    public void itShouldMockSingleFloatArgFunctionReturningFloat() throws XQueryException {
        XQueryFunctionStub hello = helloStubbber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_FLOAT)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_FLOAT)
                .withReturnValue(new FloatValue(3.14f))
                .done();

        String result = xq.evaluateXQueryFile("/hello_with_float.xqy").toString();

        assertEquals(2.1f, hello.getArguments().get(0));
        assertEquals(String.valueOf(3.14f), result);
    }

    private XQueryFunctionStubBuilder helloStubbber() {
        return xq.buildXQueryFunctionStub()
                .withNamespaceURI("http://example/")
                .withPrefix("example")
                .withFunctionName("hello");
    }

}
