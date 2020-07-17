package com.ice.stickertest;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.stickertest.beacondetect.BeaconDetect;
import com.ice.stickertest.beacondetect.BleAdvertiseScanFragment;
import com.ice.stickertest.datalogging.FileIOActivity;
import com.ice.stickertest.datalogging.GraphTest;
import com.ice.stickertest.nfc.NfcHandler;

public class MainActivity extends Activity {

    Button mNfcButton;
    Button mBlueButton;
    Button mBeaconButton;
    Button mFileButton;
    Button mGraphButton;

    private BluetoothAdapter mBluetoothAdapter;

    public static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

            // Is Bluetooth supported on this device?
            if (mBluetoothAdapter != null) {
                // Is Bluetooth turned on?
                if (mBluetoothAdapter.isEnabled()) {
                    // Are Bluetooth Advertisements supported on this device?
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
;
                    } else {
                        // Bluetooth Advertisements are not supported.
                        showErrorText(R.string.bt_ads_not_supported);
                    }
                } else {
                    // Prompt user to turn on Bluetooth).
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            } else {

                // Bluetooth is not supported.
                showErrorText(R.string.bt_not_supported);
            }

            mNfcButton = (Button) findViewById(R.id.nfc_button);
            mBlueButton = (Button) findViewById(R.id.blue_button);
            mBeaconButton = (Button) findViewById(R.id.beacon_button);
            mFileButton = (Button) findViewById(R.id.file_button);
            mGraphButton = (Button) findViewById(R.id.graph_button);

            mNfcButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, NfcHandler.class);
                    startActivity(i);
                }
            });

            mBlueButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, BluetoothPairActivity.class);
                    startActivity(i);
                }
            });

            mBeaconButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, BeaconDetect.class);
                    startActivity(i);
                }
            });
            mFileButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, FileIOActivity.class);
                    startActivity(i);
                }
            });
            mGraphButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, GraphTest.class);
                    startActivity(i);
                }
            });
        }
    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case REQUEST_ENABLE_BT:

                    if (resultCode == RESULT_OK) {

                        // Bluetooth is now Enabled, are Bluetooth Advertisements supported on
                        // this device?
                        if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
                            // Everything is supported and enabled, load the fragments.
                           // setupBeaconFragments();
                        } else {
                            // Bluetooth Advertisements are not supported.
                            showErrorText(R.string.bt_ads_not_supported);
                        }
                    } else {
                        // User declined to enable Bluetooth, exit the app.
                        Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }


        private void showErrorText(int messageId) {

            TextView view = (TextView) findViewById(R.id.error_textview);
            view.setText(getString(messageId));
        }
    }

