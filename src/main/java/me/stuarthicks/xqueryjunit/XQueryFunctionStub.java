package me.stuarthicks.xqueryjunit;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;

import java.util.Arrays;
import java.util.List;

public class XQueryFunctionStub extends ExtensionFunctionDefinition {

    private MockXQueryFunctionBuilder mockXQueryFunctionBuilder;
    private XQueryFunctionCall XQueryFunctionCall = new XQueryFunctionCall();

    public XQueryFunctionStub(MockXQueryFunctionBuilder mockXQueryFunctionBuilder) {
        this.mockXQueryFunctionBuilder = mockXQueryFunctionBuilder;
    }

    /**
     * Returns the actual arguments the function was most recently called with
     * or an empty List if the function has never been called.
     * @return
     */
    public List<Sequence> getArguments() {
        return Arrays.asList(this.XQueryFunctionCall.getArguments());
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
                mockXQueryFunctionBuilder.getPrefix(),
                mockXQueryFunctionBuilder.getNamespaceURI(),
                mockXQueryFunctionBuilder.getFunctionName()
        );
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return mockXQueryFunctionBuilder.getArgumentTypes();
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return mockXQueryFunctionBuilder.getResultType();
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return this.XQueryFunctionCall;
    }

    private class XQueryFunctionCall extends ExtensionFunctionCall {
        private Sequence[] arguments = new Sequence[]{};
        public int numberOfInvocations;

        public Sequence[] getArguments() {
            return arguments;
        }
        public int getNumberOfInvocations() {
            return this.numberOfInvocations;
        }

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            this.arguments = sequences;
            this.numberOfInvocations++;
            return mockXQueryFunctionBuilder.getResultValue();
        }
    }
}
