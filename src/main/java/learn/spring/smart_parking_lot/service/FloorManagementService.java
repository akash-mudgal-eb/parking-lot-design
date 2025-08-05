package learn.spring.smart_parking_lot.service;

import learn.spring.smart_parking_lot.dto.FloorStatus;
import learn.spring.smart_parking_lot.model.Floor;
import learn.spring.smart_parking_lot.model.ParkingSpot;
import learn.spring.smart_parking_lot.model.ParkingSpotType;
import learn.spring.smart_parking_lot.repository.FloorRepository;
import learn.spring.smart_parking_lot.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FloorManagementService {
    
    private final ParkingSpotRepository parkingSpotRepository;
    private final FloorRepository floorRepository;
    
    @Autowired
    public FloorManagementService(ParkingSpotRepository parkingSpotRepository, FloorRepository floorRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.floorRepository = floorRepository;
    }
    
    /**
     * Add a new floor with specified spot configuration
     */
    public void addFloor(int floorNumber, int motorcycleSpots, int compactSpots, int largeSpots) {
        // Check if floor already exists
        List<ParkingSpot> existingSpots = parkingSpotRepository.findByFloor(floorNumber);
        if (!existingSpots.isEmpty()) {
            throw new IllegalArgumentException("Floor " + floorNumber + " already exists with " + existingSpots.size() + " spots");
        }
        
        // Create floor entity
        Floor floor = new Floor(floorNumber);
        floorRepository.save(floor);
        
        createFloorSpots(floorNumber, motorcycleSpots, compactSpots, largeSpots);
    }
    
    /**
     * Add individual parking spots to an existing floor
     */
    public ParkingSpot addParkingSpot(int floorNumber, ParkingSpotType spotType) {
        // Find the next available spot number for this floor
        List<ParkingSpot> floorSpots = parkingSpotRepository.findByFloor(floorNumber);
        int nextSpotNumber = floorSpots.size() + 1;
        
        String typePrefix = getTypePrefix(spotType);
        String spotNumber = String.format("%d-%s-%02d", floorNumber, typePrefix, nextSpotNumber);
        
        // Ensure unique spot number
        while (parkingSpotRepository.findBySpotNumber(spotNumber).isPresent()) {
            nextSpotNumber++;
            spotNumber = String.format("%d-%s-%02d", floorNumber, typePrefix, nextSpotNumber);
        }
        
        ParkingSpot newSpot = new ParkingSpot(spotNumber, floorNumber, spotType);
        return parkingSpotRepository.save(newSpot);
    }
    
    /**
     * Get status for all floors
     */
    public List<FloorStatus> getAllFloorsStatus() {
        // Group spots by floor
        Map<Integer, List<ParkingSpot>> spotsByFloor = parkingSpotRepository.findAll()
            .stream()
            .collect(Collectors.groupingBy(ParkingSpot::getFloor));
        
        return spotsByFloor.entrySet().stream()
            .map(entry -> createFloorStatus(entry.getKey(), entry.getValue()))
            .sorted((f1, f2) -> Integer.compare(f1.getFloorNumber(), f2.getFloorNumber()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get status for a specific floor
     */
    public FloorStatus getFloorStatus(int floorNumber) {
        List<ParkingSpot> floorSpots = parkingSpotRepository.findByFloor(floorNumber);
        if (floorSpots.isEmpty()) {
            throw new IllegalArgumentException("Floor " + floorNumber + " does not exist");
        }
        return createFloorStatus(floorNumber, floorSpots);
    }
    
    /**
     * Remove a specific parking spot
     */
    public void removeParkingSpot(String spotNumber) {
        ParkingSpot spot = parkingSpotRepository.findBySpotNumber(spotNumber)
            .orElseThrow(() -> new IllegalArgumentException("Parking spot " + spotNumber + " not found"));
        
        if (!spot.getIsAvailable()) {
            throw new IllegalStateException("Cannot remove occupied parking spot " + spotNumber);
        }
        
        parkingSpotRepository.delete(spot);
    }
    
    /**
     * Remove an entire floor (only if all spots are available)
     */
    public void removeFloor(int floorNumber) {
        List<ParkingSpot> floorSpots = parkingSpotRepository.findByFloor(floorNumber);
        if (floorSpots.isEmpty()) {
            throw new IllegalArgumentException("Floor " + floorNumber + " does not exist");
        }
        
        // Check if any spots are occupied
        boolean hasOccupiedSpots = floorSpots.stream().anyMatch(spot -> !spot.getIsAvailable());
        if (hasOccupiedSpots) {
            throw new IllegalStateException("Cannot remove floor " + floorNumber + " - some spots are occupied");
        }
        
        // Remove spots and floor
        parkingSpotRepository.deleteAll(floorSpots);
        floorRepository.findByFloorNumber(floorNumber).ifPresent(floorRepository::delete);
    }
    
    /**
     * Set maintenance mode for a floor
     */
    public void setFloorMaintenanceMode(int floorNumber, boolean underMaintenance, String reason) {
        Floor floor = floorRepository.findByFloorNumber(floorNumber)
            .orElse(new Floor(floorNumber)); // Create if doesn't exist
        
        // Check if there are occupied spots when setting maintenance mode
        if (underMaintenance) {
            List<ParkingSpot> floorSpots = parkingSpotRepository.findByFloor(floorNumber);
            boolean hasOccupiedSpots = floorSpots.stream().anyMatch(spot -> !spot.getIsAvailable());
            if (hasOccupiedSpots) {
                throw new IllegalStateException("Cannot set floor " + floorNumber + 
                    " to maintenance mode - some spots are currently occupied");
            }
        }
        
        floor.setMaintenanceMode(underMaintenance, reason);
        floorRepository.save(floor);
    }
    
    /**
     * Get list of floors under maintenance
     */
    public List<Integer> getFloorsUnderMaintenance() {
        return floorRepository.findByUnderMaintenanceTrue()
            .stream()
            .map(Floor::getFloorNumber)
            .collect(Collectors.toList());
    }
    
    /**
     * Get list of available (non-maintenance) floors
     */
    public List<Integer> getAvailableFloors() {
        return floorRepository.findAvailableFloorNumbers();
    }
    
    private void createFloorSpots(int floor, int motorcycleSpots, int compactSpots, int largeSpots) {
        int spotCounter = 1;
        
        // Create motorcycle spots
        for (int i = 0; i < motorcycleSpots; i++) {
            String spotNumber = String.format("%d-M-%02d", floor, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotNumber, floor, ParkingSpotType.MOTORCYCLE);
            parkingSpotRepository.save(spot);
        }
        
        // Create compact spots
        for (int i = 0; i < compactSpots; i++) {
            String spotNumber = String.format("%d-C-%02d", floor, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotNumber, floor, ParkingSpotType.COMPACT);
            parkingSpotRepository.save(spot);
        }
        
        // Create large spots
        for (int i = 0; i < largeSpots; i++) {
            String spotNumber = String.format("%d-L-%02d", floor, spotCounter++);
            ParkingSpot spot = new ParkingSpot(spotNumber, floor, ParkingSpotType.LARGE);
            parkingSpotRepository.save(spot);
        }
        
        System.out.println("Floor " + floor + ": " + motorcycleSpots + " motorcycle, " + 
                          compactSpots + " compact, " + largeSpots + " large spots created");
    }
    
    private FloorStatus createFloorStatus(int floorNumber, List<ParkingSpot> spots) {
        FloorStatus status = new FloorStatus();
        status.setFloorNumber(floorNumber);
        status.setTotalSpots(spots.size());
        
        // Check if floor is under maintenance
        Optional<Floor> floor = floorRepository.findByFloorNumber(floorNumber);
        if (floor.isPresent()) {
            status.setUnderMaintenance(floor.get().getUnderMaintenance());
            status.setMaintenanceReason(floor.get().getMaintenanceReason());
        } else {
            status.setUnderMaintenance(false);
            status.setMaintenanceReason(null);
        }
        
        long availableSpots = spots.stream().filter(ParkingSpot::getIsAvailable).count();
        
        // If floor is under maintenance, show 0 available spots
        if (status.isUnderMaintenance()) {
            status.setAvailableSpots(0);
            status.setOccupiedSpots(spots.size());
        } else {
            status.setAvailableSpots((int) availableSpots);
            status.setOccupiedSpots((int) (spots.size() - availableSpots));
        }
        
        // Count by type
        Map<ParkingSpotType, Long> spotsByType = spots.stream()
            .collect(Collectors.groupingBy(ParkingSpot::getSpotType, Collectors.counting()));
        
        status.setMotorcycleSpots(spotsByType.getOrDefault(ParkingSpotType.MOTORCYCLE, 0L).intValue());
        status.setCompactSpots(spotsByType.getOrDefault(ParkingSpotType.COMPACT, 0L).intValue());
        status.setLargeSpots(spotsByType.getOrDefault(ParkingSpotType.LARGE, 0L).intValue());
        
        // Count available by type (only if not under maintenance)
        if (!status.isUnderMaintenance()) {
            Map<ParkingSpotType, Long> availableByType = spots.stream()
                .filter(ParkingSpot::getIsAvailable)
                .collect(Collectors.groupingBy(ParkingSpot::getSpotType, Collectors.counting()));
            
            status.setAvailableMotorcycleSpots(availableByType.getOrDefault(ParkingSpotType.MOTORCYCLE, 0L).intValue());
            status.setAvailableCompactSpots(availableByType.getOrDefault(ParkingSpotType.COMPACT, 0L).intValue());
            status.setAvailableLargeSpots(availableByType.getOrDefault(ParkingSpotType.LARGE, 0L).intValue());
        } else {
            // If under maintenance, show 0 available spots
            status.setAvailableMotorcycleSpots(0);
            status.setAvailableCompactSpots(0);
            status.setAvailableLargeSpots(0);
        }
        
        return status;
    }
    
    private String getTypePrefix(ParkingSpotType spotType) {
        return switch (spotType) {
            case MOTORCYCLE -> "M";
            case COMPACT -> "C";
            case LARGE -> "L";
        };
    }
}
