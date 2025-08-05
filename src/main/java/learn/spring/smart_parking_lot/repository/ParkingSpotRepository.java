package learn.spring.smart_parking_lot.repository;

import learn.spring.smart_parking_lot.model.ParkingSpot;
import learn.spring.smart_parking_lot.model.ParkingSpotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    Optional<ParkingSpot> findBySpotNumber(String spotNumber);
    
    List<ParkingSpot> findByIsAvailableTrue();
    
    List<ParkingSpot> findByIsAvailableTrueAndSpotType(ParkingSpotType spotType);
    
    List<ParkingSpot> findByFloor(Integer floor);
    
    @Query("SELECT COUNT(p) FROM ParkingSpot p WHERE p.isAvailable = true")
    long countAvailableSpots();
    
    @Query("SELECT COUNT(p) FROM ParkingSpot p WHERE p.isAvailable = true AND p.spotType = :spotType")
    long countAvailableSpotsByType(@Param("spotType") ParkingSpotType spotType);
    
    @Query("SELECT p FROM ParkingSpot p WHERE p.isAvailable = true AND p.spotType = :spotType ORDER BY p.floor ASC, p.spotNumber ASC")
    List<ParkingSpot> findAvailableSpotsByTypeOrderByFloorAndSpotNumber(@Param("spotType") ParkingSpotType spotType);
    
    @Query("SELECT p FROM ParkingSpot p WHERE p.isAvailable = true AND p.spotType = :spotType AND p.floor = :floor ORDER BY p.spotNumber ASC")
    List<ParkingSpot> findAvailableSpotsByTypeAndFloor(@Param("spotType") ParkingSpotType spotType, @Param("floor") Integer floor);
    
    @Query("SELECT p FROM ParkingSpot p WHERE p.isAvailable = true AND p.spotType = :spotType AND p.floor NOT IN :excludedFloors ORDER BY p.floor ASC, p.spotNumber ASC")
    List<ParkingSpot> findAvailableSpotsByTypeExcludingFloors(@Param("spotType") ParkingSpotType spotType, @Param("excludedFloors") List<Integer> excludedFloors);
}
