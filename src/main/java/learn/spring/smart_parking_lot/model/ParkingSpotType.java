package learn.spring.smart_parking_lot.model;

public enum ParkingSpotType {
    MOTORCYCLE,
    COMPACT,
    LARGE;

    public boolean canFitVehicle(VehicleType vehicleType) {
        return switch (this) {
            case MOTORCYCLE -> vehicleType == VehicleType.MOTORCYCLE;
            case COMPACT -> vehicleType == VehicleType.MOTORCYCLE || vehicleType == VehicleType.CAR;
            case LARGE -> true; // Large spots can fit any vehicle
        };
    }
}
