package learn.spring.smart_parking_lot.repository;

import learn.spring.smart_parking_lot.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
    Optional<Floor> findByFloorNumber(Integer floorNumber);
    
    List<Floor> findByUnderMaintenanceFalse();
    
    List<Floor> findByUnderMaintenanceTrue();
    
    @Query("SELECT f.floorNumber FROM Floor f WHERE f.underMaintenance = false ORDER BY f.floorNumber ASC")
    List<Integer> findAvailableFloorNumbers();
    
    boolean existsByFloorNumber(Integer floorNumber);
}
