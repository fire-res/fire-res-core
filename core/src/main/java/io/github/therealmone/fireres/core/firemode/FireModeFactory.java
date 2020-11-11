package io.github.therealmone.fireres.core.firemode;

import io.github.therealmone.fireres.core.common.config.GenerationProperties;
import io.github.therealmone.fireres.core.firemode.generator.FurnaceTempGenerator;
import io.github.therealmone.fireres.core.firemode.generator.MaxAllowedTempGenerator;
import io.github.therealmone.fireres.core.firemode.generator.MinAllowedTempGenerator;
import io.github.therealmone.fireres.core.firemode.generator.StandardTempGenerator;
import io.github.therealmone.fireres.core.common.generator.MeanFunctionGenerator;
import io.github.therealmone.fireres.core.common.generator.MeanChildFunctionsGenerator;
import io.github.therealmone.fireres.core.firemode.model.FurnaceTemperature;
import io.github.therealmone.fireres.core.firemode.model.MaxAllowedTemperature;
import io.github.therealmone.fireres.core.firemode.model.MinAllowedTemperature;
import io.github.therealmone.fireres.core.firemode.model.StandardTemperature;
import io.github.therealmone.fireres.core.firemode.model.ThermocoupleMeanTemperature;
import io.github.therealmone.fireres.core.firemode.model.ThermocoupleTemperature;
import io.github.therealmone.fireres.core.common.model.IntegerPointSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class FireModeFactory {

    private final GenerationProperties generationProperties;

    public StandardTemperature standardTemperature() {
        return new StandardTempGenerator(
                generationProperties.getGeneral().getEnvironmentTemperature(),
                generationProperties.getGeneral().getTime()
        ).generate();
    }

    public FurnaceTemperature furnaceTemperature(StandardTemperature standardTemp) {
        return new FurnaceTempGenerator(
                generationProperties.getGeneral().getTime(),
                generationProperties.getGeneral().getEnvironmentTemperature(),
                standardTemp
        ).generate();
    }

    public MinAllowedTemperature minAllowedTemperature(StandardTemperature standardTemp) {
        return new MinAllowedTempGenerator(
                generationProperties.getGeneral().getTime(),
                standardTemp
        ).generate();
    }

    public MaxAllowedTemperature maxAllowedTemperature(StandardTemperature standardTemp) {
        return new MaxAllowedTempGenerator(
                generationProperties.getGeneral().getTime(),
                standardTemp
        ).generate();
    }

    public ThermocoupleMeanTemperature thermocoupleMeanTemperature(Integer sampleNumber,
                                                                   MinAllowedTemperature minAllowedTemperature,
                                                                   MaxAllowedTemperature maxAllowedTemperature) {

        val sample = generationProperties.getSamples().get(sampleNumber);

        val function = new MeanFunctionGenerator(
                generationProperties.getGeneral().getEnvironmentTemperature(),
                generationProperties.getGeneral().getTime(),
                maxAllowedTemperature.smoothed(),
                minAllowedTemperature.smoothed(),
                new IntegerPointSequence(sample.getFireMode().getInterpolationPoints()),
                sample.getFireMode().getRandomPoints().getEnrichWithRandomPoints(),
                sample.getFireMode().getRandomPoints().getNewPointChance()
        ).generate();

        return new ThermocoupleMeanTemperature(function.getValue());
    }

    public List<ThermocoupleTemperature> thermocouplesTemperatures(MinAllowedTemperature minAllowedTemperature,
                                                                   MaxAllowedTemperature maxAllowedTemperature,
                                                                   ThermocoupleMeanTemperature thermocoupleMeanTemperature,
                                                                   Integer sampleNumber) {

        val sample = generationProperties.getSamples().get(sampleNumber);

        val functions = new MeanChildFunctionsGenerator(
                generationProperties.getGeneral().getTime(),
                generationProperties.getGeneral().getEnvironmentTemperature(),
                thermocoupleMeanTemperature,
                maxAllowedTemperature.smoothed(),
                minAllowedTemperature.smoothed(),
                sample.getFireMode().getThermocoupleCount()
        ).generate();

        return functions.stream()
                .map(f -> new ThermocoupleTemperature(f.getValue()))
                .collect(Collectors.toList());
    }
}