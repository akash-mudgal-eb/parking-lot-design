package learn.spring.smart_parking_lot.exception;

public class VehicleNotFoundException extends ParkingLotException {
    public VehicleNotFoundException(String message) {
        super(message);
    }
}
