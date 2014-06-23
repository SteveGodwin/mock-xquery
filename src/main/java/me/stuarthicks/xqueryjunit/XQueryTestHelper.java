package me.stuarthicks.xqueryjunit;

import net.sf.saxon.s9api.*;
import org.apache.commons.io.IOUtils;
import org.mockito.Mockito;

import java.io.IOException;

public final class XQueryTestHelper {

    private final Processor processor;

    public XQueryTestHelper() {
        this.processor = new Processor(false);
    }

    public final ExtensionFunction mockXQueryFunction(String namespaceURI, String localPart, SequenceType[] argumentTypes, SequenceType resultType) {
        ExtensionFunction func = Mockito.mock(ExtensionFunction.class);
        Mockito.when(func.getName()).thenReturn(new QName(namespaceURI, localPart));
        Mockito.when(func.getArgumentTypes()).thenReturn(argumentTypes);
        Mockito.when(func.getResultType()).thenReturn(resultType);
        return func;
    }

    public final void registerXQueryFunction(ExtensionFunction func) {
        this.processor.registerExtensionFunction(func);
    }

    public final XdmValue evaluateXQuery(String fileName) throws SaxonApiException, IOException {
        XQueryCompiler comp = this.processor.newXQueryCompiler();
        XQueryExecutable exp = comp.compile(fromResource(fileName));
        XQueryEvaluator ev = exp.load();
        return ev.evaluate();
    }

    private static final String fromResource(final String name) throws IOException {
        return IOUtils.toString(XQueryTestHelper.class.getResourceAsStream(name));
    }

}
