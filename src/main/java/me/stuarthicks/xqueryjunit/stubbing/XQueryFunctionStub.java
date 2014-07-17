package me.stuarthicks.xqueryjunit.stubbing;

import java.util.List;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;

public class XQueryFunctionStub extends ExtensionFunctionDefinition {

    private static final long serialVersionUID = -2974727888536121137L;

    private XQueryFunctionStubBuilder XQueryFunctionStubBuilder;
    private XQueryFunctionCall XQueryFunctionCall;

    public XQueryFunctionStub (XQueryFunctionStubBuilder XQueryFunctionStubBuilder) {
        this.XQueryFunctionStubBuilder = XQueryFunctionStubBuilder;
        this.XQueryFunctionCall = new XQueryFunctionCall(this.XQueryFunctionStubBuilder);
    }

    /**
     * Returns the actual arguments the function was most recently called with
     * or an empty List if the function has never been called.
     * @return
     */
    public List<Object> getArguments () {
        return this.XQueryFunctionCall.getArguments();
    }

    /**
     * Returns the number of times this XQueryFunctionStub has been called
     * @return
     */
    public int getNumberOfInvocations () {
        return this.XQueryFunctionCall.getNumberOfInvocations();
    }

    @Override
    public StructuredQName getFunctionQName () {
        return new StructuredQName(
                this.XQueryFunctionStubBuilder.getPrefix(),
                this.XQueryFunctionStubBuilder.getNamespaceURI(),
                this.XQueryFunctionStubBuilder.getFunctionName());
    }

    @Override
    public SequenceType[] getArgumentTypes () {
        return this.XQueryFunctionStubBuilder.getArgumentTypes();
    }

    @Override
    public SequenceType getResultType (SequenceType[] sequenceTypes) {
        return this.XQueryFunctionStubBuilder.getResultType();
    }

    @Override
    public ExtensionFunctionCall makeCallExpression () {
        return this.XQueryFunctionCall;
    }

}
