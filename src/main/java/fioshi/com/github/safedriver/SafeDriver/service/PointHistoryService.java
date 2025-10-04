package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.PointAverageDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.PointHistoryResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.model.PointHistory;
import fioshi.com.github.safedriver.SafeDriver.model.enums.HistoryPeriod;
import fioshi.com.github.safedriver.SafeDriver.repository.PointHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Transactional
    public void addPoints(User user, int points, String reason) {
        PointHistory pointHistory = new PointHistory(user, points, reason);
        pointHistoryRepository.save(pointHistory);
    }

    public PointHistoryResponseDTO getPointHistory(UUID userId, HistoryPeriod period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        List<Object[]> results;

        switch (period) {
            case DAILY:
                startDate = now.minusDays(1);
                results = pointHistoryRepository.findDailyAverage(userId, startDate);
                break;
            case WEEKLY:
                startDate = now.minusWeeks(1);
                results = pointHistoryRepository.findWeeklyAverage(userId, startDate);
                break;
            case MONTHLY:
                startDate = now.minusMonths(1);
                results = pointHistoryRepository.findMonthlyAverage(userId, startDate);
                break;
            case YEARLY:
                startDate = now.minusYears(1);
                results = pointHistoryRepository.findYearlyAverage(userId, startDate);
                break;
            default:
                throw new IllegalArgumentException("Invalid period specified");
        }

        List<PointAverageDTO> averages = results.stream()
                .map(result -> convertToObjectDTO(result, period))
                .collect(Collectors.toList());

        Double overallAverage = pointHistoryRepository.findOverallAverage(userId, startDate);

        return new PointHistoryResponseDTO(period.name(), overallAverage, averages);
    }

    private PointAverageDTO convertToObjectDTO(Object[] result, HistoryPeriod period) {
        Number value = (Number) result[0];
        Double average = (Double) result[1];
        String label = "";

        switch (period) {
            case DAILY:
                label = String.format("%02d:00", value.intValue());
                break;
            case WEEKLY:
                label = DayOfWeek.of(value.intValue() == 1 ? 7 : value.intValue() - 1).getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
                break;
            case MONTHLY:
                label = "Semana " + value.intValue();
                break;
            case YEARLY:
                label = Month.of(value.intValue()).getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
                break;
        }
        return new PointAverageDTO(label, average);
    }
}
