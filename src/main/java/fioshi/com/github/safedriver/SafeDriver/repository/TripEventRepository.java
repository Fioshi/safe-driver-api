package fioshi.com.github.safedriver.SafeDriver.repository;

import fioshi.com.github.safedriver.SafeDriver.model.TripEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TripEventRepository extends JpaRepository<TripEvent, UUID> {
}
