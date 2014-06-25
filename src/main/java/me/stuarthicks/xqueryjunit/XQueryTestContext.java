package me.stuarthicks.xqueryjunit;

import net.sf.saxon.s9api.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public final class XQueryTestContext {

    private final Processor processor;

    public XQueryTestContext() {
        this.processor = new Processor(false);
    }

    /**
     * Entry point to the creation of an xquery function stub.
     * Specify all fields then call done().
     * @return
     */
    public final MockXQueryFunctionBuilder mockXQueryFunction() {
        return new MockXQueryFunctionBuilder(this.processor);
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
        return IOUtils.toString(XQueryTestContext.class.getResourceAsStream(name));
    }

}
