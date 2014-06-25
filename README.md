xqueryjunit
============
License: Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

## What is this?

A way to run XQuery within a Java app using Saxon, and to provide stub methods to stand-in for external functions that aren't present (such as the XQuery extensions provided by MarkLogic).

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

We can stub the hello method, evaluate the document, and inspect the results with the following method:
```java
@Test
public void itShouldMockSingleStringArgFunctionReturningString() throws XQueryException {
    XQueryFunctionStub hello = xq.buildXQueryFunctionStub()
            .withNamespaceURI("http://example/")
            .withPrefix("example")
            .withFunctionName("hello")
            .withFunctionSignature(XQueryConstants.ARGUMENTS_SINGLE_STRING)
            .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
            .withReturnValue(new StringValue("Hello World!"))
            .done();

    String result = xq.evaluateXQueryFile("/hello_with_arg.xqy").toString();

    assertEquals("\"World!\"", hello.getArguments().get(0).toString());
    assertEquals(1, hello.getNumberOfInvocations());

    assertEquals("Hello World!", result);
}
```

