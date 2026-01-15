package com.example.monitoring.model;

public class DeviceSyncEvent {
    private Long deviceId;
    private String deviceName;
    private Double maxConsumption;
    private String eventType;

    public DeviceSyncEvent() {}

    public DeviceSyncEvent(Long deviceId, String deviceName, Double maxConsumption, String eventType) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.maxConsumption = maxConsumption;
        this.eventType = eventType;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Double getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(Double maxConsumption) {
        this.maxConsumption = maxConsumption;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}