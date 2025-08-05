package learn.spring.smart_parking_lot.repository;

import learn.spring.smart_parking_lot.model.ParkingTicket;
import learn.spring.smart_parking_lot.model.TicketStatus;
import learn.spring.smart_parking_lot.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {
    Optional<ParkingTicket> findByTicketNumber(String ticketNumber);
    
    Optional<ParkingTicket> findByVehicleAndStatus(Vehicle vehicle, TicketStatus status);
    
    List<ParkingTicket> findByStatus(TicketStatus status);
    
    List<ParkingTicket> findByEntryTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("SELECT pt FROM ParkingTicket pt WHERE pt.vehicle.licensePlate = :licensePlate AND pt.status = :status")
    Optional<ParkingTicket> findByLicensePlateAndStatus(@Param("licensePlate") String licensePlate, @Param("status") TicketStatus status);
    
    @Query("SELECT COUNT(pt) FROM ParkingTicket pt WHERE pt.status = 'ACTIVE'")
    long countActiveTickets();
}
