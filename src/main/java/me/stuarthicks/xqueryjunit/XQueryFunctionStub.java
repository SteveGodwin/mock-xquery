package me.stuarthicks.xqueryjunit;

import com.google.common.collect.Lists;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

import java.util.Arrays;
import java.util.List;

public class XQueryFunctionStub extends ExtensionFunctionDefinition {

    private XQueryFunctionStubBuilder XQueryFunctionStubBuilder;
    private XQueryFunctionCall XQueryFunctionCall = new XQueryFunctionCall();

    public XQueryFunctionStub(XQueryFunctionStubBuilder XQueryFunctionStubBuilder) {
        this.XQueryFunctionStubBuilder = XQueryFunctionStubBuilder;
    }

    /**
     * Returns the actual arguments the function was most recently called with
     * or an empty List if the function has never been called.
     * @return
     */
    public List<Object> getArguments() {
        return this.XQueryFunctionCall.getArguments();
    }

    /**
     * Returns the number of times this XQueryFunctionStub has been called
     * @return
     */
    public int getNumberOfInvocations() {
        return this.XQueryFunctionCall.getNumberOfInvocations();
    }

    @Override
    public StructuredQName getFunctionQName() {
        return new StructuredQName(
                this.XQueryFunctionStubBuilder.getPrefix(),
                this.XQueryFunctionStubBuilder.getNamespaceURI(),
                this.XQueryFunctionStubBuilder.getFunctionName()
        );
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return this.XQueryFunctionStubBuilder.getArgumentTypes();
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return this.XQueryFunctionStubBuilder.getResultType();
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return this.XQueryFunctionCall;
    }

    private class XQueryFunctionCall extends ExtensionFunctionCall {
        private List<Object> arguments = Lists.newArrayList();
        public int numberOfInvocations = 0;

        public List<Object> getArguments() {
            return arguments;
        }
        public int getNumberOfInvocations() {
            return this.numberOfInvocations;
        }

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            this.arguments = sanitise(Arrays.asList(sequences));
            this.numberOfInvocations++;
            return XQueryFunctionStubBuilder.getResultValue();
        }

        private List<Object> sanitise(List<Sequence> sequences) {
            List<Object> items = Lists.newArrayList();
            for (Sequence s : sequences) {
                if (s instanceof StringValue) {
                    items.add(((StringValue) s).asString());
                } else {
                    items.add(s);
                }
            }
            return items;
        }
    }
}
