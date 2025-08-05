package learn.spring.smart_parking_lot.service;

import learn.spring.smart_parking_lot.exception.NoAvailableSpotException;
import learn.spring.smart_parking_lot.model.ParkingSpot;
import learn.spring.smart_parking_lot.model.ParkingSpotType;
import learn.spring.smart_parking_lot.model.VehicleType;
import learn.spring.smart_parking_lot.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParkingSpotAllocationService {
    
    private final ParkingSpotRepository parkingSpotRepository;
    private final FloorManagementService floorManagementService;
    
    @Autowired
    public ParkingSpotAllocationService(ParkingSpotRepository parkingSpotRepository, FloorManagementService floorManagementService) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.floorManagementService = floorManagementService;
    }
    
    public ParkingSpot allocateSpot(VehicleType vehicleType) {
        // Find the best available spot for the vehicle type
        ParkingSpot spot = findBestAvailableSpot(vehicleType)
            .orElseThrow(() -> new NoAvailableSpotException("No available parking spot for " + vehicleType));
        
        // Mark the spot as occupied
        spot.occupy();
        return parkingSpotRepository.save(spot);
    }
    
    /**
     * Allocate spot with floor preference
     */
    public ParkingSpot allocateSpotWithFloorPreference(VehicleType vehicleType, Integer preferredFloor) {
        // Try to find spot on preferred floor first
        if (preferredFloor != null) {
            Optional<ParkingSpot> preferredSpot = findBestAvailableSpotOnFloor(vehicleType, preferredFloor);
            if (preferredSpot.isPresent()) {
                ParkingSpot spot = preferredSpot.get();
                spot.occupy();
                return parkingSpotRepository.save(spot);
            }
        }
        
        // Fallback to any available spot
        return allocateSpot(vehicleType);
    }
    
    private Optional<ParkingSpot> findBestAvailableSpot(VehicleType vehicleType) {
        // Strategy: Try to find the most appropriate spot size first
        // Then try larger spots if smaller ones are not available
        
        switch (vehicleType) {
            case MOTORCYCLE:
                // Try motorcycle spots first, then compact, then large
                return findAvailableSpotByType(ParkingSpotType.MOTORCYCLE)
                    .or(() -> findAvailableSpotByType(ParkingSpotType.COMPACT))
                    .or(() -> findAvailableSpotByType(ParkingSpotType.LARGE));
                    
            case CAR:
                // Try compact spots first, then large (motorcycles can't fit cars)
                return findAvailableSpotByType(ParkingSpotType.COMPACT)
                    .or(() -> findAvailableSpotByType(ParkingSpotType.LARGE));
                    
            case BUS:
                // Only large spots can fit buses
                return findAvailableSpotByType(ParkingSpotType.LARGE);
                
            default:
                return Optional.empty();
        }
    }
    
    /**
     * Find best available spot on a specific floor
     */
    private Optional<ParkingSpot> findBestAvailableSpotOnFloor(VehicleType vehicleType, Integer floor) {
        switch (vehicleType) {
            case MOTORCYCLE:
                return findAvailableSpotByTypeAndFloor(ParkingSpotType.MOTORCYCLE, floor)
                    .or(() -> findAvailableSpotByTypeAndFloor(ParkingSpotType.COMPACT, floor))
                    .or(() -> findAvailableSpotByTypeAndFloor(ParkingSpotType.LARGE, floor));
                    
            case CAR:
                return findAvailableSpotByTypeAndFloor(ParkingSpotType.COMPACT, floor)
                    .or(() -> findAvailableSpotByTypeAndFloor(ParkingSpotType.LARGE, floor));
                    
            case BUS:
                return findAvailableSpotByTypeAndFloor(ParkingSpotType.LARGE, floor);
                
            default:
                return Optional.empty();
        }
    }
    
    private Optional<ParkingSpot> findAvailableSpotByType(ParkingSpotType spotType) {
        // Get floors under maintenance to exclude them
        List<Integer> maintenanceFloors = floorManagementService.getFloorsUnderMaintenance();
        
        List<ParkingSpot> availableSpots;
        if (maintenanceFloors.isEmpty()) {
            // No maintenance floors, use regular query
            availableSpots = parkingSpotRepository
                .findAvailableSpotsByTypeOrderByFloorAndSpotNumber(spotType);
        } else {
            // Exclude maintenance floors
            availableSpots = parkingSpotRepository
                .findAvailableSpotsByTypeExcludingFloors(spotType, maintenanceFloors);
        }
        
        return availableSpots.isEmpty() ? Optional.empty() : Optional.of(availableSpots.get(0));
    }
    
    /**
     * Find available spot by type on a specific floor
     */
    private Optional<ParkingSpot> findAvailableSpotByTypeAndFloor(ParkingSpotType spotType, Integer floor) {
        // Check if the requested floor is under maintenance
        List<Integer> maintenanceFloors = floorManagementService.getFloorsUnderMaintenance();
        if (maintenanceFloors.contains(floor)) {
            return Optional.empty(); // Floor is under maintenance
        }
        
        List<ParkingSpot> availableSpots = parkingSpotRepository
            .findAvailableSpotsByTypeAndFloor(spotType, floor);
        
        return availableSpots.isEmpty() ? Optional.empty() : Optional.of(availableSpots.get(0));
    }
    
    public void freeSpot(ParkingSpot spot) {
        spot.free();
        parkingSpotRepository.save(spot);
    }
    
    public long getAvailableSpotCount() {
        return parkingSpotRepository.countAvailableSpots();
    }
    
    public long getAvailableSpotCountByType(ParkingSpotType spotType) {
        return parkingSpotRepository.countAvailableSpotsByType(spotType);
    }
}
