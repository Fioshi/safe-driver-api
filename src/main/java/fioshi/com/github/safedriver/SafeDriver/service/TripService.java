package fioshi.com.github.safedriver.SafeDriver.service;

import fioshi.com.github.safedriver.SafeDriver.dto.TelemetryPoint;
import fioshi.com.github.safedriver.SafeDriver.dto.TripTelemetryRequestDTO;
import fioshi.com.github.safedriver.SafeDriver.dto.TripSummaryResponseDTO;
import fioshi.com.github.safedriver.SafeDriver.mapper.SafeDriverMapper;
import fioshi.com.github.safedriver.SafeDriver.model.Driver;
import fioshi.com.github.safedriver.SafeDriver.model.Trip;
import fioshi.com.github.safedriver.SafeDriver.model.Vehicle;
import fioshi.com.github.safedriver.SafeDriver.repository.DriverRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.TripRepository;
import fioshi.com.github.safedriver.SafeDriver.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PointHistoryService pointHistoryService;

    @Autowired
    private SafeDriverMapper mapper;

    @Transactional
    public TripSummaryResponseDTO analyzeTrip(TripTelemetryRequestDTO request, Integer driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        List<TelemetryPoint> telemetryPoints = request.getTelemetryPoints();
        if (telemetryPoints == null || telemetryPoints.size() < 2) {
            throw new IllegalArgumentException("Telemetry points cannot be empty or have less than two points");
        }

        telemetryPoints.sort(Comparator.comparing(TelemetryPoint::getTimestamp));

        LocalDateTime startTime = telemetryPoints.get(0).getTimestamp();
        LocalDateTime endTime = telemetryPoints.get(telemetryPoints.size() - 1).getTimestamp();

        // Calculate total distance from telemetry points
        double totalDistanceKm = 0.0;
        for (int i = 0; i < telemetryPoints.size() - 1; i++) {
            TelemetryPoint p1 = telemetryPoints.get(i);
            TelemetryPoint p2 = telemetryPoints.get(i + 1);
            totalDistanceKm += haversineDistance(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());
        }

        // Get pre-processed event counts directly from the request
        int hardBrakingCount = request.getHardBrakingCount();
        int suddenAccelerationCount = request.getSuddenAccelerationCount();
        int speedingEventsCount = request.getSpeedingEventsCount();

        long durationSeconds = ChronoUnit.SECONDS.between(startTime, endTime);
        double averageSpeedKmH = (durationSeconds > 0) ? (totalDistanceKm / durationSeconds) * 3600 : 0;

        int performanceScore = calculatePerformanceScore(totalDistanceKm, hardBrakingCount, suddenAccelerationCount, speedingEventsCount);
        int pointsEarned = calculatePointsEarned(performanceScore);

        Trip trip = new Trip();
        trip.setDriver(driver);
        trip.setVehicle(vehicle);
        trip.setStartTime(startTime);
        trip.setEndTime(endTime);
        trip.setTotalDistanceKm(totalDistanceKm);
        trip.setDurationSeconds(durationSeconds);
        trip.setAverageSpeedKmH(averageSpeedKmH);
        trip.setHardBrakingCount(hardBrakingCount);
        trip.setSuddenAccelerationCount(suddenAccelerationCount);
        trip.setSpeedingEventsCount(speedingEventsCount);
        trip.setPerformanceScore(performanceScore);
        trip.setPointsEarned(pointsEarned);
        tripRepository.save(trip);

        driver.addPoints(pointsEarned);
        driverRepository.save(driver);
        pointHistoryService.addPoints(driver, pointsEarned, "Pontos ganhos em viagem");

        TripSummaryResponseDTO response = mapper.toTripSummaryResponseDTO(trip);
        response.setMessage("Viagem analisada e pontos atribuídos com sucesso!");

        return response;
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of earth in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private int calculatePerformanceScore(double totalDistanceKm, int hardBrakingCount, int suddenAccelerationCount, int speedingEventsCount) {
        int score = 100;

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
