package me.stuarthicks.xqueryjunit;

import net.sf.saxon.s9api.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public final class XQueryTestContext {

    private final Processor processor;

    public XQueryTestContext() {
        this.processor = new Processor(false);
    }

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

        private String namespaceURI;
        private String functionName;
        private SequenceType[] argumentTypes;
        private SequenceType resultType;
        private XdmValue resultValue;

        public MockXQueryFunctionBuilder(Processor processor) {
            this.processor = processor;
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

        public MockXQueryFunctionBuilder withReturnValue(final XdmValue returnValue) {
            this.resultValue = returnValue;
            return this;
        }

        public void done() {
            MockXQueryFunctionBuilder.this.processor.registerExtensionFunction(new ExtensionFunction() {
                @Override
                public QName getName() {
                    return new QName(MockXQueryFunctionBuilder.this.namespaceURI, MockXQueryFunctionBuilder.this.functionName);
                }

                @Override
                public SequenceType getResultType() {
                    return MockXQueryFunctionBuilder.this.resultType;
                }

                @Override
                public SequenceType[] getArgumentTypes() {
                    return MockXQueryFunctionBuilder.this.argumentTypes;
                }

                @Override
                public XdmValue call(XdmValue[] xdmValues) throws SaxonApiException {
                    // We are currently throwing away the actual arguments passed to our
                    // mock function and simply returning a value each time.
                    // How should we inspect the arguments? ArgumentCaptor of some sort?
                    return MockXQueryFunctionBuilder.this.resultValue;
                }
            });
        }
    }

    private static final String fromResource(final String name) throws IOException {
        return IOUtils.toString(XQueryTestContext.class.getResourceAsStream(name));
    }

}
