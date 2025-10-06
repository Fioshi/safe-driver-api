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
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
        // Obter a data/hora atual JÁ COM o offset do sistema
        OffsetDateTime nowWithOffset = LocalDateTime.now().atOffset(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));

        OffsetDateTime startDateWithOffset;
        List<Object[]> results;

        switch (period) {
            case DAILY:
                startDateWithOffset = nowWithOffset.minusDays(1);
                results = pointHistoryRepository.findDailyAverage(userId, startDateWithOffset);
                break;
            case WEEKLY:
                startDateWithOffset = nowWithOffset.minusWeeks(1);
                results = pointHistoryRepository.findWeeklyAverage(userId, startDateWithOffset);
                break;
            case MONTHLY:
                startDateWithOffset = nowWithOffset.minusMonths(1);
                results = pointHistoryRepository.findMonthlyAverage(userId, startDateWithOffset);
                break;
            case YEARLY:
                startDateWithOffset = nowWithOffset.minusYears(1);
                results = pointHistoryRepository.findYearlyAverage(userId, startDateWithOffset);
                break;
            default:
                throw new IllegalArgumentException("Invalid period specified");
        }

        List<PointAverageDTO> averages = results.stream()
                .map(result -> convertToObjectDTO(result, period))
                .collect(Collectors.toList());

        Double overallAverage = pointHistoryRepository.findOverallAverage(userId, startDateWithOffset);

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
                // <<< LÓGICA ATUALIZADA AQUI <<<
                // Converte o dia da semana do padrão do banco de dados (ex: Domingo=1)
                // para o padrão do Java DayOfWeek (Segunda=1, Domingo=7).
                label = DayOfWeek.of(value.intValue() == 1 ? 7 : value.intValue() - 1)
                        .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
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