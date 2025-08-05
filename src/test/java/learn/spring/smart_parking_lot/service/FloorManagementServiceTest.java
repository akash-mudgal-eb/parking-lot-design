package learn.spring.smart_parking_lot.service;

import learn.spring.smart_parking_lot.dto.FloorStatus;
import learn.spring.smart_parking_lot.model.ParkingSpotType;
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
public class FloorManagementServiceTest {

    @Autowired
    private FloorManagementService floorManagementService;

    @Test
    public void testAddNewFloor() {
        // Add a new floor
        floorManagementService.addFloor(4, 5, 10, 3);
        
        // Verify the floor was created
        FloorStatus floor4 = floorManagementService.getFloorStatus(4);
        assertEquals(4, floor4.getFloorNumber());
        assertEquals(18, floor4.getTotalSpots()); // 5 + 10 + 3
        assertEquals(18, floor4.getAvailableSpots()); // All should be available
        assertEquals(5, floor4.getMotorcycleSpots());
        assertEquals(10, floor4.getCompactSpots());
        assertEquals(3, floor4.getLargeSpots());
    }

    @Test
    public void testAddParkingSpot() {
        // Add a single spot to floor 1
        var newSpot = floorManagementService.addParkingSpot(1, ParkingSpotType.COMPACT);
        
        assertNotNull(newSpot);
        assertEquals(1, newSpot.getFloor());
        assertEquals(ParkingSpotType.COMPACT, newSpot.getSpotType());
        assertTrue(newSpot.getIsAvailable());
        assertTrue(newSpot.getSpotNumber().startsWith("1-C-"));
    }

    @Test
    public void testGetAllFloorsStatus() {
        List<FloorStatus> floors = floorManagementService.getAllFloorsStatus();
        
        // Should have the initial 3 floors
        assertTrue(floors.size() >= 3);
        
        // Verify floors are sorted by number
        for (int i = 1; i < floors.size(); i++) {
            assertTrue(floors.get(i-1).getFloorNumber() <= floors.get(i).getFloorNumber());
        }
    }

    @Test
    public void testAddDuplicateFloor() {
        // Try to add floor 1 which already exists
        assertThrows(IllegalArgumentException.class, () -> {
            floorManagementService.addFloor(1, 5, 5, 5);
        });
    }
}
