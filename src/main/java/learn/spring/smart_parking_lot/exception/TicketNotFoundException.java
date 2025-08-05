package learn.spring.smart_parking_lot.exception;

public class TicketNotFoundException extends ParkingLotException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}
