package com.github.ignaciotcrespo.junitwithparams;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Integration tests for {@link WithEnumParams}.
 *
 * <p>Verifies that every enum constant is visited exactly once and that
 * {@link WithParamsRule#asEnum(Class)} and {@link WithParamsRule#asEnum(String, Class)}
 * return the correct typed constant.
 */
public class WithEnumParamsTest {

    enum Color { RED, GREEN, BLUE }

    enum Direction { NORTH, SOUTH, EAST, WEST }

    enum Single { ONLY }

    @Rule
    public WithParamsRule params = new WithParamsRule();

    // --- state for testedForEachEnumConstant (Color, default name) ---
    private static Set<Color> testedColors;

    // --- state for testedForEachDirectionNamed (Direction, named param) ---
    private static Set<Direction> testedDirections;

    // --- state for singleConstantIteratesOnce ---
    private static int singleIterationCount;

    @BeforeClass
    public static void beforeClass() {
        testedColors = new HashSet<Color>();
        testedDirections = new HashSet<Direction>();
        singleIterationCount = 0;
    }

    @Test
    @WithEnumParams(Color.class)
    public void testedForEachEnumConstant() {
        Color color = params.asEnum(Color.class);

        assertNotNull("Enum value should not be null", color);
        if (testedColors.contains(color)) fail("Color " + color + " was already tested!");
        testedColors.add(color);
    }

    @Test
    @WithEnumParams(value = Direction.class, name = "dir")
    public void testedForEachDirectionNamed() {
        Direction dir = params.asEnum("dir", Direction.class);

        assertNotNull(dir);
        if (testedDirections.contains(dir)) fail("Direction " + dir + " was already tested!");
        testedDirections.add(dir);
    }

    @Test
    @WithEnumParams(Single.class)
    public void singleConstantIteratesOnce() {
        Single value = params.asEnum(Single.class);
        assertEquals(Single.ONLY, value);
        singleIterationCount++;
    }

    @AfterClass
    public static void afterClass() {
        // Color assertions
        assertEquals("All Color constants should be tested", Color.values().length, testedColors.size());
        assertTrue(testedColors.contains(Color.RED));
        assertTrue(testedColors.contains(Color.GREEN));
        assertTrue(testedColors.contains(Color.BLUE));

        // Direction assertions
        assertEquals("All Direction constants should be tested", Direction.values().length, testedDirections.size());
        assertTrue(testedDirections.contains(Direction.NORTH));
        assertTrue(testedDirections.contains(Direction.SOUTH));
        assertTrue(testedDirections.contains(Direction.EAST));
        assertTrue(testedDirections.contains(Direction.WEST));

        // Single constant
        assertEquals("Single-constant enum should iterate exactly once", 1, singleIterationCount);
    }
}
