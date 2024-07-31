package com.example.projectwork;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.google.android.gms.location.LocationCallback;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


// binding
import com.example.projectwork.databinding.ActivityCompasAcrivityBinding;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.LocationRequest;

public class CompasActivity extends AppCompatActivity implements SensorEventListener {
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                getLocation();
            }
        }
    });

    private SensorManager sensorManager;
    Sensor accelerometer, magnetometer, pressureSensor;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private float[] gravity;
    private float[] geomagnetic;
    private float currentDegree = 0f;
    private ActivityCompasAcrivityBinding binding;
    private LocationManager fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompasAcrivityBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(CompasActivity.this);

        if (ContextCompat.checkSelfPermission(CompasActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest.Builder(10000).build();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    currentLocation = locationResult.getLastLocation();
                    binding.latitudeTV.setText(MessageFormat.format("Lat: {0}", currentLocation.getLatitude()));
                    binding.longitudeTV.setText(MessageFormat.format("Long: {0}", currentLocation.getLongitude()));
                    getCityName(currentLocation);
                }
            }, Looper.getMainLooper()); // Provide a Looper (usually main looper)
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, magnetometer);
        sensorManager.unregisterListener(this, pressureSensor);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = sensorEvent.values;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = sensorEvent.values;
            float magneticStrength = calculateMagneticStrength(geomagnetic);
            binding.magneticStrengthTV.setText(MessageFormat.format("Magnetic strength: {0}", magneticStrength));
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE){
            float pressure = sensorEvent.values[0];
            binding.pressureTV.setText(MessageFormat.format("Pressure: {0} hpa", pressure));
            float altitude = calculateAltitude(pressure);
            binding.altitudeTV.setText(MessageFormat.format("Altitude: {0} meters/{1} feet", altitude, altitude * 3.28084));

        }
        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuthInRadians = orientation[0];
                float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);
                azimuthInDegrees = (azimuthInDegrees + 360) % 360;

                int degree = Math.round(azimuthInDegrees);

                float declination = 0;
                if (currentLocation != null) {
                    declination = getGeomagneticField(currentLocation).getDeclination();
                }
                int trueNorth = Math.round(degree + declination);

                RotateAnimation rotateAnimation = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(210);
                rotateAnimation.setFillAfter(true);

                binding.compassImageView.startAnimation(rotateAnimation);

                currentDegree = -degree;

                binding.headingTV.setText(MessageFormat.format("{0}", degree));
                binding.trueHeadingTV.setText(MessageFormat.format("True HDG: {0}", trueNorth));
                binding.directionTV.setText(getDirection(degree));
            }
        }
    }

    private String getDirection(float degree) {
        if (degree > 22.5 && degree < 67.5) {
            return "NE";
        } else if (degree >= 67.5 && degree < 112.5) {
            return "E";
        } else if (degree >= 112.5 && degree < 157.5) {
            return "ES";
        } else if (degree >= 157.5 && degree < 202.5) {
            return "S";
        } else if (degree >= 202.5 && degree < 247.5) {
            return "SW";
        } else if (degree >= 247.5 && degree < 292.5) {
            return "W";
        } else if (degree >= 292.5 && degree < 337.5) {
            return "WN";
        } else {
            return "N";
        }
    }

    private GeomagneticField getGeomagneticField(Location location) {
        return new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(), (float) location.getAltitude(), System.currentTimeMillis());
    }

    private float calculateMagneticStrength(float[] geomagnetic){
        return (float) Math.sqrt(geomagnetic[0] * geomagnetic[0] + geomagnetic[1] * geomagnetic[1] + geomagnetic[2] * geomagnetic[2]);
    }

    private void getCityName(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String cityName = addresses.get(0).getLocality();
                binding.cityTV.setText(MessageFormat.format("{0}", cityName));
            } else {
                binding.cityTV.setText("Couldn't find city");
            }
        } catch (Exception e) {
            binding.cityTV.setText("Couldn't find city");
            e.printStackTrace();
        }
    }

    private float calculateAltitude (float pressure) {
        return (float) ((1 - Math.pow(pressure / 1013.25f, 0.190284)) * 145366.45 * 0.3048);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}