package io.github.therealmone.fireres.gui.controller.unheated.surface.groups.first;

import com.google.inject.Inject;
import io.github.therealmone.fireres.core.model.IntegerPoint;
import io.github.therealmone.fireres.core.model.Sample;
import io.github.therealmone.fireres.gui.annotation.LoadableComponent;
import io.github.therealmone.fireres.gui.controller.AbstractComponent;
import io.github.therealmone.fireres.gui.controller.ChartContainer;
import io.github.therealmone.fireres.gui.controller.common.FunctionParams;
import io.github.therealmone.fireres.gui.controller.unheated.surface.UnheatedSurface;
import io.github.therealmone.fireres.gui.controller.unheated.surface.UnheatedSurfaceReportContainer;
import io.github.therealmone.fireres.unheated.surface.config.UnheatedSurfaceProperties;
import io.github.therealmone.fireres.unheated.surface.report.UnheatedSurfaceReport;
import io.github.therealmone.fireres.unheated.surface.service.UnheatedSurfaceFirstGroupService;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import static java.util.Collections.singletonList;

@LoadableComponent("/component/unheated-surface/groups/first/firstGroup.fxml")
public class FirstGroup extends AbstractComponent<TitledPane> implements UnheatedSurfaceReportContainer {

    @FXML
    @Getter
    private VBox paramsVbox;

    @FXML
    private FirstGroupParams firstGroupParamsController;

    @FXML
    private FunctionParams functionParamsController;

    @FXML
    private FirstGroupChart firstGroupChartController;

    @Inject
    private UnheatedSurfaceFirstGroupService unheatedSurfaceFirstGroupService;

    @Override
    protected void initialize() {
        getFunctionParams().setInterpolationService(unheatedSurfaceFirstGroupService);

        getFunctionParams().setPropertiesMapper(props ->
                props.getReportPropertiesByClass(UnheatedSurfaceProperties.class).orElseThrow().getFirstGroup());

        getFunctionParams().setNodesToBlockOnUpdate(singletonList(paramsVbox));

        getFunctionParams().setInterpolationPointConstructor((time, value) -> new IntegerPoint(time, value.intValue()));
    }

    @Override
    public Sample getSample() {
        return ((UnheatedSurface) getParent()).getSample();
    }

    @Override
    public UnheatedSurfaceReport getReport() {
        return ((UnheatedSurface) getParent()).getReport();
    }

    @Override
    public ChartContainer getChartContainer() {
        return firstGroupChartController;
    }

    public FirstGroupParams getFirstGroupParams() {
        return firstGroupParamsController;
    }

    public FunctionParams getFunctionParams() {
        return functionParamsController;
    }

}