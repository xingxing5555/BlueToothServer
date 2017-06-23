package com.example.will.peripherallib;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.UUID;

/**
 * @author will4906.
 * @Time 2017/3/9.
 */

public class CharacteristicConfig {

    private UUID uuid;
    private int properties = BluetoothGattCharacteristic.PROPERTY_NOTIFY
            | BluetoothGattCharacteristic.PROPERTY_INDICATE
            | BluetoothGattCharacteristic.PROPERTY_READ
            | BluetoothGattCharacteristic.PROPERTY_WRITE
            | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE;
    private int permissions = BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getProperties() {
        return properties;
    }

    public void setProperties(int properties) {
        this.properties = properties;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public CharacteristicConfig(UUID uuid){
        this.uuid = uuid;
    }

    public CharacteristicConfig(String uuid){
        this.uuid = UUID.fromString(uuid);
    }
}
