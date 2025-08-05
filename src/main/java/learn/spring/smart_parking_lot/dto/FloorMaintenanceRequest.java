package learn.spring.smart_parking_lot.dto;

import jakarta.validation.constraints.NotNull;

public class FloorMaintenanceRequest {
    @NotNull(message = "Under maintenance status is required")
    private Boolean underMaintenance;

    private String maintenanceReason;

    public FloorMaintenanceRequest() {}

    public FloorMaintenanceRequest(Boolean underMaintenance, String maintenanceReason) {
        this.underMaintenance = underMaintenance;
        this.maintenanceReason = maintenanceReason;
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
}
