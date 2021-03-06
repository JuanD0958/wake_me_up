package co.anbora.wakemeup.domain.model;

public interface AlarmGeofence {

    Long internalId();

    Long remoteId();

    String id();

    String name();

    String description();

    Double latitude();

    Double longitude();

    Boolean state();

    Long createdAt();

    Long updatedAt();

    Long deletedAt();

    Boolean needsSync();

}
