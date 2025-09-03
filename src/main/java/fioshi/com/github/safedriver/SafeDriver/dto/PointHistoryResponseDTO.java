package fioshi.com.github.safedriver.SafeDriver.dto;

import java.util.List;

public class PointHistoryResponseDTO {

    private String period;
    private Double overallAverage;
    private List<PointAverageDTO> averages;

    public PointHistoryResponseDTO(String period, Double overallAverage, List<PointAverageDTO> averages) {
        this.period = period;
        this.overallAverage = overallAverage;
        this.averages = averages;
    }

    // Getters and Setters

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getOverallAverage() {
        return overallAverage;
    }

    public void setOverallAverage(Double overallAverage) {
        this.overallAverage = overallAverage;
    }

    public List<PointAverageDTO> getAverages() {
        return averages;
    }

    public void setAverages(List<PointAverageDTO> averages) {
        this.averages = averages;
    }
}
