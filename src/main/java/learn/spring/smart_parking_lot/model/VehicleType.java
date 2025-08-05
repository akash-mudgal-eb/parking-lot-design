package learn.spring.smart_parking_lot.model;

public enum VehicleType {
    MOTORCYCLE(1),
    CAR(2),
    BUS(4);

    private final int spotsRequired;

    VehicleType(int spotsRequired) {
        this.spotsRequired = spotsRequired;
    }

    public int getSpotsRequired() {
        return spotsRequired;
    }
}
