package io.github.therealmone.fireres.core.factory;

import io.github.therealmone.fireres.core.config.GenerationProperties;
import io.github.therealmone.fireres.core.generator.FurnaceTempGenerator;
import io.github.therealmone.fireres.core.generator.MaxAllowedTempGenerator;
import io.github.therealmone.fireres.core.generator.MinAllowedTempGenerator;
import io.github.therealmone.fireres.core.generator.StandardTempGenerator;
import io.github.therealmone.fireres.core.generator.ThermocoupleMeanGenerator;
import io.github.therealmone.fireres.core.model.StandardTemperature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
public class NumberSequenceGeneratorFactory {

    private final GenerationProperties generationProperties;

    public StandardTempGenerator standardTempGenerator() {
        return new StandardTempGenerator(
                generationProperties.getTemperature().getEnvironmentTemperature(),
                generationProperties.getTime());
    }

    public FurnaceTempGenerator furnaceTempGenerator(StandardTemperature standardTemp) {
        return new FurnaceTempGenerator(
                generationProperties.getTemperature().getEnvironmentTemperature(),
                standardTemp);
    }

    public MinAllowedTempGenerator minAllowedTempGenerator(StandardTemperature standardTemp) {
        return new MinAllowedTempGenerator(
                generationProperties.getTemperature().getMinAllowedTempCoefficients(),
                standardTemp);
    }

    public MaxAllowedTempGenerator maxAllowedTempGenerator(StandardTemperature standardTemp) {
        return new MaxAllowedTempGenerator(
                generationProperties.getTemperature().getMaxAllowedTempCoefficients(),
                standardTemp);
    }

    public ThermocoupleMeanGenerator thermocoupleMeanGenerator() {
        return new ThermocoupleMeanGenerator(
                generationProperties.getTemperature().getEnvironmentTemperature(),
                generationProperties.getTime(),
                generationProperties.getInterpolation().getInterpolationPoints(),
                generationProperties.getInterpolation().getInterpolationMethod(),
                generationProperties.getRandomPoints().getEnrichWithRandomPoints(),
                generationProperties.getRandomPoints().getNewPointChance(),
                generationProperties.getRandomPoints().getMinDelta());
    }

}
