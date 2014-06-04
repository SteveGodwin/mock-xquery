package me.stuarthicks.xquery-junit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import net.sf.saxon.s9api.ExtensionFunction;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SequenceType;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmValue;


public class XQueryTestHelper {

	public static ExtensionFunction mockXQueryFunction(String namespaceURI, String localPart,
			SequenceType[] argumentTypes, SequenceType resultType) {
		
		ExtensionFunction func = mock(ExtensionFunction.class);
		when(func.getName()).thenReturn(new QName(namespaceURI, localPart));
		when(func.getArgumentTypes()).thenReturn(argumentTypes);
		when(func.getResultType()).thenReturn(resultType);
		return func;
	}
	
	public static XdmValue evaluateXQuery(Processor proc, String fileName) throws SaxonApiException, IOException {
		File xqueryFile = new File(fileName);
		XQueryCompiler comp = proc.newXQueryCompiler();
		XQueryExecutable exp = comp.compile(xqueryFile);
		XQueryEvaluator ev = exp.load();
		return ev.evaluate();
	}
	
}
