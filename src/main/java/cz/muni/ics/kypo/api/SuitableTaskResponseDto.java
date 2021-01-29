package cz.muni.ics.kypo.api;

public class SuitableTaskResponseDto {

    private int suitableTask;

    public int getSuitableTask() {
        return suitableTask;
    }

    public void setSuitableTask(int suitableTask) {
        this.suitableTask = suitableTask;
    }

    @Override
    public String toString() {
        return "SuitableTaskResponseDto{" +
                "suitableTask=" + suitableTask +
                '}';
    }
}
