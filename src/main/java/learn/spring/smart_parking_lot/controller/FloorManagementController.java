package learn.spring.smart_parking_lot.controller;

import learn.spring.smart_parking_lot.dto.AddFloorRequest;
import learn.spring.smart_parking_lot.dto.AddSpotRequest;
import learn.spring.smart_parking_lot.dto.FloorMaintenanceRequest;
import learn.spring.smart_parking_lot.dto.FloorStatus;
import learn.spring.smart_parking_lot.model.ParkingSpot;
import learn.spring.smart_parking_lot.service.FloorManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/floors")
@CrossOrigin(origins = "*")
public class FloorManagementController {
    
    private final FloorManagementService floorManagementService;
    
    @Autowired
    public FloorManagementController(FloorManagementService floorManagementService) {
        this.floorManagementService = floorManagementService;
    }
    
    @GetMapping
    public ResponseEntity<List<FloorStatus>> getAllFloors() {
        List<FloorStatus> floors = floorManagementService.getAllFloorsStatus();
        return ResponseEntity.ok(floors);
    }
    
    @GetMapping("/{floorNumber}")
    public ResponseEntity<FloorStatus> getFloorStatus(@PathVariable int floorNumber) {
        try {
            FloorStatus floor = floorManagementService.getFloorStatus(floorNumber);
            return ResponseEntity.ok(floor);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, String>> addFloor(@Valid @RequestBody AddFloorRequest request) {
        try {
            floorManagementService.addFloor(
                request.getFloorNumber(),
                request.getMotorcycleSpots(),
                request.getCompactSpots(),
                request.getLargeSpots()
            );
            
            String message = String.format("Floor %d added successfully with %d total spots", 
                request.getFloorNumber(), request.getTotalSpots());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", message, "success", "true"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage(), "success", "false"));
        }
    }
    
    @PostMapping("/spots")
    public ResponseEntity<Map<String, Object>> addParkingSpot(@Valid @RequestBody AddSpotRequest request) {
        try {
            ParkingSpot newSpot = floorManagementService.addParkingSpot(
                request.getFloorNumber(),
                request.getSpotType()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "message", "Parking spot added successfully",
                    "success", true,
                    "spotNumber", newSpot.getSpotNumber(),
                    "floor", newSpot.getFloor(),
                    "spotType", newSpot.getSpotType()
                ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", e.getMessage(), "success", false));
        }
    }
    
    @DeleteMapping("/{floorNumber}")
    public ResponseEntity<Map<String, String>> removeFloor(@PathVariable int floorNumber) {
        try {
            floorManagementService.removeFloor(floorNumber);
            return ResponseEntity.ok()
                .body(Map.of("message", "Floor " + floorNumber + " removed successfully", "success", "true"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage(), "success", "false"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage(), "success", "false"));
        }
    }
    
    @DeleteMapping("/spots/{spotNumber}")
    public ResponseEntity<Map<String, String>> removeParkingSpot(@PathVariable String spotNumber) {
        try {
            floorManagementService.removeParkingSpot(spotNumber);
            return ResponseEntity.ok()
                .body(Map.of("message", "Parking spot " + spotNumber + " removed successfully", "success", "true"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage(), "success", "false"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage(), "success", "false"));
        }
    }
    
    @PutMapping("/{floorNumber}/maintenance")
    public ResponseEntity<Map<String, String>> setFloorMaintenance(
            @PathVariable int floorNumber,
            @Valid @RequestBody FloorMaintenanceRequest request) {
        try {
            floorManagementService.setFloorMaintenanceMode(
                floorNumber,
                request.getUnderMaintenance(),
                request.getMaintenanceReason()
            );
            
            String action = request.getUnderMaintenance() ? "enabled" : "disabled";
            String message = String.format("Maintenance mode %s for floor %d", action, floorNumber);
            
            return ResponseEntity.ok()
                .body(Map.of("message", message, "success", "true"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage(), "success", "false"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage(), "success", "false"));
        }
    }
    
    @GetMapping("/maintenance")
    public ResponseEntity<Map<String, Object>> getMaintenanceInfo() {
        List<Integer> maintenanceFloors = floorManagementService.getFloorsUnderMaintenance();
        List<Integer> availableFloors = floorManagementService.getAvailableFloors();
        
        return ResponseEntity.ok(Map.of(
            "floorsUnderMaintenance", maintenanceFloors,
            "availableFloors", availableFloors,
            "totalMaintenanceFloors", maintenanceFloors.size(),
            "totalAvailableFloors", availableFloors.size()
        ));
    }
}
