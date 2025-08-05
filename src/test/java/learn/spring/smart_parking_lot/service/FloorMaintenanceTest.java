package learn.spring.smart_parking_lot.service;

import learn.spring.smart_parking_lot.dto.FloorStatus;
import learn.spring.smart_parking_lot.dto.ParkingResponse;
import learn.spring.smart_parking_lot.dto.VehicleEntryRequest;
import learn.spring.smart_parking_lot.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FloorMaintenanceTest {

    @Autowired
    private FloorManagementService floorManagementService;

    @Autowired
    private ParkingService parkingService;

    @Test
    public void testSetFloorToMaintenanceMode() {
        // Set floor 1 to maintenance mode
        floorManagementService.setFloorMaintenanceMode(1, true, "Cleaning and repairs");
        
        // Verify floor is marked as under maintenance
        FloorStatus floor1Status = floorManagementService.getFloorStatus(1);
        assertTrue(floor1Status.isUnderMaintenance());
        assertEquals("Cleaning and repairs", floor1Status.getMaintenanceReason());
        
        // Verify available spots are 0 for maintenance floor
        assertEquals(0, floor1Status.getAvailableSpots());
        assertEquals(0, floor1Status.getAvailableMotorcycleSpots());
        assertEquals(0, floor1Status.getAvailableCompactSpots());
        assertEquals(0, floor1Status.getAvailableLargeSpots());
    }

    @Test
    public void testVehicleCannotParkOnMaintenanceFloor() {
        // Set floor 1 to maintenance mode
        floorManagementService.setFloorMaintenanceMode(1, true, "Under maintenance");
        
        // Try to park a vehicle - it should get assigned to a different floor
        VehicleEntryRequest request = new VehicleEntryRequest("TEST001", VehicleType.CAR, "Test User");
        ParkingResponse response = parkingService.parkVehicle(request);
        
        assertTrue(response.isSuccess(), "Parking should succeed on non-maintenance floors. Error: " + response.getMessage());
        assertNotNull(response.getSpotNumber());
        
        // Verify the assigned spot is NOT on floor 1
        String spotNumber = response.getSpotNumber();
        int assignedFloor = Integer.parseInt(spotNumber.split("-")[0]);
        assertNotEquals(1, assignedFloor, 
            "Vehicle should not be assigned to maintenance floor 1");
    }

    @Test
    public void testCannotSetMaintenanceModeWithOccupiedSpots() {
        // First park a vehicle
        VehicleEntryRequest request = new VehicleEntryRequest("OCCUPIED001", VehicleType.CAR, "Test User");
        ParkingResponse parkResponse = parkingService.parkVehicle(request);
        
        assertTrue(parkResponse.isSuccess(), "Initial parking should succeed. Error: " + parkResponse.getMessage());
        
        // Extract the floor number from the spot number (format: "floorNumber-spotNumber")
        String spotNumber = parkResponse.getSpotNumber();
        int floorNumber = Integer.parseInt(spotNumber.split("-")[0]);
        
        // Try to set maintenance mode on the floor where the vehicle is parked
        // This should fail because there are occupied spots
        assertThrows(IllegalStateException.class, () -> {
            floorManagementService.setFloorMaintenanceMode(floorNumber, true, "Should fail");
        }, "Should not be able to set maintenance mode on floor with occupied spots");
    }

    @Test
    public void testDisableMaintenanceMode() {
        // Set floor 2 to maintenance mode
        floorManagementService.setFloorMaintenanceMode(2, true, "Maintenance");
        
        // Verify it's in maintenance mode
        FloorStatus beforeStatus = floorManagementService.getFloorStatus(2);
        assertTrue(beforeStatus.isUnderMaintenance());
        
        // Disable maintenance mode
        floorManagementService.setFloorMaintenanceMode(2, false, null);
        
        // Verify it's no longer in maintenance mode
        FloorStatus afterStatus = floorManagementService.getFloorStatus(2);
        assertFalse(afterStatus.isUnderMaintenance());
        assertNull(afterStatus.getMaintenanceReason());
        
        // Verify spots are available again
        assertTrue(afterStatus.getAvailableSpots() > 0);
    }

    @Test
    public void testGetMaintenanceFloorsList() {
        // Set multiple floors to maintenance
        floorManagementService.setFloorMaintenanceMode(1, true, "Reason 1");
        floorManagementService.setFloorMaintenanceMode(3, true, "Reason 3");
        
        // Get maintenance floors list
        List<Integer> maintenanceFloors = floorManagementService.getFloorsUnderMaintenance();
        
        assertTrue(maintenanceFloors.contains(1));
        assertTrue(maintenanceFloors.contains(3));
        assertFalse(maintenanceFloors.contains(2));
        
        // Get available floors list
        List<Integer> availableFloors = floorManagementService.getAvailableFloors();
        assertFalse(availableFloors.contains(1));
        assertTrue(availableFloors.contains(2));
        assertFalse(availableFloors.contains(3));
    }

    @Test
    public void testAllFloorsStatusWithMaintenance() {
        // Set floor 2 to maintenance
        floorManagementService.setFloorMaintenanceMode(2, true, "Regular maintenance");
        
        // Get all floors status
        List<FloorStatus> allFloors = floorManagementService.getAllFloorsStatus();
        
        // Find floor 2 and verify its status
        FloorStatus floor2 = allFloors.stream()
            .filter(f -> f.getFloorNumber() == 2)
            .findFirst()
            .orElseThrow(() -> new AssertionError("Floor 2 not found"));
        
        assertTrue(floor2.isUnderMaintenance());
        assertEquals("Regular maintenance", floor2.getMaintenanceReason());
        assertEquals(0, floor2.getAvailableSpots());
        
        // Verify other floors are not affected
        FloorStatus floor1 = allFloors.stream()
            .filter(f -> f.getFloorNumber() == 1)
            .findFirst()
            .orElseThrow(() -> new AssertionError("Floor 1 not found"));
        
        assertFalse(floor1.isUnderMaintenance());
        assertTrue(floor1.getAvailableSpots() > 0);
    }
}
