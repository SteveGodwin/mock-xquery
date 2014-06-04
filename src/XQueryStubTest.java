import static example.XQueryTestHelper.evaluateXQuery;
import static example.XQueryTestHelper.mockXQueryFunction;
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

public class XQueryStubTest {

	@Test
	public void test() throws SaxonApiException, IOException {

		//TODO: This as a builder?
		ExtensionFunction hello = mockXQueryFunction(
				"http://exmple/", "hello", new SequenceType[] {},
				SequenceType.makeSequenceType(STRING, ONE));

		when(hello.call(new XdmValue[] {})).thenReturn(
				new XdmAtomicValue("Hello World!"));

		Processor proc = new Processor(false);
		proc.registerExtensionFunction(hello);

		String result = evaluateXQuery(proc, "hello.xqy").toString();
		assertEquals("Hello World!", result);
		
		verify(hello).call(new XdmValue[] {});
	}

}
