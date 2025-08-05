package learn.spring.smart_parking_lot.exception;

import learn.spring.smart_parking_lot.dto.ParkingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(NoAvailableSpotException.class)
    public ResponseEntity<ParkingResponse> handleNoAvailableSpotException(NoAvailableSpotException ex) {
        ParkingResponse response = ParkingResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ParkingResponse> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        ParkingResponse response = ParkingResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ParkingResponse> handleTicketNotFoundException(TicketNotFoundException ex) {
        ParkingResponse response = ParkingResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(ParkingLotException.class)
    public ResponseEntity<ParkingResponse> handleParkingLotException(ParkingLotException ex) {
        ParkingResponse response = ParkingResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ParkingResponse> handleGenericException(Exception ex) {
        ParkingResponse response = ParkingResponse.failure("An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
