package learn.spring.smart_parking_lot.dto;

public class ParkingLotStatus {
    private long totalSpots;
    private long availableSpots;
    private long occupiedSpots;
    private long motorcycleSpots;
    private long compactSpots;
    private long largeSpots;
    private long activeTickets;

    public ParkingLotStatus() {}

    // Getters and Setters
    public long getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(long totalSpots) {
        this.totalSpots = totalSpots;
    }

    public long getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(long availableSpots) {
        this.availableSpots = availableSpots;
    }

    public long getOccupiedSpots() {
        return occupiedSpots;
    }

    public void setOccupiedSpots(long occupiedSpots) {
        this.occupiedSpots = occupiedSpots;
    }

    public long getMotorcycleSpots() {
        return motorcycleSpots;
    }

    public void setMotorcycleSpots(long motorcycleSpots) {
        this.motorcycleSpots = motorcycleSpots;
    }

    public long getCompactSpots() {
        return compactSpots;
    }

    public void setCompactSpots(long compactSpots) {
        this.compactSpots = compactSpots;
    }

    public long getLargeSpots() {
        return largeSpots;
    }

    public void setLargeSpots(long largeSpots) {
        this.largeSpots = largeSpots;
    }

    public long getActiveTickets() {
        return activeTickets;
    }

    public void setActiveTickets(long activeTickets) {
        this.activeTickets = activeTickets;
    }
}
