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

## About

JUnitWithParams uses the rule `WithParamsRule` to parse the annotation `@WithParams`

Very similar to the great library [JUnitParams](https://github.com/Pragmatists/JUnitParams)
but not using a JUnit runner, this allows to use parameterised tests with the most used runners (Spring, Robolectric, AndroidJUnit4)

# Current Version
* The current stable version is `1.0.0`
* The current snapshot version is `1.0.1-SNAPSHOT`

# Android support
JUnitWithParams works fine in android, it is compiled with JDK 1.6 and tested on real projects.

## Quickstart

JUnitWithParams is available as Maven artifact:
```
<dependency>
  <groupId>com.github.ignaciotcrespo</groupId>
  <artifactId>junitwithparams</artifactId>
  <version>1.0.0</version>
  <scope>test</scope>
</dependency>
```
To use JUnitWithParams in a Gradle build add this to your dependencies:

```
testCompile 'com.github.ignaciotcrespo:junitwithparams:1.0.0'
```

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