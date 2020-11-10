package io.github.therealmone.fireres.core.config.unheated;

import com.typesafe.config.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UnheatedSurfaceThirdGroup extends UnheatedSurfaceGroup {

    @Optional
    @Builder.Default
    private Integer bound = 300;

}