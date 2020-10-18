package io.github.therealmone.fireres.core.utils;

import io.github.therealmone.fireres.core.config.interpolation.Point;
import lombok.val;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.therealmone.fireres.core.TestUtils.assertFunctionConstantlyGrowing;

public class RandomUtilsTest {

    public static final int CYCLES = 10000;

    @Test
    public void generatePointsInInterval() {
        for (int i = 0; i < CYCLES; i++) {
            val generatedPoints = RandomUtils.generatePointsInInterval(
                    new Point(1, 300),
                    new Point(20, 700),
                    1.0);

            assertFunctionConstantlyGrowing(generatedPoints.stream()
                    .map(Point::getTemperature)
                    .collect(Collectors.toList()));
        }
    }

    @Test
    public void generatePointsInIntervalWithLowNewPointChance() {
        for (int i = 0; i < CYCLES; i++) {
            val generatedPoints = RandomUtils.generatePointsInInterval(
                    new Point(1, 300),
                    new Point(20, 700),
                    0.1);

            assertFunctionConstantlyGrowing(generatedPoints.stream()
                    .map(Point::getTemperature)
                    .collect(Collectors.toList()));
        }
    }

    @Test
    public void generateInnerPoints() {
        for (int i = 0; i < CYCLES; i++) {
            val generatedPoints = RandomUtils.generateInnerPoints(
                    List.of(
                            new Point(0, 21),
                            new Point(1, 323),
                            new Point(5, 500),
                            new Point(8, 900)),
                    1.0);

            assertFunctionConstantlyGrowing(generatedPoints.stream()
                    .map(Point::getTemperature)
                    .collect(Collectors.toList()));
        }
    }

}
