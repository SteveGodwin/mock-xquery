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

    /**
     * Creates a new extension function using Mockito.
     * @param namespaceURI Namespace for mock function to exist in
     * @param localPart Name of xquery function
     * @param argumentTypes Method signature
     * @param resultType Method return type
     * @return
     */
    public final ExtensionFunction newMockXQueryFunction(String namespaceURI, String localPart, SequenceType[] argumentTypes, SequenceType resultType) {
        ExtensionFunction func = Mockito.mock(ExtensionFunction.class);
        Mockito.when(func.getName()).thenReturn(new QName(namespaceURI, localPart));
        Mockito.when(func.getArgumentTypes()).thenReturn(argumentTypes);
        Mockito.when(func.getResultType()).thenReturn(resultType);
        return func;
    }

    /**
     * Registers an XQuery function so that it will be included
     * in the environment when calling evaluateXQueryFile
     * @param func Function to include
     */
    public final void registerXQueryFunction(ExtensionFunction func) {
        this.processor.registerExtensionFunction(func);
    }

    /**
     * Executes an XQuery file (on the classpath) including all registered
     * mock functions, and returns the resulting document
     * @param filename Filename as it appears on the classpath
     * @return Returns result of execution
     * @throws XQueryException
     */
    public final XdmValue evaluateXQueryFile(String filename) throws XQueryException {
        try {
            XQueryCompiler comp = this.processor.newXQueryCompiler();
            XQueryExecutable exp = comp.compile(fromResource(filename));
            XQueryEvaluator ev = exp.load();
            return ev.evaluate();
        } catch (SaxonApiException e) {
            throw new XQueryException("An error occurred during execution of the document " + filename, e);
        } catch (IOException e) {
            throw new XQueryException("An error occurred reading the file " + filename + " from the classpath", e);

        }
    }

    private static final String fromResource(final String name) throws IOException {
        return IOUtils.toString(XQueryTestHelper.class.getResourceAsStream(name));
    }

}
