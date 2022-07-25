package com.example.tai1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
    private List<Pig> mListPigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mappingLayout();

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
        Pctthreshold();
        temperatureRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
                temperatureRangeTextView.setText(temperatureRangeBar.getLeftIndex() + " - " + temperatureRangeBar.getRightIndex());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef_up = database.getReference("Farm/Barn1/Threshold/Temperature/UP");
                myRef_up.setValue(Integer.toString(temperatureRangeBar.getRightIndex()));
                DatabaseReference myRef_dw = database.getReference("Farm/Barn1/Threshold/Temperature/DW");
                myRef_dw.setValue(Integer.toString(temperatureRangeBar.getLeftIndex()));
                compareTemperature();
            }
        });

        compareTemperature();

        try {
            airConditionerTemperatureValue = Integer.parseInt(airConditionerTemperature.getText().toString());
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse" + nfe);
        }

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

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Farm/Barn1/Device/Air-Conditioner/Status-AC");
                    myRef.setValue("ON");
                } else {
                    airConditionerImage.setImageResource(R.drawable.airconditioner);
                    increaseTemperature.setVisibility(View.INVISIBLE);
                    decreaseTemperature.setVisibility(View.INVISIBLE);
                    airConditionerTemperature.setVisibility(View.INVISIBLE);
                    airConditionerPowerButton.setText("OFF");
                    airConditionerPowerButton.setBackgroundColor(Color.parseColor("#E65B5B"));

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Farm/Barn1/Device/Air-Conditioner/Status-AC");
                    myRef.setValue("OFF");
                }
            }
        });

        decreaseTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (airConditionerTemperatureValue > 20) {
                    airConditionerTemperatureValue--;
                    Integer temp_airConditionerTemperatureValue = airConditionerTemperatureValue;
                    airConditionerTemperature.setText(Integer.toString(airConditionerTemperatureValue));

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Farm/Barn1/Device/Air-Conditioner/Temperature-AC");
                    myRef.setValue(temp_airConditionerTemperatureValue);
                }
            }
        });

        increaseTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (airConditionerTemperatureValue < 32) {
                    airConditionerTemperatureValue++;
                    Integer temp_airConditionerTemperatureValue = airConditionerTemperatureValue;
                    airConditionerTemperature.setText(Integer.toString(airConditionerTemperatureValue));

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Farm/Barn1/Device/Air-Conditioner/Temperature-AC");
                    myRef.setValue(temp_airConditionerTemperatureValue);
                }
            }
        });

        DatabaseReference myRef_fan = database.getReference("Farm/Barn1/Device/Fan");
        myRef_fan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data_fan = snapshot.getValue().toString();
                    if (data_fan.equals("ON")){
                        fanImage.setImageResource(R.drawable.fanon);
                        fanOn.setBackgroundColor(Color.parseColor("#60E65B"));
                        fanOn.setTextColor(Color.parseColor("#FFFFFF"));
                        fanOff.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        fanOff.setTextColor(Color.parseColor("#000000"));
                    }
                    else {
                        fanImage.setImageResource(R.drawable.fanoff);
                        fanOff.setBackgroundColor(Color.parseColor("#E65B5B"));
                        fanOff.setTextColor(Color.parseColor("#FFFFFF"));
                        fanOn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        fanOn.setTextColor(Color.parseColor("#000000"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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

        DatabaseReference myRef_lamp = database.getReference("Farm/Barn1/Device/Lamp");
        myRef_lamp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data_lamp = snapshot.getValue().toString();
                    if (data_lamp.equals("ON")){
                        lightImage.setImageResource(R.drawable.lighton);
                    }
                    else {
                        lightImage.setImageResource(R.drawable.lightoff);;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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

        setupPigListView();

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
//        pigAdapter = new PigAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        pigList.setLayoutManager(linearLayoutManager);

        mListPigs = new ArrayList<>();
        pigAdapter = new PigAdapter(mListPigs);
        pigList.setAdapter(pigAdapter);
        getListPigProfile();
//        pigList.setAdapter(pigAdapter);
    }

    private void getListPigProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Farm/Barn1/PET");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListPigs.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Pig pig = dataSnapshot.getValue(Pig.class);
                    mListPigs.add(pig);
                }
                pigAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                 if call Firebase failed then hard code for info pet
//                List<Pig> list = new ArrayList<>();
                mListPigs.add(new Pig("ID1", "5", "Japan", "54"));
                mListPigs.add(new Pig( "ID2", "12", "US", "15"));
                mListPigs.add(new Pig("ID3", "6", "UK", "56"));
                pigAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupTemperatureRageBar() {
        Pctthreshold();
        temperatureRangeBar.setTickCount(101);
        temperatureRangeBar.setTickHeight(0);
        temperatureRangeBar.setBarWeight(5);
        temperatureRangeBar.setConnectingLineColor(Color.parseColor("#FA4F00"));
        temperatureRangeBar.setConnectingLineWeight(5);
        temperatureRangeBar.setThumbColorNormal(Color.parseColor("#FA4F00"));
        temperatureRangeBar.setThumbRadius(7);
    }

    private void Pctthreshold() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef_temp = database.getReference("Farm/Barn1/Threshold/Temperature/UP");
        myRef_temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data_up = snapshot.getValue().toString();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef_temp = database.getReference("Farm/Barn1/Threshold/Temperature/DW");
                    myRef_temp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String data_dw = snapshot.getValue().toString();
                                temperatureRangeTextView.setText(data_dw + " - " + data_up);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
//        String power = airConditionerPowerButton.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef_fan = database.getReference("Farm/Barn1/Device/Air-Conditioner/Status-AC");
        myRef_fan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data_ac = snapshot.getValue().toString();
                    if (data_ac.equals("ON")){
                        airConditionerImage.setImageResource(R.drawable.airconditioneron);
                        increaseTemperature.setVisibility(View.VISIBLE);
                        decreaseTemperature.setVisibility(View.VISIBLE);
                        airConditionerTemperature.setVisibility(View.VISIBLE);
                        airConditionerPowerButton.setText("ON");
                        airConditionerPowerButton.setBackgroundColor(Color.parseColor("#60E65B"));
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef_fan = database.getReference("Farm/Barn1/Device/Air-Conditioner/Temperature-AC");
                        myRef_fan.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String temp_ac = snapshot.getValue().toString();
                                    airConditionerTemperature.setText(temp_ac);
                                    airConditionerTemperatureValue = Integer.parseInt(temp_ac);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                    else {
                        airConditionerImage.setImageResource(R.drawable.airconditioner);
                        increaseTemperature.setVisibility(View.INVISIBLE);
                        decreaseTemperature.setVisibility(View.INVISIBLE);
                        airConditionerTemperature.setVisibility(View.INVISIBLE);
                        airConditionerPowerButton.setText("OFF");
                        airConditionerPowerButton.setBackgroundColor(Color.parseColor("#E65B5B"));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}