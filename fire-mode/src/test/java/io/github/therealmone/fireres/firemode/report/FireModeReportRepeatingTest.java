package io.github.therealmone.fireres.firemode.report;

import com.google.inject.Inject;
import io.github.therealmone.fireres.firemode.GuiceRunner;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.github.therealmone.fireres.firemode.TestGenerationProperties.TIME;
import static io.github.therealmone.fireres.firemode.TestUtils.assertFunctionConstantlyGrowing;
import static io.github.therealmone.fireres.firemode.TestUtils.assertFunctionNotHigher;
import static io.github.therealmone.fireres.firemode.TestUtils.assertFunctionNotLower;
import static io.github.therealmone.fireres.firemode.TestUtils.assertSizesEquals;
import static io.github.therealmone.fireres.firemode.TestUtils.assertThermocouplesTemperaturesEqualsMean;
import static org.junit.Assert.assertEquals;

@RunWith(GuiceRunner.class)
public class FireModeReportRepeatingTest {

    private static final Integer ATTEMPTS = 100;

    @Inject
    private FireModeReportProvider reportProvider;

    @Test
    public void provideReportTest() {
        for (int i = 0; i < ATTEMPTS; i++) {
            val report = reportProvider.get();

            val furnaceTemp = report.getFurnaceTemperature().getValue();
            val minAllowedTemp = report.getMinAllowedTemperature().getValue();
            val maxAllowedTemp = report.getMaxAllowedTemperature().getValue();
            val maxAllowedSmoothedTemp = report.getMaxAllowedTemperature().getSmoothedValue();
            val standardTemp = report.getStandardTemperature().getValue();

            //noinspection unchecked
            assertSizesEquals(TIME, furnaceTemp, minAllowedTemp, maxAllowedTemp, maxAllowedSmoothedTemp, standardTemp);

            assertFunctionConstantlyGrowing(minAllowedTemp);
            assertFunctionConstantlyGrowing(maxAllowedSmoothedTemp);
            assertFunctionNotHigher(minAllowedTemp, maxAllowedTemp);
            assertFunctionNotHigher(minAllowedTemp, maxAllowedSmoothedTemp);

            assertFunctionNotLower(standardTemp, minAllowedTemp);
            assertFunctionNotHigher(standardTemp, maxAllowedTemp);
            assertFunctionNotHigher(standardTemp, maxAllowedSmoothedTemp);

            report.getSamples().forEach(sample -> {
                val meanTemp = sample.getThermocoupleMeanTemperature();

                assertFunctionConstantlyGrowing(meanTemp.getValue());
                assertFunctionNotLower(meanTemp.getValue(), minAllowedTemp);
                assertFunctionNotHigher(meanTemp.getValue(), maxAllowedTemp);
                assertFunctionNotHigher(meanTemp.getValue(), maxAllowedSmoothedTemp);

                val thermocouplesTemps = sample.getThermocoupleTemperatures();

                assertEquals(6, thermocouplesTemps.size());
                assertThermocouplesTemperaturesEqualsMean(thermocouplesTemps, meanTemp);

                thermocouplesTemps.forEach(thermocouplesTemp -> {

                    assertEquals(TIME, thermocouplesTemp.getValue().size());

                    assertFunctionConstantlyGrowing(thermocouplesTemp.getValue());
                    assertFunctionNotLower(thermocouplesTemp.getValue(), minAllowedTemp);
                    assertFunctionNotHigher(thermocouplesTemp.getValue(), maxAllowedTemp);
                    assertFunctionNotHigher(thermocouplesTemp.getValue(), maxAllowedSmoothedTemp);

                });
            });
        }
    }

}