package fioshi.com.github.safedriver.SafeDriver.repository;

import fioshi.com.github.safedriver.SafeDriver.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query(value = """
        SELECT EXTRACT(HOUR FROM p.timestamp) AS hour,
               AVG(p.points) AS avg_points
        FROM point_history p
        WHERE p.user_id = :userId
          AND p.timestamp >= :startDate
        GROUP BY hour
        ORDER BY hour ASC
        """, nativeQuery = true)
    List<Object[]> findDailyAverage(@Param("userId") UUID userId,
                                    @Param("startDate") OffsetDateTime startDate);

    @Query(value = """
        SELECT EXTRACT(ISODOW FROM p.timestamp) AS day,
               AVG(p.points) AS avg_points
        FROM point_history p
        WHERE p.user_id = :userId
          AND p.timestamp >= :startDate
        GROUP BY day
        ORDER BY day ASC
        """, nativeQuery = true)
    List<Object[]> findWeeklyAverage(@Param("userId") UUID userId,
                                     @Param("startDate") OffsetDateTime startDate);

    @Query(value = """
        SELECT EXTRACT(WEEK FROM p.timestamp) AS week,
               AVG(p.points) AS avg_points
        FROM point_history p
        WHERE p.user_id = :userId
          AND p.timestamp >= :startDate
        GROUP BY week
        ORDER BY week ASC
        """, nativeQuery = true)
    List<Object[]> findMonthlyAverage(@Param("userId") UUID userId,
                                      @Param("startDate") OffsetDateTime startDate);

    @Query(value = """
        SELECT EXTRACT(MONTH FROM p.timestamp) AS month,
               AVG(p.points) AS avg_points
        FROM point_history p
        WHERE p.user_id = :userId
          AND p.timestamp >= :startDate
        GROUP BY month
        ORDER BY month ASC
        """, nativeQuery = true)
    List<Object[]> findYearlyAverage(@Param("userId") UUID userId,
                                     @Param("startDate") OffsetDateTime startDate);

    @Query(value = """
        SELECT AVG(p.points)
        FROM point_history p
        WHERE p.user_id = :userId
          AND p.timestamp >= :startDate
        """, nativeQuery = true)
    Double findOverallAverage(@Param("userId") UUID userId,
                              @Param("startDate") OffsetDateTime startDate);
}
