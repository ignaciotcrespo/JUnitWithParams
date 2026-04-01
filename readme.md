# JUnitWithParams

*Parameterised tests, it works with any Runner (Robolectric, Spring, Mockito, and more!)*

![build status](https://img.shields.io/badge/build-info%20=%3E-yellow.svg)
[![Build Status](https://travis-ci.org/ignaciotcrespo/JUnitWithParams.svg?branch=master)](https://travis-ci.org/ignaciotcrespo/JUnitWithParams)
[![codecov.io](http://codecov.io/github/ignaciotcrespo/JUnitWithParams/coverage.svg?branch=master)](http://codecov.io/github/ignaciotcrespo/JUnitWithParams?branch=master)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ignaciotcrespo/junitwithparams/blob/master/LICENSE)

## Example, single parameter

```java
public class CalculatorTest {
 
  @Rule
  public WithParamsRule params = new WithParamsRule();
 
  @Test
  @WithParams({"2", "4", "8", "1000"})
  public void isEven() throws Exception {
        assertTrue(calculator.isEven(params.asInt()));
  }
  
}
```


## Example, multiple parameters

```java
public class CalculatorTest {
 
  @Rule
  public WithParamsRule params = new WithParamsRule();
 
  @Test
  @WithParams(
        names = {"n1", "n2", "result"},
        value = {
                "1", "2", "3",
                "11", "-2", "9"
        }
  )
  public void sum() throws Exception {
        int n1 = params.asInt("n1");
        int n2 = params.asInt("n2");
 
        int result = calculator.sum(n1, n2);
 
        assertEquals(params.asInt("result"), result);
  }
  
}
```

## `@WithBooleanParams` — true / false shorthand

Runs the test twice: once with `true` and once with `false`. No need to list the values manually.

```java
@Test
@WithBooleanParams
public void toggleFeature() {
    boolean enabled = params.asBoolean();
    feature.setEnabled(enabled);
    assertEquals(enabled, feature.isEnabled());
}
```

## `@WithNullParam` — null-safety testing

Runs the test twice: once with the provided non-null value and once with `null`.
Ideal for verifying that your code handles `null` inputs gracefully.

```java
@Test
@WithNullParam("hello")
public void handlesNull() {
    String value = params.get(); // "hello" on the first run, null on the second
    processValue(value);         // must not throw for either case
}
```

Use the optional `name` attribute for named parameters:

```java
@Test
@WithNullParam(value = "hello", name = "input")
public void handlesNullNamed() {
    String value = params.get("input");
}
```

## `@WithEnumParams` — iterate over all enum constants

Automatically runs the test once for every constant in the specified enum class.
The current constant is retrieved via the type-safe `asEnum(Class)` accessor.

```java
enum Color { RED, GREEN, BLUE }

@Test
@WithEnumParams(Color.class)
public void testAllColors() {
    Color color = params.asEnum(Color.class);
    assertNotNull(color);
    renderBackground(color); // called for RED, GREEN, and BLUE
}
```

Use the optional `name` attribute for named parameters:

```java
@Test
@WithEnumParams(value = Color.class, name = "color")
public void testAllColorsNamed() {
    Color color = params.asEnum("color", Color.class);
    assertNotNull(color);
}
```

## `@WithParamsSource` — method-provided parameter sets

Reads parameter sets from a static method in the test class.
Avoids large annotation arrays and keeps complex data sets as real Java code.

The provider method must be `static`, accept no arguments, and return `String[][]`
where each inner array is one test iteration. The `names` attribute maps column
indices to parameter names.

```java
@Test
@WithParamsSource(value = "provideNumbers", names = {"n1", "n2", "result"})
public void sum() {
    int n1 = params.asInt("n1");
    int n2 = params.asInt("n2");
    assertEquals(params.asInt("result"), calculator.sum(n1, n2));
}

static String[][] provideNumbers() {
    return new String[][] {
        {"1",  "2",  "3"},
        {"11", "-2", "9"}
    };
}
```

Single-parameter shorthand (default name `"param1"`):

```java
@Test
@WithParamsSource("provideWords")
public void wordIsNotEmpty() {
    String word = params.get();
    assertFalse(word.isEmpty());
}

static String[][] provideWords() {
    return new String[][] { {"hello"}, {"world"}, {"foo"} };
}
```

## `asEnum(Class)` — type-safe enum accessor

Converts the current string parameter value to an enum constant by name.
Returns `null` if the stored value is `null` (e.g. when used with `@WithNullParam`).

```java
// default parameter
Color color = params.asEnum(Color.class);

// named parameter
Color color = params.asEnum("colorParam", Color.class);
```

The accessor works with any enum and complements the existing typed accessors
(`asInt()`, `asBoolean()`, `asDouble()`, etc.).

## Kotlin

Kotlin is supported, with some small differences.

Create rule:
```
@get:Rule var params = WithParamsRule()
```

The format for the arrays in the annotations
```
  @WithParams(
        names = ["n1", "n2", "result"],
        value = [
                "1", "2", "3",
                "11", "-2", "9"
        ]
  )
```

## About

JUnitWithParams uses the rule `WithParamsRule` to parse the annotation `@WithParams`

Very similar to the great library [JUnitParams](https://github.com/Pragmatists/JUnitParams)
but not using a JUnit runner, this allows to use parameterised tests with the most used runners (Spring, Robolectric, AndroidJUnit4)

# Current Version
* The current stable version is `1.0.7`

# Android support
JUnitWithParams works fine in android, it is compiled with JDK 1.6 and tested on real projects.

## Quickstart

JUnitWithParams is published to [GitHub Packages](https://github.com/ignaciotcrespo/JUnitWithParams/packages).

**Gradle:**
```groovy
repositories {
    maven { url "https://maven.pkg.github.com/ignaciotcrespo/JUnitWithParams" }
}

dependencies {
    testImplementation 'com.github.ignaciotcrespo:junitwithparams:1.0.7'
}
```

**Maven:**
```xml
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/ignaciotcrespo/JUnitWithParams</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.ignaciotcrespo</groupId>
  <artifactId>junitwithparams</artifactId>
  <version>1.0.7</version>
  <scope>test</scope>
</dependency>
```

> **Note:** GitHub Packages requires authentication. See [GitHub's guide](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) for setup.

## Releasing a new version

Create and push a version tag:
```bash
git tag v1.0.8
git push origin v1.0.8
```

This triggers a GitHub Actions workflow that publishes the artifact to GitHub Packages. The version is derived from the tag name automatically.

# Contribution
JUnitWithParams is a work in progress, it is stable but of course there are still some edge cases not covered.

You are welcome to contribute to the project, feel free to create a pull request with your changes.

For questions, suggestions or feedback, create an issue in this repository.

# License

JUnitWithParams is released under the [![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT).

```
The MIT License

Copyright (c) 2017, Ignacio Tomas Crespo (itcrespo@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
