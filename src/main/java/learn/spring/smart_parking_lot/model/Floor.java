package learn.spring.smart_parking_lot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "floors")
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Floor number is required")
    @Column(name = "floor_number", unique = true, nullable = false)
    @Min(value = 1, message = "Floor number must be at least 1")
    private Integer floorNumber;

    @Column(name = "under_maintenance", nullable = false)
    private Boolean underMaintenance = false;

    @Column(name = "maintenance_reason")
    private String maintenanceReason;

    @Column(name = "floor_name")
    private String floorName;

    public Floor() {}

    public Floor(Integer floorNumber) {
        this.floorNumber = floorNumber;
        this.underMaintenance = false;
    }

    public Floor(Integer floorNumber, String floorName) {
        this.floorNumber = floorNumber;
        this.floorName = floorName;
        this.underMaintenance = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Boolean getUnderMaintenance() {
        return underMaintenance;
    }

    public void setUnderMaintenance(Boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    public String getMaintenanceReason() {
        return maintenanceReason;
    }

    public void setMaintenanceReason(String maintenanceReason) {
        this.maintenanceReason = maintenanceReason;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public void setMaintenanceMode(boolean underMaintenance, String reason) {
        this.underMaintenance = underMaintenance;
        this.maintenanceReason = underMaintenance ? reason : null;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "id=" + id +
                ", floorNumber=" + floorNumber +
                ", underMaintenance=" + underMaintenance +
                ", maintenanceReason='" + maintenanceReason + '\'' +
                ", floorName='" + floorName + '\'' +
                '}';
    }
}
