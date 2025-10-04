package fioshi.com.github.safedriver.SafeDriver.repository;

import fioshi.com.github.safedriver.SafeDriver.model.Trip;
import fioshi.com.github.safedriver.SafeDriver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {
    List<Trip> findByUser(User user);
}
