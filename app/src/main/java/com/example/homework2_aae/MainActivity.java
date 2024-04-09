package com.example.homework2_aae;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private ImageView buttonMenu;
    private LinearLayout buttons;
    private boolean buttonsCheck = false;
    private ImageView buttonPalette, buttonClear;
    private ArtView art;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonMenu = findViewById(R.id.buttonMenu);
        buttons = findViewById(R.id.buttons);
        buttonPalette = findViewById(R.id.buttonPalette);
        buttonClear = findViewById(R.id.buttonClear);
        art = findViewById(R.id.art);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        buttonMenu.setOnClickListener(listener);
        buttonPalette.setOnClickListener(listener);
        buttonClear.setOnClickListener(listener);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Sensor multiSensor = sensorEvent.sensor;
            if (multiSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float xAccelerometer = sensorEvent.values[0];
                float yAccelerometer = sensorEvent.values[1];
                float zAccelerometer = sensorEvent.values[2];
                float medianAccelerometer = (xAccelerometer + yAccelerometer + zAccelerometer) / 3;
                if (medianAccelerometer > 10) {
                    if (buttonsCheck) {
                        buttonsCheck = false;
                        buttons.setVisibility(View.INVISIBLE);
                    } else {
                        buttonsCheck = true;
                        buttons.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int id = view.getId();
            if (id == R.id.buttonMenu) {
                if (buttonsCheck) {
                    buttonsCheck = false;
                    buttons.setVisibility(View.INVISIBLE);
                } else {
                    buttonsCheck = true;
                    buttons.setVisibility(View.VISIBLE);
                }
            } else if (id == R.id.buttonClear) {
                AlertDialog.Builder broomDialog = new AlertDialog.Builder(MainActivity.this);
                broomDialog.setTitle("Очистка рисунка");
                broomDialog.setMessage("Очистить область рисования (имеющийся рисунок будет удалён)?");

                broomDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        art.clear();
                        dialog.dismiss();
                    }
                });
                broomDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                broomDialog.show();
            }
        }
    };
}