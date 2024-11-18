package cz.cyberrange.platform.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "SuitableTaskResponseDto")
@Data
public class SuitableTaskResponseDto {

    @ApiModelProperty(value = "Returns the number representing the suitable task for a given participant", example = "1")
    private int suitableTask;
}
