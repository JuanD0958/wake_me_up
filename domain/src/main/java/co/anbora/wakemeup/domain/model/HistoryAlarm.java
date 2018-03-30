package co.anbora.wakemeup.domain.model;

public interface HistoryAlarm {

    Long internalId();

    Long remoteId();

    String id();

    String alarmId();

    Long createdAt();

    Long deletedAt();

    Long updatedAt();

    Boolean needSync();

}
