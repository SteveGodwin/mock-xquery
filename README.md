xqueryjunit
============
License: Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

## What is this?

A way to run [XQuery](http://www.w3.org/XML/Query/) within a Java app using [Saxon](http://saxon.sourceforge.net/), and to provide stub methods to stand-in for external functions that aren't present (such as the XQuery extensions provided by [MarkLogic](http://www.marklogic.com/)).

## Why?

Primarily for testing purposes. My use-case is to write tests for XQuery intended to be deployed on a MarkLogic application server. All current ways I can find of testing MarkLogic XQuery involve running a MarkLogic instance somewhere and deploying to there to run tests. NO THANKS. I want simple unit tests I can run locally without any fancy setup. As my XQuery is designed to complement a larger Java component, being able to keep the two together and have the XQuery tested with the Java is convenient.

## Usage

### Maven dependency

There is currently no release version and this has not been pushed into any public maven repository. Build locally then include the following dependency in your pom.xml:

```xml
<dependency>
  <groupId>me.stuarthicks</groupId>
  <artifactId>xqueryjunit</artifactId>
  <version>1-SNAPSHOT</version>
</dependency>
```

### Stubbing Example

See the tests for more examples, but put simply, use the provided builder to stub your functions. Optionally you can maintain a reference to each stubbed function and after executing your XQuery you can inspect the function to see what parameters it was called with and how many times it was called. Useful for asserts in unit tests.

Let's take this example XQuery document `hello_with_arg.xqy`:
```xquery
declare namespace example = "http://example/";
example:hello("World!")
```

We can stub the hello method, evaluate the document, and inspect the results with the following technique:
```java
@Test
public void itShouldMockSingleStringArgFunctionReturningString() throws XQueryException {
  XQueryContext xq = new XQueryContext();
  XQueryFunctionStub hello = xq.buildXQueryFunctionStub()
    .withNamespaceURI("http://example/")
    .withPrefix("example")
    .withFunctionName("hello")
    .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_STRING)
    .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
    .withReturnValue(new StringValue("Hello World!"))
    .done();

  String result = xq.evaluateXQueryFile("/hello_with_arg.xqy").toString();

  assertEquals("World!", hello.getArguments().get(0).toString());
  assertEquals(1, hello.getNumberOfInvocations());

  assertEquals("Hello World!", result);
}
```

What if you want to do something icky like stub out a core xquery function like fn:doc()? Well you can! (but please, use it as a last resort)
```java
@Test
public void itShouldMockCoreFunctions () throws Exception {
  XQueryFunctionStub doc = this.xq.buildXQueryCoreFunctionStub("fn:doc")
    .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_STRING)
    .withReturnType(XQueryConstants.RETURNS_SINGLE_NODE)
    .withReturnValue(nodeFromFile("/foobar.xml"))
    .done();

  String result = this.xq.evaluateXQueryFile("/fn-doc.xqy").toString().trim();

  assertEquals("foo", doc.getArguments().get(0));
  assertEquals("<bar/>", result);
}
```

Methods stubbed via the XQueryContext are automatically registered and will be present in the next call to `evaluateXQueryFile()`. There is no provided way to delete stubs, simply throw away the context and create a new one (or create the context in your before method).

Also worth noting, calling withReturnValue multiple times in the builder causes each one to be remembered and played back by the stub function in order (with the last one repeated if the stub is called additional times). This is intended to match the behaviour you see in mockito.

## Limitations

The way I'm currently stubbbing out core functions is a hack. Don't expect it to be reliable and use it as a last resort. See issue #4 for progress on this.
