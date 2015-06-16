package de.hawai.bicycle_tracking.server.dto;

import java.util.UUID;

/**
 * Created by torben on 12.05.15.
 */
public class TourListEntryDTO {
    public UUID id = null;
    public String name = null;
    public UUID bikeID = null;
    public Double lengthInKm = null;
    public String startAt = null;
    public String finishedAt = null;

    @Override
    public String toString() {
        return "TourListEntryDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bikeID=" + bikeID +
                ", lengthInKm=" + lengthInKm +
                ", startAt='" + startAt.toString() + '\'' +
                ", finishedAt='" + finishedAt.toString() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TourListEntryDTO that = (TourListEntryDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (bikeID != null ? !bikeID.equals(that.bikeID) : that.bikeID != null) return false;
        if (lengthInKm != null ? !lengthInKm.equals(that.lengthInKm) : that.lengthInKm != null) return false;
        if (startAt != null ? !startAt.equals(that.startAt) : that.startAt != null) return false;
        return !(finishedAt != null ? !finishedAt.equals(that.finishedAt) : that.finishedAt != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (bikeID != null ? bikeID.hashCode() : 0);
        result = 31 * result + (lengthInKm != null ? lengthInKm.hashCode() : 0);
        result = 31 * result + (startAt != null ? startAt.hashCode() : 0);
        result = 31 * result + (finishedAt != null ? finishedAt.hashCode() : 0);
        return result;
    }
}
