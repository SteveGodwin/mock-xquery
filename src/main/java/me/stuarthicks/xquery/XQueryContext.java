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
    private boolean stubNamespaceRequired = false;

    public XQueryContext () {
        this.processor = new Processor(false);
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
     * Entry point to the overriding of a core XQuery function. Currently only
     * redefining fn:doc() is supported.
     * @return
     * @param prefixAndName
     *            Eg. "fn:doc"
     */
    public final XQueryFunctionStubBuilder buildXQueryCoreFunctionStub (String prefixAndName) {
        this.stubNamespaceRequired = true;
        return new XQueryFunctionStubBuilder(this.processor)
                .withPrefix("stub")
                .withNamespaceURI("http://stub/")
                .withFunctionName(prefixAndName.substring(prefixAndName.indexOf(":") + 1));
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
        //TODO inter-file dependencies? Do they work out of the box or do I need to scan?
        try {
            XQueryCompiler comp = this.processor.newXQueryCompiler();
            comp.setLanguageVersion(xqueryVersion);
            String query = fromResource(filename);
            XQueryExecutable exp = comp.compile(injectStubNamespace(query));
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

    /**
     * Injects a "stub" namespace, then replaces certain known problematic
     * function calls to be intercepted with that namespace. Ugly, nasty hack
     * because Saxon doesn't let me reorder it's "path" to find
     * ExtensionFunctions first. Ultimately, this lets me stub core functions.
     * Should be used as last resort.
     * @param query
     * @return
     */
    private String injectStubNamespace (String query) {
        // FIXME: Replace this hack with a uri resolver
        if (!this.stubNamespaceRequired) return query;
        return String.format("%s\n%s",
                "declare namespace stub = \"http://stub/\";",
                query.replaceAll("(?:\\W)fn:doc\\(", "stub:doc(")
                );
    }

    private static String fromResource (final String name) throws IOException {
        return IOUtils.toString(XQueryContext.class.getResourceAsStream(name));
    }

}
