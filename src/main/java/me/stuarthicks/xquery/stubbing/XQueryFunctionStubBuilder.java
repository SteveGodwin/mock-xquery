package me.stuarthicks.xquery.stubbing;

import java.util.List;

import net.sf.saxon.om.Sequence;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.value.SequenceType;

import com.google.common.collect.Lists;

public final class XQueryFunctionStubBuilder {

    private final Processor processor;

    private String prefix;
    private String namespaceURI;
    private String functionName;
    private SequenceType[] argumentTypes;
    private SequenceType resultType;
    private List<Sequence> resultValues;

    public String getPrefix () {
        return this.prefix;
    }

    public String getNamespaceURI () {
        return this.namespaceURI;
    }

    public String getFunctionName () {
        return this.functionName;
    }

    public SequenceType[] getArgumentTypes () {
        return this.argumentTypes;
    }

    public SequenceType getResultType () {
        return this.resultType;
    }

    public List<Sequence> getResultValues () {
        return this.resultValues;
    }

    public XQueryFunctionStubBuilder (Processor processor) {
        this.resultValues = Lists.newArrayList();
        this.processor = processor;
    }

    public XQueryFunctionStubBuilder withPrefix (final String nsPrefix) {
        this.prefix = nsPrefix;
        return this;
    }

    public XQueryFunctionStubBuilder withNamespaceURI (final String namespace) {
        this.namespaceURI = namespace;
        return this;
    }

    public XQueryFunctionStubBuilder withFunctionName (final String name) {
        this.functionName = name;
        return this;
    }

    public XQueryFunctionStubBuilder withFunctionSignature (final SequenceType[] arguments) {
        this.argumentTypes = arguments;
        return this;
    }

    public XQueryFunctionStubBuilder withReturnType (final SequenceType returnType) {
        this.resultType = returnType;
        return this;
    }

    public XQueryFunctionStubBuilder withReturnValue (final Sequence returnValue) {
        this.resultValues.add(returnValue);
        return this;
    }

    public XQueryFunctionStub done () {
        XQueryFunctionStub function = new XQueryFunctionStub(this);
        XQueryFunctionStubBuilder.this.processor.registerExtensionFunction(function);
        return function;
    }

}
