package com.example.will.peripherallib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;

import com.example.will.peripherallib.common.ContextTool;
import com.example.will.peripherallib.common.LocalBluetoothCtrl;

import java.util.UUID;

/**
 * @author will4906.
 * @Time 2017/3/9.
 */

public class BlePeripheral {

    private LocalBluetoothCtrl localBluetoothCtrl = LocalBluetoothCtrl.getInstance();

    private static class BlePeripheralHolder {
        private static final BlePeripheral INSTANCE = new BlePeripheral();
    }

    public static final BlePeripheral getInstance() {
        return BlePeripheralHolder.INSTANCE;
    }

    private BlePeripheral (){
        if (localBluetoothCtrl.isBLESupported()){
            BluetoothManager bluetoothManager = localBluetoothCtrl.getBluetoothManager();
            BluetoothAdapter bluetoothAdapter = localBluetoothCtrl.getBluetoothAdapter();
            bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
            bluetoothGattServer = bluetoothManager.openGattServer(context, bluetoothGattServerCallback);
        }
    }

    private Context context = ContextTool.getContext();
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private BluetoothGattServer bluetoothGattServer;
    private BluetoothDevice connectDevice = null;
    private BlePeripheralCallback blePeripheralCallback = new BlePeripheralCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {}

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {}

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {}

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {}

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {}

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {}

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {}

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {}

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {}
    };

    public BlePeripheralCallback getBlePeripheralCallback() {
        return blePeripheralCallback;
    }

    public void setBlePeripheralCallback(BlePeripheralCallback blePeripheralCallback) {
        this.blePeripheralCallback = blePeripheralCallback;
    }

    public BluetoothDevice getConnectDevice() {
        return connectDevice;
    }

    private BluetoothGattServerCallback bluetoothGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED){
                connectDevice = device;
            }else{
                connectDevice = null;
            }
            blePeripheralCallback.onConnectionStateChange(device,status,newState);
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            blePeripheralCallback.onServiceAdded(status,service);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            blePeripheralCallback.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            blePeripheralCallback.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            blePeripheralCallback.onDescriptorReadRequest(device, requestId, offset, descriptor);
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            blePeripheralCallback.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, null);
        }

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
            blePeripheralCallback.onExecuteWrite(device, requestId, execute);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            blePeripheralCallback.onNotificationSent(device, status);
        }

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {
            blePeripheralCallback.onMtuChanged(device, mtu);
        }
    };

    public BluetoothLeAdvertiser getBluetoothLeAdvertiser() {
        if (bluetoothLeAdvertiser == null){
            if (localBluetoothCtrl.isBLESupported()){
                BluetoothAdapter bluetoothAdapter = localBluetoothCtrl.getBluetoothAdapter();
                bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
            }
        }
        return bluetoothLeAdvertiser;
    }

    public BluetoothGattServer getBluetoothGattServer() {
        if (bluetoothGattServer == null){
            if (localBluetoothCtrl.isBLESupported()) {
                BluetoothManager bluetoothManager = localBluetoothCtrl.getBluetoothManager();
                bluetoothGattServer = bluetoothManager.openGattServer(context, bluetoothGattServerCallback);
            }
        }
        return bluetoothGattServer;
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseCallback callback){
        bluetoothLeAdvertiser = getBluetoothLeAdvertiser();
        if (bluetoothLeAdvertiser != null && settings != null && advertiseData != null && callback != null){
            getBluetoothLeAdvertiser().startAdvertising(settings, advertiseData, callback);
        }
    }

    public void startAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseCallback callback){
        bluetoothLeAdvertiser = getBluetoothLeAdvertiser();
        if (bluetoothLeAdvertiser != null && settings != null && advertiseData != null && scanResponse != null && callback != null){
            getBluetoothLeAdvertiser().startAdvertising(settings, advertiseData, scanResponse, callback);
        }
    }

    public void stopAdvertising(AdvertiseCallback callback){
        bluetoothLeAdvertiser = getBluetoothLeAdvertiser();
        if (bluetoothLeAdvertiser != null && callback != null){
            getBluetoothLeAdvertiser().stopAdvertising(callback);
        }
    }

    public BluetoothGattService addServices(ServiceConfig serviceConfig, CharacteristicConfig[] characteristicConfigs){
        if (serviceConfig == null){
            return null;
        }
        BluetoothGattService service = new BluetoothGattService(serviceConfig.getUuid(),serviceConfig.getServiceType());
        if (characteristicConfigs != null){
            for (CharacteristicConfig characteristicConfig : characteristicConfigs){
                BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(characteristicConfig.getUuid(),characteristicConfig.getProperties(),characteristicConfig.getPermissions());
                service.addCharacteristic(characteristic);
            }
        }
        getBluetoothGattServer().addService(service);
        return service;
    }

    public boolean sendDataToRemoteDevice(UUID serviceUuid, UUID characteristicUuid, BluetoothDevice bluetoothDevice, byte[] data){
        bluetoothGattServer = getBluetoothGattServer();
        if (bluetoothGattServer != null){
            if (bluetoothDevice != null){
                if (localBluetoothCtrl.getBluetoothManager().getConnectionState(bluetoothDevice,BluetoothProfile.GATT_SERVER) == BluetoothProfile.STATE_CONNECTED){
                    BluetoothGattService service = bluetoothGattServer.getService(serviceUuid);
                    if (service != null){
                        BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
                        if (characteristic != null){
                            characteristic.setValue(data);
                            return bluetoothGattServer.notifyCharacteristicChanged(bluetoothDevice, characteristic, false);
                        }
                    }
                }
            }
        }
        return false;
    }
}
