package co.anbora.wakemeup.domain.model;

public interface HistoryAlarm {

    Long internalId();

    Long remoteId();

    String id();

    Double latitude();

    Double longitude();

    String alarmId();

    Long createdAt();

    Long deletedAt();

    Long updatedAt();

    Boolean needSync();

}
