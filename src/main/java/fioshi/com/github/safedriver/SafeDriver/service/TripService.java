package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.TelemetryPoint;
import fioshi.com.github.safedriver.SafeDriver.dto.TripTelemetryRequestDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.TripSummaryResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import fioshi.com.github.safedriver.SafeDriver.model.Trip;
import fioshi.com.github.safedriver.SafeDriver.model.TripEvent;
import fioshi.com.github.safedriver.SafeDriver.repository.UserRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.TripRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.TripEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final PointHistoryService pointHistoryService;
    private final SafeDriverMapper mapper;
    private final TripEventRepository tripEventRepository;

    public TripService(UserRepository userRepository,
                       TripRepository tripRepository,
                       PointHistoryService pointHistoryService,
                       SafeDriverMapper mapper,
                       TripEventRepository tripEventRepository) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.pointHistoryService = pointHistoryService;
        this.mapper = mapper;
        this.tripEventRepository = tripEventRepository;
    }

    @Transactional
    public TripSummaryResponseDTO analyzeTrip(TripTelemetryRequestDTO request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<TelemetryPoint> telemetryPoints = request.getTelemetryPoints();
        if (telemetryPoints == null || telemetryPoints.size() < 2) {
            throw new IllegalArgumentException("Telemetry points cannot be empty or have less than two points");
        }

        telemetryPoints.sort(Comparator.comparing(TelemetryPoint::getTimestamp));

        OffsetDateTime startTime = telemetryPoints.get(0).getTimestamp().atOffset(OffsetDateTime.now().getOffset());
        OffsetDateTime endTime = telemetryPoints.get(telemetryPoints.size() - 1).getTimestamp().atOffset(OffsetDateTime.now().getOffset());
        TelemetryPoint lastTelemetryPoint = telemetryPoints.get(telemetryPoints.size() - 1);

        BigDecimal totalDistanceKm = BigDecimal.ZERO;
        for (int i = 0; i < telemetryPoints.size() - 1; i++) {
            TelemetryPoint p1 = telemetryPoints.get(i);
            TelemetryPoint p2 = telemetryPoints.get(i + 1);
            totalDistanceKm = totalDistanceKm.add(BigDecimal.valueOf(haversineDistance(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude())));
        }

        int performanceScore = calculatePerformanceScore(totalDistanceKm, request.getTripEvents());
        int pointsEarned = calculatePointsEarned(performanceScore);

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setStartTime(startTime);
        trip.setEndTime(endTime);
        trip.setDistanceKm(totalDistanceKm);
        trip.setEconomySavedBrl(calculateEconomySavedBrl(totalDistanceKm));
        trip.setPointsEarned(pointsEarned);
        trip.setEndLatitude(lastTelemetryPoint.getLatitude() != null ? BigDecimal.valueOf(lastTelemetryPoint.getLatitude()) : null);
        trip.setEndLongitude(lastTelemetryPoint.getLongitude() != null ? BigDecimal.valueOf(lastTelemetryPoint.getLongitude()) : null);
        tripRepository.save(trip);

        if (request.getTripEvents() != null && !request.getTripEvents().isEmpty()) {
            List<TripEvent> tripEvents = request.getTripEvents().stream()
                    .map(mapper::toTripEvent)
                    .peek(event -> event.setTrip(trip))
                    .collect(Collectors.toList());
            trip.setTripEvents(tripEvents);
            tripEventRepository.saveAll(tripEvents);
        }

        tripRepository.save(trip); // Salva novamente para incluir o feedback

        user.addPoints(pointsEarned);
        userRepository.save(user);
        pointHistoryService.addPoints(user, pointsEarned, "Pontos ganhos em viagem");

        TripSummaryResponseDTO response = mapper.toTripSummaryResponseDTO(trip);
        response.setMessage("Viagem analisada e pontos atribuídos com sucesso!");

        return response;
    }

    @Transactional(readOnly = true)
    public List<TripSummaryResponseDTO> getTripsByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Trip> trips = tripRepository.findByUser(user);
        return mapper.toTripSummaryResponseDTOList(trips);
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private BigDecimal calculateEconomySavedBrl(BigDecimal totalDistanceKm) {
        return totalDistanceKm.multiply(BigDecimal.valueOf(0.1));
    }

    private int calculatePerformanceScore(BigDecimal totalDistanceKm, List<fioshi.com.github.safedriver.SafeDriver.dto.TripEventDTO> tripEvents) {
        int score = 100;
        int hardBrakingCount = 0;
        int suddenAccelerationCount = 0;
        int speedingEventsCount = 0;

        if (tripEvents != null) {
            for (fioshi.com.github.safedriver.SafeDriver.dto.TripEventDTO event : tripEvents) {
                switch (event.getEventType()) {
                    case "frenagem_brusca":
                        hardBrakingCount++;
                        break;
                    case "aceleracao_brusca":
                        suddenAccelerationCount++;
                        break;
                    case "excesso_velocidade":
                        speedingEventsCount++;
                        break;
                }
            }
        }

        score -= (hardBrakingCount * 15);
        score -= (suddenAccelerationCount * 10);
        score -= (speedingEventsCount * 5);

        if (score < 0) score = 0;
        if (score > 100) score = 100;
        return score;
    }

    private int calculatePointsEarned(int performanceScore) {
        return (int) (performanceScore * 0.75);
    }
}
