package fioshi.com.github.safedriver.SafeDriver.repository;

import fioshi.com.github.safedriver.SafeDriver.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query("SELECT FUNCTION('HOUR', p.timestamp) as hour, AVG(p.points) as avg_points " +
           "FROM PointHistory p " +
           "WHERE p.user.userId = :userId AND p.timestamp >= :startDate " +
           "GROUP BY hour " +
           "ORDER BY hour ASC")
    List<Object[]> findDailyAverage(@Param("userId") UUID userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT FUNCTION('DAYOFWEEK', p.timestamp) as day, AVG(p.points) as avg_points " +
           "FROM PointHistory p " +
           "WHERE p.user.userId = :userId AND p.timestamp >= :startDate " +
           "GROUP BY day " +
           "ORDER BY day ASC")
    List<Object[]> findWeeklyAverage(@Param("userId") UUID userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT FUNCTION('WEEK', p.timestamp) as week, AVG(p.points) as avg_points " +
           "FROM PointHistory p " +
           "WHERE p.user.userId = :userId AND p.timestamp >= :startDate " +
           "GROUP BY week " +
           "ORDER BY week ASC")
    List<Object[]> findMonthlyAverage(@Param("userId") UUID userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT FUNCTION('MONTH', p.timestamp) as month, AVG(p.points) as avg_points " +
           "FROM PointHistory p " +
           "WHERE p.user.userId = :userId AND p.timestamp >= :startDate " +
           "GROUP BY month " +
           "ORDER BY month ASC")
    List<Object[]> findYearlyAverage(@Param("userId") UUID userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT AVG(p.points) FROM PointHistory p WHERE p.user.userId = :userId AND p.timestamp >= :startDate")
    Double findOverallAverage(@Param("userId") UUID userId, @Param("startDate") LocalDateTime startDate);
}
