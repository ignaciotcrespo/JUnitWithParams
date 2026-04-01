package com.github.ignaciotcrespo.junitwithparams;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration tests for {@link WithParamsSource}.
 *
 * <p>Covers:
 * <ul>
 *   <li>Multi-parameter source method with named params ({@link #sum()})</li>
 *   <li>Single-parameter source method with default param name ({@link #wordIsNotEmpty()})</li>
 *   <li>Empty source method producing zero iterations ({@link #neverRunsForEmpty()})</li>
 *   <li>Null-returning source method producing zero iterations ({@link #neverRunsForNull()})</li>
 * </ul>
 */
public class WithParamsSourceTest {

    @Rule
    public WithParamsRule params = new WithParamsRule();

    private static List<Integer> sumResults;
    private static List<String> words;
    private static int emptyIterations;
    private static int nullIterations;

    @BeforeClass
    public static void beforeClass() {
        sumResults = new ArrayList<Integer>();
        words = new ArrayList<String>();
        emptyIterations = 0;
        nullIterations = 0;
    }

    // -----------------------------------------------------------------------
    // Multi-parameter: named params
    // -----------------------------------------------------------------------

    @Test
    @WithParamsSource(value = "provideSums", names = {"n1", "n2", "result"})
    public void sum() {
        int n1 = params.asInt("n1");
        int n2 = params.asInt("n2");
        int expected = params.asInt("result");
        assertEquals(expected, n1 + n2);
        sumResults.add(expected);
    }

    static String[][] provideSums() {
        return new String[][]{
                {"1", "2", "3"},
                {"11", "-2", "9"},
                {"0", "0", "0"}
        };
    }

    // -----------------------------------------------------------------------
    // Single-parameter: default param name
    // -----------------------------------------------------------------------

    @Test
    @WithParamsSource("provideWords")
    public void wordIsNotEmpty() {
        String word = params.get();
        assertTrue("Word should not be empty", word != null && !word.isEmpty());
        words.add(word);
    }

    static String[][] provideWords() {
        return new String[][]{
                {"hello"},
                {"world"},
                {"foo"}
        };
    }

    // -----------------------------------------------------------------------
    // Empty source — zero iterations
    // -----------------------------------------------------------------------

    @Test
    @WithParamsSource("provideEmpty")
    public void neverRunsForEmpty() {
        emptyIterations++;
    }

    static String[][] provideEmpty() {
        return new String[0][];
    }

    // -----------------------------------------------------------------------
    // Null source — zero iterations
    // -----------------------------------------------------------------------

    @Test
    @WithParamsSource("provideNull")
    public void neverRunsForNull() {
        nullIterations++;
    }

    static String[][] provideNull() {
        return null;
    }

    // -----------------------------------------------------------------------
    // @AfterClass verification
    // -----------------------------------------------------------------------

    @AfterClass
    public static void afterClass() {
        // sum
        assertEquals("Expected 3 sum iterations", 3, sumResults.size());
        assertTrue(sumResults.contains(3));
        assertTrue(sumResults.contains(9));
        assertTrue(sumResults.contains(0));

        // words
        assertEquals("Expected 3 word iterations", 3, words.size());
        assertTrue(words.contains("hello"));
        assertTrue(words.contains("world"));
        assertTrue(words.contains("foo"));

        // empty / null source
        assertEquals("Empty source should produce zero iterations", 0, emptyIterations);
        assertEquals("Null source should produce zero iterations", 0, nullIterations);
    }
}
