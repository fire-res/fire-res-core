package io.github.therealmone.fireres.firemode.pipeline;

import com.google.inject.Inject;
import io.github.therealmone.fireres.core.config.GenerationProperties;
import io.github.therealmone.fireres.core.pipeline.EnrichType;
import io.github.therealmone.fireres.core.pipeline.ReportEnricher;
import io.github.therealmone.fireres.firemode.generator.StandardTempGenerator;
import io.github.therealmone.fireres.firemode.report.FireModeReport;
import lombok.val;

import java.util.List;

import static io.github.therealmone.fireres.firemode.pipeline.FireModeEnrichType.FURNACE_TEMPERATURE;
import static io.github.therealmone.fireres.firemode.pipeline.FireModeEnrichType.MAX_ALLOWED_TEMPERATURE;
import static io.github.therealmone.fireres.firemode.pipeline.FireModeEnrichType.MIN_ALLOWED_TEMPERATURE;
import static io.github.therealmone.fireres.firemode.pipeline.FireModeEnrichType.SAMPLES_TEMPERATURE;
import static io.github.therealmone.fireres.firemode.pipeline.FireModeEnrichType.STANDARD_TEMPERATURE;

public class StandardTemperatureEnricher implements ReportEnricher<FireModeReport> {

    @Inject
    private GenerationProperties generationProperties;

    @Override
    public void enrich(FireModeReport report) {
        val time = generationProperties.getGeneral().getTime();
        val t0 = generationProperties.getGeneral().getEnvironmentTemperature();

        val standardTemperature = new StandardTempGenerator(t0, time)
                .generate();

        report.setStandardTemperature(standardTemperature);
    }

    @Override
    public boolean supports(EnrichType enrichType) {
        return STANDARD_TEMPERATURE.equals(enrichType);
    }

    @Override
    public List<EnrichType> getAffectedTypes() {
        return List.of(
                MIN_ALLOWED_TEMPERATURE,
                MAX_ALLOWED_TEMPERATURE,
                FURNACE_TEMPERATURE,
                SAMPLES_TEMPERATURE);
    }
}
