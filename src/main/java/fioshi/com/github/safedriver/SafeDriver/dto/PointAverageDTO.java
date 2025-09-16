package fioshi.com.github.safedriver.SafeDriver.dto;

public class PointAverageDTO {

    private String label;
    private Double average;

    public PointAverageDTO(String label, Double average) {
        this.label = label;
        this.average = average;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
