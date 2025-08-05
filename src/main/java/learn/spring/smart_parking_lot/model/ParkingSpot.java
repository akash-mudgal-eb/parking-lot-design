package learn.spring.smart_parking_lot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Spot number is required")
    @Column(name = "spot_number", unique = true, nullable = false)
    private String spotNumber;

    @Min(value = 1, message = "Floor must be at least 1")
    @Column(name = "floor", nullable = false)
    private Integer floor;

    @NotNull(message = "Spot type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "spot_type", nullable = false)
    private ParkingSpotType spotType;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @OneToOne(mappedBy = "parkingSpot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ParkingTicket currentTicket;

    public ParkingSpot() {}

    public ParkingSpot(String spotNumber, Integer floor, ParkingSpotType spotType) {
        this.spotNumber = spotNumber;
        this.floor = floor;
        this.spotType = spotType;
        this.isAvailable = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public ParkingSpotType getSpotType() {
        return spotType;
    }

    public void setSpotType(ParkingSpotType spotType) {
        this.spotType = spotType;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public ParkingTicket getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(ParkingTicket currentTicket) {
        this.currentTicket = currentTicket;
    }

    public void occupy() {
        this.isAvailable = false;
    }

    public void free() {
        this.isAvailable = true;
        this.currentTicket = null;
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "id=" + id +
                ", spotNumber='" + spotNumber + '\'' +
                ", floor=" + floor +
                ", spotType=" + spotType +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
