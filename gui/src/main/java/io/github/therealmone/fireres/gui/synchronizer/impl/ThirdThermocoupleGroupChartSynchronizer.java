package io.github.therealmone.fireres.gui.synchronizer.impl;

import io.github.therealmone.fireres.gui.synchronizer.ChartSynchronizer;
import io.github.therealmone.fireres.unheated.surface.report.UnheatedSurfaceReport;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import lombok.val;

import static io.github.therealmone.fireres.gui.model.ElementIds.DEFAULT_MEAN_LINE;
import static io.github.therealmone.fireres.gui.model.ElementIds.DEFAULT_MEAN_LINE_LEGEND_SYMBOL;
import static io.github.therealmone.fireres.gui.model.ElementIds.THIRD_THERMOCOUPLE_GROUP_THERMOCOUPLE_MAX_TEMPERATURE_LINE;
import static io.github.therealmone.fireres.gui.model.ElementIds.THIRD_THERMOCOUPLE_GROUP_THERMOCOUPLE_TEMPERATURE_LINE;
import static io.github.therealmone.fireres.gui.util.ChartUtils.addLegendSymbolId;
import static io.github.therealmone.fireres.gui.util.ChartUtils.addPointsToSeries;
import static io.github.therealmone.fireres.gui.util.ChartUtils.randomizeColor;

public class ThirdThermocoupleGroupChartSynchronizer implements ChartSynchronizer<UnheatedSurfaceReport> {

    public static final String THERMOCOUPLE_TEXT = "Термопара - ";
    public static final String MAX_THERMOCOUPLE_TEMPERATURE_TEXT = "Предельное значение температуры термопар";
    public static final String MEAN_TEMPERATURE_TEXT = "Среднее значение температуры";

    public void synchronize(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        chart.getData().clear();

        addMeanTemperatureLine(chart, report);
        addThermocoupleBoundLine(chart, report);
        addThermocoupleTemperatureLines(chart, report);

        addLegendSymbolId(chart, MEAN_TEMPERATURE_TEXT, DEFAULT_MEAN_LINE_LEGEND_SYMBOL);
    }

    private void addThermocoupleTemperatureLines(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        for (int i = 0; i < report.getThirdGroup().getThermocoupleTemperatures().size(); i++) {

            val thermocoupleSeries = new XYChart.Series<Number, Number>();
            val thermocoupleTemperature = report.getThirdGroup().getThermocoupleTemperatures().get(i);

            thermocoupleSeries.setName(THERMOCOUPLE_TEXT + (i + 1));
            addPointsToSeries(thermocoupleSeries, thermocoupleTemperature);
            chart.getData().add(thermocoupleSeries);
            thermocoupleSeries.getNode().setId(THIRD_THERMOCOUPLE_GROUP_THERMOCOUPLE_TEMPERATURE_LINE);
            randomizeColor(thermocoupleSeries.getNode(), 0.4);
        }
    }

    private void addThermocoupleBoundLine(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MAX_THERMOCOUPLE_TEMPERATURE_TEXT);
        addPointsToSeries(series, report.getThirdGroup().getThermocoupleBound());
        chart.getData().add(series);
        series.getNode().setId(THIRD_THERMOCOUPLE_GROUP_THERMOCOUPLE_MAX_TEMPERATURE_LINE);
    }

    private void addMeanTemperatureLine(LineChart<Number, Number> chart, UnheatedSurfaceReport report) {
        val series = new XYChart.Series<Number, Number>();

        series.setName(MEAN_TEMPERATURE_TEXT);
        addPointsToSeries(series, report.getThirdGroup().getMeanTemperature());
        chart.getData().add(series);
        series.getNode().setId(DEFAULT_MEAN_LINE);
    }
}