package learn.spring.smart_parking_lot.service;

import learn.spring.smart_parking_lot.model.VehicleType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class FeeCalculationService {
    
    // Base rates per hour for different vehicle types
    private static final BigDecimal MOTORCYCLE_RATE = new BigDecimal("2.00");
    private static final BigDecimal CAR_RATE = new BigDecimal("5.00");
    private static final BigDecimal BUS_RATE = new BigDecimal("10.00");
    
    // Minimum fee (for first 15 minutes)
    private static final BigDecimal MINIMUM_FEE = new BigDecimal("1.00");
    
    public BigDecimal calculateFee(VehicleType vehicleType, LocalDateTime entryTime, LocalDateTime exitTime) {
        if (entryTime.isAfter(exitTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }
        
        Duration duration = Duration.between(entryTime, exitTime);
        long minutes = duration.toMinutes();
        
        // Minimum charge for first 15 minutes
        if (minutes <= 15) {
            return MINIMUM_FEE;
        }
        
        // Calculate hours (ceiling)
        double hours = Math.ceil(minutes / 60.0);
        BigDecimal hourlyRate = getHourlyRate(vehicleType);
        
        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }
    
    private BigDecimal getHourlyRate(VehicleType vehicleType) {
        return switch (vehicleType) {
            case MOTORCYCLE -> MOTORCYCLE_RATE;
            case CAR -> CAR_RATE;
            case BUS -> BUS_RATE;
        };
    }
    
    public long calculateDurationInMinutes(LocalDateTime entryTime, LocalDateTime exitTime) {
        return Duration.between(entryTime, exitTime).toMinutes();
    }
}
