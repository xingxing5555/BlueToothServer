package com.example.will.peripherallib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

/**
 * @author will4906.
 * @Time 2017/3/9.
 */

public interface BlePeripheralCallback {

    void onConnectionStateChange(BluetoothDevice device, int status, int newState);

    void onServiceAdded(int status, BluetoothGattService service);

    void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic);

    void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value);
    
    void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor);

    void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value);

    void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute);
    
    void onNotificationSent(BluetoothDevice device, int status);

    void onMtuChanged(BluetoothDevice device, int mtu);
}
