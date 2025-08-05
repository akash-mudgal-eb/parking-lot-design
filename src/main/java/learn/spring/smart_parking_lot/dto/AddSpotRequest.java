package learn.spring.smart_parking_lot.dto;

import learn.spring.smart_parking_lot.model.ParkingSpotType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddSpotRequest {
    @NotNull(message = "Floor number is required")
    @Min(value = 1, message = "Floor number must be at least 1")
    private Integer floorNumber;

    @NotNull(message = "Spot type is required")
    private ParkingSpotType spotType;

    public AddSpotRequest() {}

    public AddSpotRequest(Integer floorNumber, ParkingSpotType spotType) {
        this.floorNumber = floorNumber;
        this.spotType = spotType;
    }

    // Getters and Setters
    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public ParkingSpotType getSpotType() {
        return spotType;
    }

    public void setSpotType(ParkingSpotType spotType) {
        this.spotType = spotType;
    }
}
