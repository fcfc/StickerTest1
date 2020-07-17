package com.ice.stickertest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

//  BluetoothPairActivity
//  Search for pairable Bluetooth devices
//  Return found pairable devices in list
//  Allow connection to any of found devices

public class BluetoothPairActivity extends Activity {
    private TextView mStatus;
    private Button mPairButton;
    private Button mScanButton;

    private ProgressDialog mProgressDialog;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mStatus = (TextView) findViewById(R.id.tv_status);
        mPairButton = (Button) findViewById(R.id.btn_view_paired);
        mScanButton = (Button) findViewById(R.id.btn_scan);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Scanning...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                mBluetoothAdapter.cancelDiscovery();
            }
        });

        // Bluetooth found
        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            mPairButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                    if (pairedDevices == null || pairedDevices.size() == 0) {
                        showToast("No paired devices found");
                    } else {
                        ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
                        list.addAll(pairedDevices);
                        Intent intent = new Intent(BluetoothPairActivity.this, BluetoothDeviceListActivity.class);
                        intent.putParcelableArrayListExtra("device,list", list);
                        startActivity(intent);
                    }
                }
            });


            mScanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBluetoothAdapter.startDiscovery();
                }
            });


            if (mBluetoothAdapter.isEnabled()) {
                showEnabled();
            } else {
                showDisabled();
            }
        }

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
    }


    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void showEnabled() {
        mStatus.setText("Bluetooth is on");
        mStatus.setTextColor(Color.BLUE);

        mPairButton.setEnabled(true);
        mScanButton.setEnabled(true);
        ;
    }


    private void showDisabled() {
        mStatus.setText("Bluetooth is Off");
        mStatus.setTextColor(Color.RED);

        mPairButton.setEnabled(false);
        mScanButton.setEnabled(false);
        ;
    }

    private void showUnsupported() {
        mStatus.setText("Bluetooth is unsupported by this device");

        mPairButton.setEnabled(false);
        mScanButton.setEnabled(false);
        ;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");
                    showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();
                mProgressDialog.show();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDialog.dismiss();
                Intent newIntent = new Intent(BluetoothPairActivity.this, BluetoothDeviceListActivity.class);
                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
                startActivity(newIntent);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mDeviceList.add(device);
                showToast("Found device " + device.getName());
            }
        }
    };
}
