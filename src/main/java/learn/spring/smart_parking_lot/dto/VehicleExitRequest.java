package learn.spring.smart_parking_lot.dto;

import jakarta.validation.constraints.NotBlank;

public class VehicleExitRequest {
    @NotBlank(message = "License plate is required")
    private String licensePlate;

    public VehicleExitRequest() {}

    public VehicleExitRequest(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
