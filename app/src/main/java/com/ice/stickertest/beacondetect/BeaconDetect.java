package com.ice.stickertest.beacondetect;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.ice.stickertest.R;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;


// Detect Bluetooth LE beacons.
// Get instance of Android bluetooth adapter
// Get handle to Bluetooth LE Scanner
// pass Scan Filter and Scan settings to scanner
// start scan
// Use callback to handle results of scan

public class BeaconDetect extends Activity {
    BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    ScanFilter    mScanFilter;
    ScanSettings  mScanSettings;
    // data to add to scan filter to selectively look for beacons
    int MANUFACTURER_ID = 224;
    String MANUFACTURER_DATA = "0000000000000000";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_detector);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.startScan(Arrays.asList(mScanFilter), mScanSettings, mScanCallback);
    }


    /*
     *   Scan callback object - 
     */
    protected ScanCallback mScanCallback = new ScanCallback() {
	@Override
	public void onBatchScanResults(List<ScanResult> results) {
	    super.onBatchScanResults(results);

	//    for (ScanResult.result : results) {
	//	mAdapter.add(result);
	//    }
	//    mAdapter.notifyDataSetChanged();
	}

	    
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            ScanRecord mScanRecord = result.getScanRecord();
            byte[] manufacturerData = mScanRecord.getManufacturerSpecificData(MANUFACTURER_ID);
            int mRssi = result.getRssi();
            int mTxPower       = mScanRecord.getTxPowerLevel();
            int advertiseFlags = mScanRecord.getAdvertiseFlags();
            String deviceName  = mScanRecord.getDeviceName();
            long timestamp     = result.getTimestampNanos();
        }

    };

    /*
     *   Scan Filter
     */
    // Scan filter can filter on
    // 1. Service UUIDs
    // 2. Remote device name
    // 3. Mac address of remote device
    // 4. Service data
    // 5. Manufacturer specific


    private void setScanFilter() {
        ScanFilter.Builder mBuilder = new ScanFilter.Builder();
        ByteBuffer mManufacturerData     = ByteBuffer.allocate(23);
        ByteBuffer mManufacturerDataMask = ByteBuffer.allocate(24);

//        mBuilder.setManufacturerData(MANUFACTURER_ID, mManufacturerData.array(), mManufacturerDataMask.array());
/*        mBuilder.setDeviceAddress ( deviceAddress );          // Filter on device address
          mBuilder.setDeviceName ( deviceName );                // Filter on device name
          mBuilder.setServiceData ( mServiceData.array(), mServiceDataMask.array() );
          mBuilder.setServiceSolicitationUuid ( ParcelUUID(uuid) );
          mBuilder.setServiceUuid ( ParcelUUID(uuid) );
 */

        mScanFilter = mBuilder.build();
    }

    private void setScanSettings() {
        ScanSettings.Builder mBuilder = new ScanSettings.Builder();

        mBuilder.setReportDelay(0);
        mBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        mScanSettings = mBuilder.build();
    }

    // Calculate distance to beacon
    public double calculateDistance(int txPower, double rssi)  {
        if (rssi == 0) {
            return -1.0;        // cannot determine accuracy
        }
        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double accuracy = (0.89976)* Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

    private String getDistance(double accuracy) {
        if (accuracy == -1.0) {
            return "Unknown";
        } else if (accuracy < 1) {
            return "Immediate";
        } else if (accuracy < 3) {
            return "Near";
        } else {
            return "Far";
        }
    }
}
