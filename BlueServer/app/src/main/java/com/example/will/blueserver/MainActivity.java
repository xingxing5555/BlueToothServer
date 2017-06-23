package com.example.will.blueserver;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.will.peripherallib.BlePeripheral;
import com.example.will.peripherallib.BlePeripheralCallback;
import com.example.will.peripherallib.CharacteristicConfig;
import com.example.will.peripherallib.ServiceConfig;
import com.example.will.peripherallib.common.LocalBluetoothCtrl;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.will.blueserver.IWashUUID.ser1Char1Uuid;
import static com.example.will.blueserver.IWashUUID.ser1Char2Uuid;
import static com.example.will.blueserver.IWashUUID.ser1Uuid;
import static com.example.will.blueserver.IWashUUID.ser2Uuid;
import static com.example.will.blueserver.IWashUUID.ser3Char1Uuid;
import static com.example.will.blueserver.IWashUUID.ser3Uuid;
import static com.example.will.blueserver.IWashUUID.ser4CharUuid;
import static com.example.will.blueserver.IWashUUID.ser4Uuid;
import static com.example.will.blueserver.IWashUUID.ser5Char1Uuid;
import static com.example.will.blueserver.IWashUUID.ser5Uuid;
import static com.example.will.blueserver.IWashUUID.ser6Char1Uuid;
import static com.example.will.blueserver.IWashUUID.ser6Char2Uuid;
import static com.example.will.blueserver.IWashUUID.ser6Char3Uuid;
import static com.example.will.blueserver.IWashUUID.ser6Char4Uuid;
import static com.example.will.blueserver.IWashUUID.ser6Char5Uuid;
import static com.example.will.blueserver.IWashUUID.ser6Uuid;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private Button sendButton;
    private TextView infoText,deviceText;
    private EditText sendEdit;
    private LocalBluetoothCtrl localBluetoothCtrl = LocalBluetoothCtrl.getInstance();
    private BlePeripheral blePeripheral = BlePeripheral.getInstance();

    private static final int RC_LOCATION_PERM = 100;

    private AdvertiseCallback advertiseCallback;
    private AdvertiseData advertiseData;
    private AdvertiseSettings advertiseSettings;
    private boolean hasInitBle = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    infoText.setText("");
                    if (blePeripheral.getConnectDevice() != null){
                        setTitle("连接到" + blePeripheral.getConnectDevice().getAddress());
                    }else{
                        setTitle("断开连接");
                    }
                    break;
                case 1:
//                    infoText.append((String)msg.obj + "\n");
                    break;
                case 2:
                    infoText.append((String)msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSendButton();
        initPermission();
//        initBluetooth();

    }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    private void initPermission(){
        String[] perms = { Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!
//            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
            Log.v("Permission","has");
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, "我们需要位置权限",
                    RC_LOCATION_PERM, perms);
        }
    }

    private void initView(){
        sendButton = (Button)findViewById(R.id.send_button);
        infoText = (TextView)findViewById(R.id.info_text);
        infoText.setMovementMethod(new ScrollingMovementMethod());
        deviceText = (TextView)findViewById(R.id.device_text);
        String macAddress = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
        deviceText.setText("qeebike_" + "123456");
        sendEdit = (EditText)findViewById(R.id.editText);

        localBluetoothCtrl.getBluetoothAdapter().setName("qeebike_" + "123456");

        Log.e("wyn", "name is " + localBluetoothCtrl.getBluetoothAdapter().getName());
    }

    private void initSendButton(){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strLine = sendEdit.getText().toString();
                if (blePeripheral.sendDataToRemoteDevice(ser1Uuid,ser1Char2Uuid,blePeripheral.getConnectDevice(),strLine.getBytes())){
                    infoText.append("消息发送成功\n");
                }else {
                    infoText.append("消息发送失败\n");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.enable_ble_item:
                localBluetoothCtrl.enableBle(false);
                return true;
            case R.id.disable_ble_item:
                localBluetoothCtrl.disableBle();
                return true;
            case R.id.start_advertise_item:
                if (localBluetoothCtrl.isBLEEnabled()){
                    if (!hasInitBle){
                        initBluetooth();
                        hasInitBle = true;
                    }
                    localBluetoothCtrl.getBluetoothAdapter().setName("qeebike_" + "123456");

                    Log.e("wyn", "222 name is " + localBluetoothCtrl.getBluetoothAdapter().getName());

                    blePeripheral.startAdvertising(advertiseSettings, advertiseData, advertiseCallback);
                }else{
                    localBluetoothCtrl.enableBle(true);
                }
                return true;
            case R.id.stop_advertise_item:
                blePeripheral.stopAdvertising(advertiseCallback);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("main", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private void initBluetooth(){
        blePeripheral.addServices(new ServiceConfig(ser1Uuid), new CharacteristicConfig[]{new CharacteristicConfig(ser1Char1Uuid),new CharacteristicConfig(ser1Char2Uuid)});
        blePeripheral.addServices(new ServiceConfig(ser2Uuid), null);
        blePeripheral.addServices(new ServiceConfig(ser3Uuid), new CharacteristicConfig[]{new CharacteristicConfig(ser3Char1Uuid)});
        blePeripheral.addServices(new ServiceConfig(ser4Uuid), new CharacteristicConfig[]{new CharacteristicConfig(ser4CharUuid)});
        blePeripheral.addServices(new ServiceConfig(ser5Uuid), new CharacteristicConfig[]{new CharacteristicConfig(ser5Char1Uuid)});
        blePeripheral.addServices(new ServiceConfig(ser6Uuid), new CharacteristicConfig[]{
                new CharacteristicConfig(ser6Char1Uuid),
                new CharacteristicConfig(ser6Char2Uuid),
                new CharacteristicConfig(ser6Char3Uuid),
                new CharacteristicConfig(ser6Char4Uuid),
                new CharacteristicConfig(ser6Char5Uuid)
        });

        blePeripheral.setBlePeripheralCallback(new BlePeripheralCallback() {
            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                if (newState == BluetoothProfile.STATE_CONNECTED){
                    Log.v("设备" + device.getAddress(), "连接成功");
                    Message m0 = new Message();
                    m0.what = 0;
                    handler.sendMessage(m0);
                }else{
                    Log.v("设备" + device.getAddress(), "断开连接");
                }
            }

            @Override
            public void onServiceAdded(int status, BluetoothGattService service) {
                Log.v("设备","添加服务");
            }

            @Override
            public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
                Log.v("接收请求","读characteristic");
            }

            @Override
            public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                Log.v("接收请求","写characteristic");
                String strLine = new String(value);

                Log.e("wyn", "strLine is " + strLine);

                strLine = device.getAddress() + strLine + "\n";

                Message m = new Message();
                m.what = 2;
                m.obj = strLine;
                handler.sendMessage(m);
            }

            @Override
            public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
                Log.v("接收请求","读Descriptor");
            }

            @Override
            public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                Log.v("接收请求","写Descriptor");
            }

            @Override
            public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {

            }

            @Override
            public void onNotificationSent(BluetoothDevice device, int status) {

            }

            @Override
            public void onMtuChanged(BluetoothDevice device, int mtu) {

            }
        });
        advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.v("广播","成功");
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.v("广播失败","参数：" + errorCode);
            }
        };

        advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(new ParcelUuid(ser6Uuid))
                .build();
        advertiseSettings = new AdvertiseSettings.Builder()
                .setConnectable(true)
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTimeout(0)
                .build();

    }

}
