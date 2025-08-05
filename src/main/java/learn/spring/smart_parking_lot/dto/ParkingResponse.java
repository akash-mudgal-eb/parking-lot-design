package learn.spring.smart_parking_lot.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingResponse {
    private boolean success;
    private String message;
    private String ticketNumber;
    private String spotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private BigDecimal fee;
    private Long durationInMinutes;

    public ParkingResponse() {}

    public ParkingResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ParkingResponse success(String message) {
        return new ParkingResponse(true, message);
    }

    public static ParkingResponse failure(String message) {
        return new ParkingResponse(false, message);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Long getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}
