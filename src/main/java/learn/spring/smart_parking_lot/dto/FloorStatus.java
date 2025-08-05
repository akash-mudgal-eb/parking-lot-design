package learn.spring.smart_parking_lot.dto;

public class FloorStatus {
    private int floorNumber;
    private int totalSpots;
    private int availableSpots;
    private int occupiedSpots;
    private int motorcycleSpots;
    private int compactSpots;
    private int largeSpots;
    private int availableMotorcycleSpots;
    private int availableCompactSpots;
    private int availableLargeSpots;
    private boolean underMaintenance;
    private String maintenanceReason;

    public FloorStatus() {}

    // Getters and Setters
    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    public int getOccupiedSpots() {
        return occupiedSpots;
    }

    public void setOccupiedSpots(int occupiedSpots) {
        this.occupiedSpots = occupiedSpots;
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

    public int getAvailableMotorcycleSpots() {
        return availableMotorcycleSpots;
    }

    public void setAvailableMotorcycleSpots(int availableMotorcycleSpots) {
        this.availableMotorcycleSpots = availableMotorcycleSpots;
    }

    public int getAvailableCompactSpots() {
        return availableCompactSpots;
    }

    public void setAvailableCompactSpots(int availableCompactSpots) {
        this.availableCompactSpots = availableCompactSpots;
    }

    public int getAvailableLargeSpots() {
        return availableLargeSpots;
    }

    public void setAvailableLargeSpots(int availableLargeSpots) {
        this.availableLargeSpots = availableLargeSpots;
    }

    public boolean isUnderMaintenance() {
        return underMaintenance;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    public String getMaintenanceReason() {
        return maintenanceReason;
    }

    public void setMaintenanceReason(String maintenanceReason) {
        this.maintenanceReason = maintenanceReason;
    }

    @Override
    public String toString() {
        return "FloorStatus{" +
                "floorNumber=" + floorNumber +
                ", totalSpots=" + totalSpots +
                ", availableSpots=" + availableSpots +
                ", occupiedSpots=" + occupiedSpots +
                ", motorcycleSpots=" + motorcycleSpots +
                ", compactSpots=" + compactSpots +
                ", largeSpots=" + largeSpots +
                ", underMaintenance=" + underMaintenance +
                ", maintenanceReason='" + maintenanceReason + '\'' +
                '}';
    }
}
