package me.stuarthicks.xquery;

import java.io.IOException;

import me.stuarthicks.xquery.stubbing.XQueryFunctionStubBuilder;
import net.sf.saxon.expr.instruct.UserFunction;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.trans.XPathException;

import org.apache.commons.io.IOUtils;

public final class XQueryContext {

    private final Processor processor;

    public XQueryContext () {
        Processor p = new Processor(false);
        p.getUnderlyingConfiguration().setModuleURIResolver(new ModuleResourceURIResolver());
        p.getUnderlyingConfiguration().setURIResolver(new ResourceURIResolver());
        this.processor = p;
    }

    /**
     * Entry point to the creation of an XQuery function stub. Specify all
     * fields then call done(). The created function is automatically registered
     * to the XQueryContext and will exist in the environment when some XQuery
     * is next evaluated.
     * @return A builder. Go, build!
     */
    public final XQueryFunctionStubBuilder buildXQueryFunctionStub () {
        return new XQueryFunctionStubBuilder(this.processor);
    }

    /**
     * Executes an XQuery file (on the classpath) including all registered stub
     * functions, and returns the resulting document
     * @param filename
     *            Filename as it appears on the classpath
     * @return Returns result of execution
     * @throws XQueryException
     */
    public final XdmValue evaluateXQueryFile (String filename) throws XQueryException {
        return evaluateXQueryFile(filename, "1.0");
    }

    /**
     * Executes an XQuery file (on the classpath) under a given version of
     * XQuery
     * @param filename
     *            Filename as it appears on the classpath
     * @param xqueryVersion
     *            Must be "1.0" or "3.0"
     * @return Returns result of execution
     * @throws XQueryException
     */
    public final XdmValue evaluateXQueryFile (String filename, String xqueryVersion) throws XQueryException {
        try {
            XQueryCompiler comp = this.processor.newXQueryCompiler();
            comp.setLanguageVersion(xqueryVersion);
            String query = fromResource(filename);
            XQueryExecutable exp = comp.compile(query);
            XQueryEvaluator ev = exp.load();
            return ev.evaluate();
        }
        catch (SaxonApiException | IOException e) {
            throw new XQueryException("An error occurred during execution of the document " + filename, e);
        }
    }

    public final Sequence callXQueryFunction (String filename, String namespaceURI, String functionName, Sequence[] args) throws XQueryException {
        Sequence[] actualArgs = (args == null) ? new Sequence[] {} : args;
        try {
            StaticQueryContext sqc = this.processor.getUnderlyingConfiguration().newStaticQueryContext();
            XQueryExpression exp = sqc.compileQuery(fromResource(filename));
            int numOfArgs = (args == null) ? 0 : args.length;
            UserFunction function = exp.getStaticContext().getUserDefinedFunction(namespaceURI, functionName, numOfArgs);
            return function.call(actualArgs, exp.newController());
        }
        catch (XPathException | IOException e) {
            throw new XQueryException("Unable to call function " + namespaceURI + ":" + functionName, e);
        }
    }

    private static String fromResource (final String name) throws IOException {
        return IOUtils.toString(XQueryContext.class.getResourceAsStream(name));
    }
}
