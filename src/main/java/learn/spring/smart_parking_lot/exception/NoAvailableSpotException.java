package learn.spring.smart_parking_lot.exception;

public class NoAvailableSpotException extends ParkingLotException {
    public NoAvailableSpotException(String message) {
        super(message);
    }
}
