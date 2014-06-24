package me.stuarthicks.xqueryjunit;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.*;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.SequenceType;
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

    public static final class MockXQueryFunctionBuilder {

        private final Processor processor;

        private String prefix;
        private String namespaceURI;
        private String functionName;
        private SequenceType[] argumentTypes;
        private SequenceType resultType;
        private AtomicValue resultValue;

        public MockXQueryFunctionBuilder(Processor processor) {
            this.processor = processor;
        }

        public MockXQueryFunctionBuilder withPrefix(final String prefix) {
            this.prefix = prefix;
            return this;
        }

        public MockXQueryFunctionBuilder withNamespaceURI(final String namespace) {
            this.namespaceURI = namespace;
            return this;
        }

        public MockXQueryFunctionBuilder withFunctionName(final String name) {
            this.functionName = name;
            return this;
        }

        public MockXQueryFunctionBuilder withFunctionSignature(final SequenceType[] arguments) {
            this.argumentTypes = arguments;
            return this;
        }

        public MockXQueryFunctionBuilder withReturnType(final SequenceType returnType) {
            this.resultType = returnType;
            return this;
        }

        public MockXQueryFunctionBuilder withReturnValue(final AtomicValue returnValue) {
            this.resultValue = returnValue;
            return this;
        }

        public void done() {
            MockXQueryFunctionBuilder.this.processor.registerExtensionFunction(new ExtensionFunctionDefinition() {
                @Override
                public StructuredQName getFunctionQName() {
                    return new StructuredQName(
                            MockXQueryFunctionBuilder.this.prefix,
                            MockXQueryFunctionBuilder.this.namespaceURI,
                            MockXQueryFunctionBuilder.this.functionName
                    );
                }

                @Override
                public SequenceType[] getArgumentTypes() {
                    return MockXQueryFunctionBuilder.this.argumentTypes;
                }

                @Override
                public SequenceType getResultType(net.sf.saxon.value.SequenceType[] sequenceTypes) {
                    return MockXQueryFunctionBuilder.this.resultType;
                }

                @Override
                public ExtensionFunctionCall makeCallExpression() {
                    return new ExtensionFunctionCall() {
                        @Override
                        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
                            //TODO Provide a way for tester to verify actual invocation arguments
                            return MockXQueryFunctionBuilder.this.resultValue;
                        }
                    };
                }
            });
        }
    }

    private static final String fromResource(final String name) throws IOException {
        return IOUtils.toString(XQueryTestContext.class.getResourceAsStream(name));
    }

}
