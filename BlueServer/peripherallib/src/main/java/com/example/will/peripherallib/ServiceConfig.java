package com.example.will.peripherallib;

import android.bluetooth.BluetoothGattService;

import java.util.UUID;

/**
 * @author will4906.
 * @Time 2017/3/9.
 */

public class ServiceConfig {

    private UUID uuid;
    private int serviceType = BluetoothGattService.SERVICE_TYPE_PRIMARY;

    public ServiceConfig(UUID uuid){
        this.uuid = uuid;
    }

    public ServiceConfig(String uuid){
        this.uuid = UUID.fromString(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }
}
