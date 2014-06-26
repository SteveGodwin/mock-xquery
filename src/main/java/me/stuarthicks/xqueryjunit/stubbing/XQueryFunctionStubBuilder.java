package me.stuarthicks.xqueryjunit.stubbing;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.SequenceType;

public final class XQueryFunctionStubBuilder {

    private final Processor processor;

    private String prefix;
    private String namespaceURI;
    private String functionName;
    private SequenceType[] argumentTypes;
    private SequenceType resultType;
    private AtomicValue resultValue;

    public String getPrefix() {
        return prefix;
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public String getFunctionName() {
        return functionName;
    }

    public SequenceType[] getArgumentTypes() {
        return argumentTypes;
    }

    public SequenceType getResultType() {
        return resultType;
    }

    public AtomicValue getResultValue() {
        return resultValue;
    }

    public XQueryFunctionStubBuilder(Processor processor) {
        this.processor = processor;
    }

    public XQueryFunctionStubBuilder withPrefix(final String prefix) {
        this.prefix = prefix;
        return this;
    }

    public XQueryFunctionStubBuilder withNamespaceURI(final String namespace) {
        this.namespaceURI = namespace;
        return this;
    }

    public XQueryFunctionStubBuilder withFunctionName(final String name) {
        this.functionName = name;
        return this;
    }

    public XQueryFunctionStubBuilder withFunctionSignature(final SequenceType[] arguments) {
        this.argumentTypes = arguments;
        return this;
    }

    public XQueryFunctionStubBuilder withReturnType(final SequenceType returnType) {
        this.resultType = returnType;
        return this;
    }

    public XQueryFunctionStubBuilder withReturnValue(final AtomicValue returnValue) {
        this.resultValue = returnValue;
        return this;
    }

    public XQueryFunctionStub done() {
        XQueryFunctionStub function = new XQueryFunctionStub(this);
        XQueryFunctionStubBuilder.this.processor.registerExtensionFunction(function);
        return function;
    }

}
