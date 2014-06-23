package me.stuarthicks.xqueryjunit;

import net.sf.saxon.s9api.*;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.io.IOUtils;


public class XQueryTestHelper {

  public static ExtensionFunction mockXQueryFunction(String namespaceURI, String localPart, SequenceType[] argumentTypes, SequenceType resultType) {
    ExtensionFunction func = Mockito.mock(ExtensionFunction.class);
    Mockito.when(func.getName()).thenReturn(new QName(namespaceURI, localPart));
    Mockito.when(func.getArgumentTypes()).thenReturn(argumentTypes);
    Mockito.when(func.getResultType()).thenReturn(resultType);
    return func;
  }

  public XdmValue evaluateXQuery(Processor proc, String fileName) throws SaxonApiException, IOException {
    XQueryCompiler comp = proc.newXQueryCompiler();
    XQueryExecutable exp = comp.compile(fromResource(fileName));
    XQueryEvaluator ev = exp.load();
    return ev.evaluate();
  }

  protected String fromResource(final String name) throws IOException {
    return IOUtils.toString(getClass().getResourceAsStream(name));
  }

}
