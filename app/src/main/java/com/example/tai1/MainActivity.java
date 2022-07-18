package com.example.tai1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView temperaturebarm;
    private TextView humiditybarm;
    private TextView lightbarm;
    private RangeBar temperatureRangeBar;
    private TextView temperatureRangeTextView, airConditionerTemperature, barnTemperatureText;
    private ImageView temperatureWarning, humidityWarning, brightnessWarning, airConditionerImage, decreaseTemperature, increaseTemperature, fanImage, lightImage;
    private Button airConditionerPowerButton, fanOn, fanOff;
    private Switch lightSwitch;
    private Integer airConditionerTemperatureValue;
    private RecyclerView pigList;
    private PigAdapter pigAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mappingLayout();

        setupPigListView();

        humidityWarning.setVisibility(View.INVISIBLE);
        brightnessWarning.setVisibility(View.INVISIBLE);

        setupTemperatureRageBar();

        compareTemperature();

        try {
            airConditionerTemperatureValue = Integer.parseInt(airConditionerTemperature.getText().toString());
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse" + nfe);
        }

        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef_temp = database.getReference("Farm/Barn1/Sensor/Temperature");
        myRef_temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue().toString();
                    temperaturebarm.setText(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                temperaturebarm.setText("25");
            }
        });

        DatabaseReference myRef_humi = database.getReference("Farm/Barn1/Sensor/Humidity");
        myRef_humi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue().toString();
                    humiditybarm.setText(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                humiditybarm.setText("50");
            }
        });

        DatabaseReference myRef_light = database.getReference("Farm/Barn1/Sensor/Light");
        myRef_light.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.getValue().toString();
                    lightbarm.setText(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lightbarm.setText("50");
            }
        });

        temperatureRangeTextView.setText(temperatureRangeBar.getLeftIndex() + " - " + temperatureRangeBar.getRightIndex());

        temperatureRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
                temperatureRangeTextView.setText(temperatureRangeBar.getLeftIndex() + " - " + temperatureRangeBar.getRightIndex());
                compareTemperature();
            }
        });

        checkAirConditionerStatus();

        airConditionerPowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String power = airConditionerPowerButton.getText().toString();
                if (power.equals("OFF")) {
                    airConditionerImage.setImageResource(R.drawable.airconditioneron);
                    increaseTemperature.setVisibility(View.VISIBLE);
                    decreaseTemperature.setVisibility(View.VISIBLE);
                    airConditionerTemperature.setVisibility(View.VISIBLE);
                    airConditionerPowerButton.setText("ON");
                    airConditionerPowerButton.setBackgroundColor(Color.parseColor("#60E65B"));
                } else {
                    airConditionerImage.setImageResource(R.drawable.airconditioner);
                    increaseTemperature.setVisibility(View.INVISIBLE);
                    decreaseTemperature.setVisibility(View.INVISIBLE);
                    airConditionerTemperature.setVisibility(View.INVISIBLE);
                    airConditionerPowerButton.setText("OFF");
                    airConditionerPowerButton.setBackgroundColor(Color.parseColor("#E65B5B"));
                }
            }
        });

        decreaseTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (airConditionerTemperatureValue > 20) {
                    airConditionerTemperatureValue--;
                    airConditionerTemperature.setText(Integer.toString(airConditionerTemperatureValue));
                }
            }
        });

        increaseTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (airConditionerTemperatureValue < 32) {
                    airConditionerTemperatureValue++;
                    airConditionerTemperature.setText(Integer.toString(airConditionerTemperatureValue));
                }
            }
        });

        fanOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Farm/Barn1/Device/Fan");
                myRef.setValue("ON");

                fanImage.setImageResource(R.drawable.fanon);
                fanOn.setBackgroundColor(Color.parseColor("#60E65B"));
                fanOn.setTextColor(Color.parseColor("#FFFFFF"));
                fanOff.setBackgroundColor(Color.parseColor("#FFFFFF"));
                fanOff.setTextColor(Color.parseColor("#000000"));
            }
        });

        fanOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Farm/Barn1/Device/Fan");
                myRef.setValue("OFF");

                fanImage.setImageResource(R.drawable.fanoff);
                fanOff.setBackgroundColor(Color.parseColor("#E65B5B"));
                fanOff.setTextColor(Color.parseColor("#FFFFFF"));
                fanOn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                fanOn.setTextColor(Color.parseColor("#000000"));
            }
        });

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    lightImage.setImageResource(R.drawable.lighton);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Farm/Barn1/Device/Lamp");
                    myRef.setValue("ON");
                } else {
                    lightImage.setImageResource(R.drawable.lightoff);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Farm/Barn1/Device/Lamp");
                    myRef.setValue("OFF");
                }
            }
        });

    }

    private void mappingLayout() {
        temperaturebarm = findViewById(R.id.temperature_value);
        humiditybarm = findViewById(R.id.humidity_value);
        lightbarm = findViewById(R.id.brightness_value);
        temperatureWarning = findViewById(R.id.temperature_warning);
        humidityWarning = findViewById(R.id.humidity_warning);
        brightnessWarning = findViewById(R.id.brightness_warning);
        temperatureRangeBar = findViewById(R.id.temperature_range);
        temperatureRangeTextView = findViewById(R.id.temperature_range_value);
        airConditionerImage = findViewById(R.id.air_conditioner_image);
        airConditionerPowerButton = findViewById(R.id.air_conditioner_button);
        decreaseTemperature = findViewById(R.id.decrease_button);
        increaseTemperature = findViewById(R.id.increase_button);
        airConditionerTemperature = findViewById(R.id.ac_temp);
        barnTemperatureText = findViewById(R.id.temperature_value);
        fanOn = findViewById(R.id.fan_on);
        fanOff = findViewById(R.id.fan_off);
        fanImage = findViewById(R.id.fan_image);
        lightSwitch = findViewById(R.id.light_switch);
        lightImage = findViewById(R.id.light_image);
        pigList = findViewById(R.id.pig_list);

    }

    private void setupPigListView() {
        pigAdapter = new PigAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        pigList.setLayoutManager(linearLayoutManager);

        pigAdapter.setData(getListPigProfile());
        pigList.setAdapter(pigAdapter);
    }

    private List<Pig> getListPigProfile() {
        List<Pig> list = new ArrayList<>();
        list.add(new Pig(R.drawable.pig, "ID1", "5", "Japan", "54"));
        list.add(new Pig(R.drawable.pig, "ID2", "12", "US", "15"));
        list.add(new Pig(R.drawable.pig, "ID3", "6", "UK", "56"));
        return list;
    }

    private void setupTemperatureRageBar() {
        temperatureRangeBar.setTickCount(101);
        temperatureRangeBar.setTickHeight(0);
        temperatureRangeBar.setBarWeight(5);
        temperatureRangeBar.setConnectingLineColor(Color.parseColor("#FA4F00"));
        temperatureRangeBar.setConnectingLineWeight(5);
        temperatureRangeBar.setThumbColorNormal(Color.parseColor("#FA4F00"));
        temperatureRangeBar.setThumbRadius(7);
    }

    private void compareTemperature() {
//        Integer barnTemperature = Integer.parseInt(barnTemperatureText.getText().toString());
//        if (barnTemperature > (20+temperatureRangeBar.getLeftIndex()) && barnTemperature < (20+temperatureRangeBar.getRightIndex())) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef_temp = database.getReference("Farm/Barn1/Sensor/Temperature");
        myRef_temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer data = Integer.parseInt(snapshot.getValue().toString());
                    if ((temperatureRangeBar.getLeftIndex() < data) && (data < temperatureRangeBar.getRightIndex())) {
                        temperatureWarning.setVisibility(View.INVISIBLE);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Farm/Barn1/WARNING");
                        myRef.setValue("OFF");

                    } else {
                        temperatureWarning.setVisibility(View.VISIBLE);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Farm/Barn1/WARNING");
                        myRef.setValue("ON");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                temperatureWarning.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void checkAirConditionerStatus(){
        String power = airConditionerPowerButton.getText().toString();
        if (power.equals("OFF")) {
            airConditionerImage.setImageResource(R.drawable.airconditioneron);
            increaseTemperature.setVisibility(View.VISIBLE);
            decreaseTemperature.setVisibility(View.VISIBLE);
            airConditionerTemperature.setVisibility(View.VISIBLE);
            airConditionerPowerButton.setText("ON");
            airConditionerPowerButton.setBackgroundColor(Color.parseColor("#60E65B"));
        } else {
            airConditionerImage.setImageResource(R.drawable.airconditioner);
            increaseTemperature.setVisibility(View.INVISIBLE);
            decreaseTemperature.setVisibility(View.INVISIBLE);
            airConditionerTemperature.setVisibility(View.INVISIBLE);
            airConditionerPowerButton.setText("OFF");
            airConditionerPowerButton.setBackgroundColor(Color.parseColor("#E65B5B"));
        }
    }
}