package me.stuarthicks.xqueryjunit;

import static me.stuarthicks.xqueryjunit.SaxonHelpers.nodeFromFile;
import static org.junit.Assert.assertEquals;
import me.stuarthicks.xqueryjunit.exceptions.XQueryException;
import me.stuarthicks.xqueryjunit.stubbing.XQueryFunctionStub;
import me.stuarthicks.xqueryjunit.stubbing.XQueryFunctionStubBuilder;
import net.sf.saxon.value.FloatValue;
import net.sf.saxon.value.Int64Value;
import net.sf.saxon.value.StringValue;

import org.junit.Before;
import org.junit.Test;

public class XQueryStubbingTest {

    private XQueryContext xq;

    @Before
    public void before () {
        this.xq = new XQueryContext();
    }

    @Test
    public void itShouldMockNoArgFunctionReturningString () throws XQueryException {
        helloStubber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_NONE)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
                .withReturnValue(new StringValue("Hello World!"))
                .done();

        String result = this.xq.evaluateXQueryFile("/hello.xqy").toString();

        assertEquals("Hello World!", result);
    }

    @Test
    public void itShouldMockSingleStringArgFunctionReturningString () throws XQueryException {
        XQueryFunctionStub hello = helloStubber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_STRING)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
                .withReturnValue(new StringValue("Hello World!"))
                .done();

        String result = this.xq.evaluateXQueryFile("/hello_with_arg.xqy").toString();

        assertEquals("World!", hello.getArguments().get(0));
        assertEquals(1, hello.getNumberOfInvocations());

        assertEquals("Hello World!", result);
    }

    @Test
    public void itShouldMockSingleIntArgFunctionReturningInt () throws XQueryException {
        XQueryFunctionStub hello = helloStubber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_INT)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_INT)
                .withReturnValue(new Int64Value(2))
                .done();

        String result = this.xq.evaluateXQueryFile("/hello_with_int.xqy").toString();

        assertEquals(2, hello.getArguments().get(0));
        assertEquals(String.valueOf(2), result);
    }

    @Test
    public void itShouldMockSingleFloatArgFunctionReturningFloat () throws XQueryException {
        XQueryFunctionStub hello = helloStubber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_FLOAT)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_FLOAT)
                .withReturnValue(new FloatValue(3.14f))
                .done();

        String result = this.xq.evaluateXQueryFile("/hello_with_float.xqy").toString();

        assertEquals(2.1f, hello.getArguments().get(0));
        assertEquals(String.valueOf(3.14f), result);
    }

    @Test
    public void itShouldMockCoreFunctions () throws Exception {
        XQueryFunctionStub doc = this.xq.buildXQueryCoreFunctionStub("fn:doc")
                .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_STRING)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_NODE)
                .withReturnValue(nodeFromFile("/foobar.xml"))
                .done();

        String result = this.xq.evaluateXQueryFile("/fn-doc.xqy").toString().trim();

        assertEquals("foo", doc.getArguments().get(0));
        assertEquals("<bar/>", result);
    }

    @Test
    public void itShouldAllowMultipleReturnValuesAndSettleOnLast () throws Exception {
        helloStubber()
                .withFunctionSignature(XQueryConstants.ARGUMENTS_NONE)
                .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
                .withReturnValue(new StringValue("A"))
                .withReturnValue(new StringValue("B"))
                .done();

        String result = this.xq.evaluateXQueryFile("/hello_thrice.xqy").getUnderlyingValue().toString().trim();

        assertEquals("(\"A\", \"B\", \"B\")", result);

    }

    private XQueryFunctionStubBuilder helloStubber () {
        return this.xq.buildXQueryFunctionStub()
                .withNamespaceURI("http://example/")
                .withPrefix("example")
                .withFunctionName("hello");
    }

}
