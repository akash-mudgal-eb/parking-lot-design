package learn.spring.smart_parking_lot.service;

import learn.spring.smart_parking_lot.dto.ParkingResponse;
import learn.spring.smart_parking_lot.dto.VehicleEntryRequest;
import learn.spring.smart_parking_lot.dto.VehicleExitRequest;
import learn.spring.smart_parking_lot.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ParkingServiceTest {

    @Autowired
    private ParkingService parkingService;

    @Test
    public void testParkAndExitVehicle() {
        // Test parking a car
        VehicleEntryRequest entryRequest = new VehicleEntryRequest("ABC123", VehicleType.CAR, "John Doe");
        ParkingResponse parkResponse = parkingService.parkVehicle(entryRequest);
        
        assertTrue(parkResponse.isSuccess());
        assertNotNull(parkResponse.getTicketNumber());
        assertNotNull(parkResponse.getSpotNumber());
        
        // Test exiting the car
        VehicleExitRequest exitRequest = new VehicleExitRequest("ABC123");
        ParkingResponse exitResponse = parkingService.exitVehicle(exitRequest);
        
        assertTrue(exitResponse.isSuccess());
        assertNotNull(exitResponse.getFee());
        assertTrue(exitResponse.getFee().doubleValue() >= 0);
    }

    @Test
    public void testParkSameVehicleTwice() {
        // Park a vehicle
        VehicleEntryRequest entryRequest = new VehicleEntryRequest("XYZ789", VehicleType.MOTORCYCLE, "Jane Doe");
        ParkingResponse firstPark = parkingService.parkVehicle(entryRequest);
        assertTrue(firstPark.isSuccess());
        
        // Try to park the same vehicle again
        ParkingResponse secondPark = parkingService.parkVehicle(entryRequest);
        assertFalse(secondPark.isSuccess());
        assertTrue(secondPark.getMessage().contains("already parked"));
    }
}
