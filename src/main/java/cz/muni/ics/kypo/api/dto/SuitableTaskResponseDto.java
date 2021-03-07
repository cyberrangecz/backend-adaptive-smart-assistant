package cz.muni.ics.kypo.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "SuitableTaskResponseDto")
public class SuitableTaskResponseDto {

    @ApiModelProperty(value = "Returns the number representing the suitable task for a given participant", example = "1")
    private int suitableTask;

    public int getSuitableTask() {
        return suitableTask;
    }

    public void setSuitableTask(int suitableTask) {
        this.suitableTask = suitableTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuitableTaskResponseDto)) return false;
        SuitableTaskResponseDto that = (SuitableTaskResponseDto) o;
        return suitableTask == that.suitableTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suitableTask);
    }

    @Override
    public String toString() {
        return "SuitableTaskResponseDto{" +
                "suitableTask=" + suitableTask +
                '}';
    }
}
