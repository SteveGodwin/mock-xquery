package me.stuarthicks.xqueryjunit.stubbing;

import com.google.common.collect.Lists;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.Int64Value;
import net.sf.saxon.value.StringValue;

import java.util.Arrays;
import java.util.List;

public class XQueryFunctionCall extends ExtensionFunctionCall {
    private XQueryFunctionStubBuilder XQueryFunctionStubBuilder;
    private List<Object> arguments = Lists.newArrayList();
    public int numberOfInvocations = 0;

    public XQueryFunctionCall(XQueryFunctionStubBuilder XQueryFunctionStubBuilder) {
        this.XQueryFunctionStubBuilder = XQueryFunctionStubBuilder;
    }

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
        return this.XQueryFunctionStubBuilder.getResultValue();
    }

    private List<Object> sanitise(List<Sequence> sequences) {
        List<Object> items = Lists.newArrayList();
        for (Sequence s : sequences) {
            if (s instanceof StringValue) {
                items.add(((StringValue) s).asString());
            } else if (s instanceof Int64Value) {
                items.add(((Int64Value)s).asBigInteger().intValue());
            } else {
                items.add(s);
            }
        }
        return items;
    }
}
