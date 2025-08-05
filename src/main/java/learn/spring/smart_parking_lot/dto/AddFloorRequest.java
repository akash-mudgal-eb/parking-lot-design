package learn.spring.smart_parking_lot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddFloorRequest {
    @NotNull(message = "Floor number is required")
    @Min(value = 1, message = "Floor number must be at least 1")
    private Integer floorNumber;

    @Min(value = 0, message = "Motorcycle spots must be non-negative")
    private int motorcycleSpots = 0;

    @Min(value = 0, message = "Compact spots must be non-negative")
    private int compactSpots = 0;

    @Min(value = 0, message = "Large spots must be non-negative")
    private int largeSpots = 0;

    public AddFloorRequest() {}

    public AddFloorRequest(Integer floorNumber, int motorcycleSpots, int compactSpots, int largeSpots) {
        this.floorNumber = floorNumber;
        this.motorcycleSpots = motorcycleSpots;
        this.compactSpots = compactSpots;
        this.largeSpots = largeSpots;
    }

    // Getters and Setters
    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getMotorcycleSpots() {
        return motorcycleSpots;
    }

    public void setMotorcycleSpots(int motorcycleSpots) {
        this.motorcycleSpots = motorcycleSpots;
    }

    public int getCompactSpots() {
        return compactSpots;
    }

    public void setCompactSpots(int compactSpots) {
        this.compactSpots = compactSpots;
    }

    public int getLargeSpots() {
        return largeSpots;
    }

    public void setLargeSpots(int largeSpots) {
        this.largeSpots = largeSpots;
    }

    public int getTotalSpots() {
        return motorcycleSpots + compactSpots + largeSpots;
    }
}
