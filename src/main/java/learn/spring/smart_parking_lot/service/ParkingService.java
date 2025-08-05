package learn.spring.smart_parking_lot.service;

import learn.spring.smart_parking_lot.dto.ParkingLotStatus;
import learn.spring.smart_parking_lot.dto.ParkingResponse;
import learn.spring.smart_parking_lot.dto.VehicleEntryRequest;
import learn.spring.smart_parking_lot.dto.VehicleExitRequest;
import learn.spring.smart_parking_lot.exception.TicketNotFoundException;
import learn.spring.smart_parking_lot.model.*;
import learn.spring.smart_parking_lot.repository.ParkingSpotRepository;
import learn.spring.smart_parking_lot.repository.ParkingTicketRepository;
import learn.spring.smart_parking_lot.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Transactional
public class ParkingService {
    
    private final VehicleRepository vehicleRepository;
    private final ParkingTicketRepository ticketRepository;
    private final ParkingSpotAllocationService spotAllocationService;
    private final FeeCalculationService feeCalculationService;
    
    @Autowired
    public ParkingService(VehicleRepository vehicleRepository,
                         ParkingTicketRepository ticketRepository,
                         ParkingSpotAllocationService spotAllocationService,
                         FeeCalculationService feeCalculationService) {
        this.vehicleRepository = vehicleRepository;
        this.ticketRepository = ticketRepository;
        this.spotAllocationService = spotAllocationService;
        this.feeCalculationService = feeCalculationService;
    }
    
    public ParkingResponse parkVehicle(VehicleEntryRequest request) {
        try {
            // Check if vehicle is already parked
            if (isVehicleCurrentlyParked(request.getLicensePlate())) {
                return ParkingResponse.failure("Vehicle is already parked in the lot");
            }
            
            // Create or get existing vehicle
            Vehicle vehicle = getOrCreateVehicle(request);
            
            // Allocate parking spot
            ParkingSpot spot = spotAllocationService.allocateSpot(vehicle.getVehicleType());
            
            // Create parking ticket
            LocalDateTime entryTime = LocalDateTime.now();
            String ticketNumber = generateTicketNumber();
            
            ParkingTicket ticket = new ParkingTicket(ticketNumber, vehicle, spot, entryTime);
            ticket = ticketRepository.save(ticket);
            
            // Update spot with current ticket
            spot.setCurrentTicket(ticket);
            
            // Prepare response
            ParkingResponse response = ParkingResponse.success("Vehicle parked successfully");
            response.setTicketNumber(ticketNumber);
            response.setSpotNumber(spot.getSpotNumber());
            response.setEntryTime(entryTime);
            
            return response;
            
        } catch (Exception e) {
            return ParkingResponse.failure("Failed to park vehicle: " + e.getMessage());
        }
    }
    
    public ParkingResponse exitVehicle(VehicleExitRequest request) {
        try {
            // Find active ticket for the vehicle
            ParkingTicket ticket = ticketRepository
                .findByLicensePlateAndStatus(request.getLicensePlate(), TicketStatus.ACTIVE)
                .orElseThrow(() -> new TicketNotFoundException("No active parking ticket found for license plate: " + request.getLicensePlate()));
            
            LocalDateTime exitTime = LocalDateTime.now();
            
            // Calculate fee
            BigDecimal fee = feeCalculationService.calculateFee(
                ticket.getVehicle().getVehicleType(),
                ticket.getEntryTime(),
                exitTime
            );
            
            long durationInMinutes = feeCalculationService.calculateDurationInMinutes(
                ticket.getEntryTime(),
                exitTime
            );
            
            // Update ticket
            ticket.markAsExited(exitTime);
            ticket.markAsPaid(fee);
            ticketRepository.save(ticket);
            
            // Free the parking spot
            spotAllocationService.freeSpot(ticket.getParkingSpot());
            
            // Prepare response
            ParkingResponse response = ParkingResponse.success("Vehicle exit processed successfully");
            response.setTicketNumber(ticket.getTicketNumber());
            response.setSpotNumber(ticket.getParkingSpot().getSpotNumber());
            response.setEntryTime(ticket.getEntryTime());
            response.setExitTime(exitTime);
            response.setFee(fee);
            response.setDurationInMinutes(durationInMinutes);
            
            return response;
            
        } catch (Exception e) {
            return ParkingResponse.failure("Failed to process vehicle exit: " + e.getMessage());
        }
    }
    
    public ParkingLotStatus getParkingLotStatus() {
        ParkingLotStatus status = new ParkingLotStatus();
        
        long availableSpots = spotAllocationService.getAvailableSpotCount();
        long activeTickets = ticketRepository.countActiveTickets();
        long totalSpots = availableSpots + activeTickets; // Total = available + occupied
        long occupiedSpots = activeTickets;
        
        long motorcycleSpots = spotAllocationService.getAvailableSpotCountByType(ParkingSpotType.MOTORCYCLE);
        long compactSpots = spotAllocationService.getAvailableSpotCountByType(ParkingSpotType.COMPACT);
        long largeSpots = spotAllocationService.getAvailableSpotCountByType(ParkingSpotType.LARGE);
        
        status.setTotalSpots(totalSpots);
        status.setAvailableSpots(availableSpots);
        status.setOccupiedSpots(occupiedSpots);
        status.setActiveTickets(activeTickets);
        status.setMotorcycleSpots(motorcycleSpots);
        status.setCompactSpots(compactSpots);
        status.setLargeSpots(largeSpots);
        
        return status;
    }
    
    private Vehicle getOrCreateVehicle(VehicleEntryRequest request) {
        return vehicleRepository.findByLicensePlate(request.getLicensePlate())
            .orElseGet(() -> {
                Vehicle newVehicle = new Vehicle(request.getLicensePlate(), request.getVehicleType(), request.getOwnerName());
                return vehicleRepository.save(newVehicle);
            });
    }
    
    private boolean isVehicleCurrentlyParked(String licensePlate) {
        return ticketRepository.findByLicensePlateAndStatus(licensePlate, TicketStatus.ACTIVE).isPresent();
    }
    
    private String generateTicketNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "PKT-" + timestamp + "-" + uuid;
    }
}
