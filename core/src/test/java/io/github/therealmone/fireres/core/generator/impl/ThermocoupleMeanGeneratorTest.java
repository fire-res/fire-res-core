package io.github.therealmone.fireres.core.generator.impl;

import io.github.therealmone.fireres.core.config.GenerationProperties;
import io.github.therealmone.fireres.core.config.interpolation.InterpolationMethod;
import io.github.therealmone.fireres.core.config.interpolation.InterpolationPoints;
import io.github.therealmone.fireres.core.config.interpolation.InterpolationProperties;
import io.github.therealmone.fireres.core.config.sample.SampleProperties;
import io.github.therealmone.fireres.core.model.Point;
import io.github.therealmone.fireres.core.config.random.RandomPointsProperties;
import io.github.therealmone.fireres.core.config.temperature.TemperatureProperties;
import io.github.therealmone.fireres.core.factory.PointSequenceGeneratorFactory;
import lombok.val;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.github.therealmone.fireres.core.TestUtils.assertFunctionConstantlyGrowing;
import static org.junit.Assert.assertEquals;

public class ThermocoupleMeanGeneratorTest {

    private static final InterpolationPoints INTERPOLATION_POINTS = new InterpolationPoints(new ArrayList<>() {{
        add(new Point(0, 21));
        add(new Point(1, 306));
        add(new Point(18, 749));
        add(new Point(21, 789));
        add(new Point(26, 822));
        add(new Point(48, 898));
        add(new Point(49, 901));
        add(new Point(70, 943));
    }});

    @Test
    public void linearInterpolationMethod() {
        val factory = new PointSequenceGeneratorFactory(GenerationProperties.builder()
                .temperature(TemperatureProperties.builder()
                        .environmentTemperature(21)
                        .build())
                .time(71)
                .samples(List.of(SampleProperties.builder()
                        .randomPoints(RandomPointsProperties.builder()
                                .enrichWithRandomPoints(false)
                                .build())
                        .interpolation(InterpolationProperties.builder()
                                .interpolationPoints(INTERPOLATION_POINTS)
                                .interpolationMethod(InterpolationMethod.LINEAR)
                                .build())
                        .build()))
                .build());

        val thermocoupleMeanTemp = factory.thermocoupleMeanGenerator(0).generate().getValue();

        assertInterpolationPoints(thermocoupleMeanTemp);
        assertFunctionConstantlyGrowing(thermocoupleMeanTemp);
    }

    @Test
    public void linearInterpolationMethodWithRandomPoints() {
        val factory = new PointSequenceGeneratorFactory(GenerationProperties.builder()
                .temperature(TemperatureProperties.builder()
                        .environmentTemperature(21)
                        .build())
                .time(71)
                .samples(List.of(SampleProperties.builder()
                        .randomPoints(RandomPointsProperties.builder()
                                .enrichWithRandomPoints(true)
                                .newPointChance(1.0)
                                .build())
                        .interpolation(InterpolationProperties.builder()
                                .interpolationPoints(INTERPOLATION_POINTS)
                                .interpolationMethod(InterpolationMethod.LINEAR)
                                .build())
                        .build()))
                .build());

        val thermocoupleMeanTemp = factory.thermocoupleMeanGenerator(0).generate().getValue();

        assertInterpolationPoints(thermocoupleMeanTemp);
        assertFunctionConstantlyGrowing(thermocoupleMeanTemp);
    }

    @Test
    public void linearInterpolationMethodWithRandomPointsAndLowNewPointChance() {
        val factory = new PointSequenceGeneratorFactory(GenerationProperties.builder()
                .temperature(TemperatureProperties.builder()
                        .environmentTemperature(21)
                        .build())
                .time(71)
                .samples(List.of(SampleProperties.builder()
                        .randomPoints(RandomPointsProperties.builder()
                                .enrichWithRandomPoints(true)
                                .newPointChance(0.1)
                                .build())
                        .interpolation(InterpolationProperties.builder()
                                .interpolationPoints(INTERPOLATION_POINTS)
                                .interpolationMethod(InterpolationMethod.LINEAR)
                                .build())
                        .build()))
                .build());

        val thermocoupleMeanTemp = factory.thermocoupleMeanGenerator(0).generate().getValue();

        assertInterpolationPoints(thermocoupleMeanTemp);
        assertFunctionConstantlyGrowing(thermocoupleMeanTemp);
    }

    private void assertInterpolationPoints(List<Point> function) {
        for (Point point : INTERPOLATION_POINTS.getPoints()) {
            assertEquals(point, function.stream()
                    .filter(p -> p.getTime().equals(point.getTime())).findFirst()
                    .orElseThrow());
        }
    }

}