package learn.spring.smart_parking_lot.config;

import learn.spring.smart_parking_lot.model.Floor;
import learn.spring.smart_parking_lot.model.ParkingSpot;
import learn.spring.smart_parking_lot.model.ParkingSpotType;
import learn.spring.smart_parking_lot.repository.FloorRepository;
import learn.spring.smart_parking_lot.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final ParkingSpotRepository parkingSpotRepository;
    private final FloorRepository floorRepository;
    
    @Autowired
    public DataInitializer(ParkingSpotRepository parkingSpotRepository, FloorRepository floorRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.floorRepository = floorRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        if (parkingSpotRepository.count() == 0) {
            initializeParkingSpots();
        }
    }
    
    private void initializeParkingSpots() {
        System.out.println("Initializing parking spots...");
        
        // Initialize parking spots for a 3-floor parking lot
        // Floor 1: 20 spots (10 motorcycle, 8 compact, 2 large)
        // Floor 2: 20 spots (8 motorcycle, 10 compact, 2 large)
        // Floor 3: 20 spots (5 motorcycle, 10 compact, 5 large)
        
        // Create floor entities first
        for (int i = 1; i <= 3; i++) {
            if (!floorRepository.existsByFloorNumber(i)) {
                Floor floor = new Floor(i, "Floor " + i);
                floorRepository.save(floor);
            }
        }
        
        createFloorSpots(1, 10, 8, 2);
        createFloorSpots(2, 8, 10, 2);
        createFloorSpots(3, 5, 10, 5);
        
        long totalSpots = parkingSpotRepository.count();
        System.out.println("Initialized " + totalSpots + " parking spots across 3 floors");
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
}
