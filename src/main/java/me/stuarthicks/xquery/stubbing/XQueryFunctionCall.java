package me.stuarthicks.xquery.stubbing;

import java.util.Arrays;
import java.util.List;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.FloatValue;
import net.sf.saxon.value.Int64Value;
import net.sf.saxon.value.StringValue;

import com.google.common.collect.Lists;

public class XQueryFunctionCall extends ExtensionFunctionCall {

    private static final long serialVersionUID = -657472652323160853L;

    private XQueryFunctionStubBuilder XQueryFunctionStubBuilder;
    private List<Object> arguments = Lists.newArrayList();
    public int numberOfInvocations = 0;

    public XQueryFunctionCall (XQueryFunctionStubBuilder XQueryFunctionStubBuilder) {
        this.XQueryFunctionStubBuilder = XQueryFunctionStubBuilder;
    }

    public List<Object> getArguments () {
        return this.arguments;
    }

    public int getNumberOfInvocations () {
        return this.numberOfInvocations;
    }

    @Override
    public Sequence call (XPathContext xPathContext, Sequence[] sequences) throws XPathException {
        this.arguments = sanitise(Arrays.asList(sequences));
        this.numberOfInvocations++;
        return getNextResult();
    }

    private Sequence getNextResult () {
        List<Sequence> values = this.XQueryFunctionStubBuilder.getResultValues();
        Sequence nextValue = null;
        if (values.size() > 1) {
            nextValue = values.get(0);
            values.remove(0);
            return nextValue;
        }
        return values.get(0);
    }

    private static List<Object> sanitise (List<Sequence> sequences) {
        List<Object> items = Lists.newArrayList();
        for (Sequence s : sequences) {
            if (s instanceof StringValue) {
                items.add(((StringValue) s).asString());
            }
            else if (s instanceof Int64Value) {
                items.add(((Int64Value) s).asBigInteger().intValue());
            }
            else if (s instanceof FloatValue) {
                items.add(((FloatValue) s).getFloatValue());
            }
            else {
                items.add(s);
            }
        }
        return items;
    }
}
