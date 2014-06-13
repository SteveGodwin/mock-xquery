package me.stuarthicks.xqueryjunit;

import static me.stuarthicks.xqueryjunit.XQueryTestHelper.mockXQueryFunction;
import static net.sf.saxon.s9api.ItemType.STRING;
import static net.sf.saxon.s9api.OccurrenceIndicator.ONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import net.sf.saxon.s9api.ExtensionFunction;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.SequenceType;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmValue;

import org.junit.Test;
import me.stuarthicks.xqueryjunit.XQueryTestHelper;

public class XQueryStubTest {

    private static final SequenceType[] ARGUMENTS_NONE = new SequenceType[]{};
    private static final SequenceType RETURNS_STRING = SequenceType.makeSequenceType(STRING, ONE);
    private static final XdmValue[] PARAMS_NONE = new XdmValue[]{};

    private XQueryTestHelper testHelper = new XQueryTestHelper();

  @Test
  public void test() throws SaxonApiException, IOException {
    ExtensionFunction hello = mockXQueryFunction("http://exmple/", "hello", ARGUMENTS_NONE, RETURNS_STRING);

    when(hello.call(PARAMS_NONE)).thenReturn(new XdmAtomicValue("Hello World!"));

    Processor proc = new Processor(false);
    proc.registerExtensionFunction(hello);

    String result = testHelper.evaluateXQuery(proc, "/hello.xqy").toString();
    assertEquals("Hello World!", result);

    verify(hello).call(new XdmValue[] {});
  }

}
