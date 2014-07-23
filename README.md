xquery-stubbing
===============
License: Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

## What is this?

A way to run [XQuery](http://www.w3.org/XML/Query/) within a Java app using [Saxon](http://saxon.sourceforge.net/), and to provide stub methods to stand-in for external functions that aren't present (such as the XQuery extensions provided by [MarkLogic](http://www.marklogic.com/)).

## Why?

Primarily for testing purposes. My use-case is to write tests for XQuery intended to be deployed on a MarkLogic application server. All current ways I can find of testing MarkLogic XQuery involve running a MarkLogic instance somewhere and deploying to there to run tests. NO THANKS. I want simple unit tests I can run locally without any fancy setup. As my XQuery is designed to complement a larger Java component, being able to keep the two together and have the XQuery tested with the Java is convenient.

## Usage

### Maven dependency

xquery-stubbing is available in maven-central! The latest stable release is:

```xml
<dependency>
  <groupId>me.stuarthicks</groupId>
  <artifactId>xquery-stubbing</artifactId>
  <version>1.0.0</version>
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

What if your xquery file is a library file and only contains function definitions? Well you can also call a function in the document and unit-test it's output alone. Let's take the following XQuery document:

```xquery
declare namespace example = "http://example/";

declare function example:hi() {
    example:hello()
};

"no-op"
```

In this case, we want to stub `example:hello()` and test it by calling `example:hi()`. We can do that like this:

```java
@Test
public void itShouldBeAbleToCallSpecificFunctionInXQueryFile () throws Exception {
  helloStubber()
    .withFunctionSignature(XQueryConstants.ARGUMENTS_NONE)
    .withReturnType(XQueryConstants.RETURNS_SINGLE_STRING)
    .withReturnValue(new StringValue("Hello World!"))
    .done();

  String result = this.xq.callXQueryFunction("/hello_in_lib_function.xqy", NAMESPACE, "hi", null).head().getStringValue();

  assertEquals("Hello World!", result);
}
```

This allows a lot of flexibility in testing small chunks of XQuery and if you keep you external dependencies untangled from your code, you should be able to minimise the amount of stubbing needed.

Methods stubbed via the XQueryContext are automatically registered and will be present in the next call to `evaluateXQueryFile()`. There is no provided way to delete stubs, simply throw away the context and create a new one (or create the context in your before method).

Also worth noting, calling withReturnValue multiple times in the builder causes each one to be remembered and played back by the stub function in order (with the last one repeated if the stub is called additional times). This is intended to match the behaviour you see in mockito.

## Limitations

URI Resolvers have been implemented to allow document operations and imports to work normally (suggestion: use multiple `at` hints), but they aren't extensively tested. Please report any issues you have with those functions. There are also kinks to be worked out regarding XQuery version declarations and the way Saxon deals with (or rather doesn't) "unknown" versions. If you're testing this with code meant for MarkLogic, you'll probably encounter problems with `xquery version "1.0-ml";`. I'll update this when I have an acceptable solution.
