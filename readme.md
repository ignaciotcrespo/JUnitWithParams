# JUnitWithParams

*Parameterised tests, it works with any Runner (Robolectric, Spring, Mockito, and more!)*

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

