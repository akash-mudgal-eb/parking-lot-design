package learn.spring.smart_parking_lot.controller;

import learn.spring.smart_parking_lot.dto.ParkingLotStatus;
import learn.spring.smart_parking_lot.dto.ParkingResponse;
import learn.spring.smart_parking_lot.dto.VehicleEntryRequest;
import learn.spring.smart_parking_lot.dto.VehicleExitRequest;
import learn.spring.smart_parking_lot.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "*")
public class ParkingController {
    
    private final ParkingService parkingService;
    
    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }
    
    @PostMapping("/entry")
    public ResponseEntity<ParkingResponse> parkVehicle(@Valid @RequestBody VehicleEntryRequest request) {
        ParkingResponse response = parkingService.parkVehicle(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/exit")
    public ResponseEntity<ParkingResponse> exitVehicle(@Valid @RequestBody VehicleExitRequest request) {
        ParkingResponse response = parkingService.exitVehicle(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status")
    public ResponseEntity<ParkingLotStatus> getParkingLotStatus() {
        ParkingLotStatus status = parkingService.getParkingLotStatus();
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Smart Parking Lot API is running");
    }
}
