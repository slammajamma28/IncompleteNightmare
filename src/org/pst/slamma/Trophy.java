package org.pst.slamma;

import java.time.LocalDateTime;

public class Trophy {

    public int log_num;
    public LocalDateTime timestamp;
    public double rarity_percentage;
    public LocalDateTime audit_timestamp;
    public int owners;

    public int getOwners() {
        return owners;
    }

    public void setOwners(int owners) {
        this.owners = owners;
    }

    public Trophy() {
        this.audit_timestamp = LocalDateTime.now();
    }

    public int getLog_num() {
        return log_num;
    }

    public void setLog_num(int log_num) {
        this.log_num = log_num;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getAudit_timestamp() {
        return audit_timestamp;
    }

    public void setAudit_timestamp(LocalDateTime audit_timestamp) {
        this.audit_timestamp = audit_timestamp;
    }

    public double getRarity_percentage() { return rarity_percentage; }

    public void setRarity_percentage(double rarity_percentage) { this.rarity_percentage = rarity_percentage; }

}