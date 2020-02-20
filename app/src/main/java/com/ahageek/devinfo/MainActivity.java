package com.ahageek.devinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText gsf = findViewById(R.id.gsf);
        gsf.setText(getGSF());
        EditText androidid = findViewById(R.id.android_id);
        androidid.setText(getAndroidID());
    }

    String getAndroidID() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    String getDeviceID() {
        // TODO: Request permission dynamically
        String imei = "UNKNOWNN";
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = tm.getImei();
            } else {
                imei = tm.getDeviceId();
            }
        }
        return imei;
    }


    String getGSF() {
        Cursor query = getContentResolver().query(Uri.parse("content://com.google.android.gsf.gservices"), null, null, new String[] { "android_id" }, null);
        if (query == null) return "UNKNOWN";
        if (!query.moveToFirst() || query.getColumnCount() < 2) {
            query.close();
            return "NOT FOUND";
        }
        final String gsf = Long.toHexString(Long.parseLong(query.getString(1)));
        query.close();
        return gsf;
    }
}
