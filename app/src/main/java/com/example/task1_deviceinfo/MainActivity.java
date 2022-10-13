package com.example.task1_deviceinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int deviceStatus;

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int batteryLevel = -1;

            if (Build.VERSION.SDK_INT >= 21) {
                BatteryManager batteryManager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
                batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                toastNotification(batteryLevel);
            } else {
                deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                batteryLevel = (int) (((float) level / (float) scale) * 100.0f);
                toastNotification(batteryLevel);
            }
        }
    };

    private void toastNotification(int batteryLevel) {
        if (batteryLevel >= 5 && batteryLevel <= 15) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.currentBatteryStatus) + " = Низкий уровень заряда\nбатареи(" + batteryLevel +
                    " %)!", Toast.LENGTH_SHORT).show();
        } else if (batteryLevel < 5 && batteryLevel >= 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.currentBatteryStatus) + " = Критический низкий уровен\nзаряда батареи( " +
                    batteryLevel + " %!)", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.currentBatteryStatus) + " = Уровень заряда батарии: " +
                    batteryLevel + " %", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //слой activity
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setColumnStretchable(2, true);

        TableRow rowHeaderTable = new TableRow(this);
        TextView Label = new TextView(this);
        Label.setText("Info Table"); ///конечно, это нужно убрать в ресурс-файл
        Label.setTypeface(Typeface.SERIF);
        rowHeaderTable.addView(Label, new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        tableLayout.addView(rowHeaderTable);

        //строка имя модели
        TableRow rowDeviceModel = new TableRow(this);
        TextView textDeviceModel = new TextView(this);
        textDeviceModel.setText("Device model: ");
        rowDeviceModel.addView(textDeviceModel, new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));

        TextView textDeviceModelDetail = new TextView(this);
        textDeviceModelDetail.setText(new StringBuilder().append(Build.BRAND).append(" ").append(Build.MODEL).toString());
        rowDeviceModel.addView(textDeviceModelDetail, new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));

        tableLayout.addView(rowDeviceModel);

        //строка версии
        TableRow rowOSVer = new TableRow(this);
        TextView textOSVer = new TextView(this);
        textOSVer.setText("OS version: ");
        rowOSVer.addView(textOSVer, new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));

        TextView textOSVerDetail = new TextView(this);
        textOSVerDetail.setText(Build.VERSION.RELEASE);
        rowOSVer.addView(textOSVerDetail, new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.f));
        tableLayout.addView(rowOSVer);


        //строка SDK
        TableRow rowSDKVer = new TableRow(this);
        TextView textSDKVer = new TextView(this);
        textSDKVer.setText("SDK version: ");
        rowSDKVer.addView(textSDKVer, new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));

        TextView textSDKVerDetail = new TextView(this);
        textSDKVerDetail.setText(Integer.toString(Build.VERSION.SDK_INT));
        rowSDKVer.addView(textSDKVerDetail, new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.f));

        tableLayout.addView(rowSDKVer);

        //строка UUID (?) устаревшее
        TableRow rowUUID = new TableRow(this);
        TextView textUUID = new TextView(this);
        textUUID.setText("UUID: ");
        rowUUID.addView(textUUID, new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));

        TextView textUUIDDetail = new TextView(this);
        textUUIDDetail.setText(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        rowUUID.addView(textUUIDDetail, new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.f));
        tableLayout.addView(rowUUID);

        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        this.registerReceiver(broadcastreceiver, intentfilter); //регистрация получения рассылки

        setContentView(tableLayout);

    }

}