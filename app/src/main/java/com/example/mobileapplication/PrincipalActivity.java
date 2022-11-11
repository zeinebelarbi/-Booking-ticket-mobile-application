package com.example.mobileapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawerLayout;
private NavigationView navigationView;
private ImageView menuIcon;
private LinearLayout contentView;
private EditText nameDevice,valueDevice;
private Button btnAddDevice;
private DatabaseReference reference;
private ListView listDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    drawerLayout=findViewById(R.id.drawer_layout_principal);
    navigationView=findViewById(R.id.navigation_view_principal);
    menuIcon=findViewById(R.id.menu_principal);
    contentView=findViewById(R.id.ll_principal);
    nameDevice=findViewById(R.id.etNameDevice);
    valueDevice=findViewById(R.id.etValueDevice);
    btnAddDevice=findViewById(R.id.btnAddDevice);
    listDevice=findViewById(R.id.listDevice);
    btnAddDevice.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String deviseName = nameDevice.getText().toString();
            String deviseValue = valueDevice.getText().toString();
            String deviceName = null;
            String deviceValue = null;
            if (deviceName.isEmpty()) {
                nameDevice.setError("Device name should not be empty");
            } else if (deviceValue.isEmpty()) {
                valueDevice.setError("Device name should not be empty");
            } else {
            addDevice(deviceName,deviceValue);
            }
        }
            });
    ArrayList<String> deviceArrayList = new arrayList();
    ArrayAdapter<String> adapter =new ArrayAdapter<>(PrincipalActivity.this,R.layout.list_item,deviceArrayList);
    listDevice.setAdapter(adapter);
    DatabaseReference deviceReference = FirebaseDatabase.getInstance().getReference().child("Devices");
    deviceReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            deviceArrayList.clear();
            for (DataSnapshot deviceSnapshot : snapshot.getChildren()) {
          Device device =deviceSnapshot.getValue(Device.class);
            deviceArrayList.add(device.getName()+" : "+device.getValue());

            }
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(PrincipalActivity.this, "Error !", Toast.LENGTH_SHORT).show();
        }
    });
        reference= FirebaseDatabase.getInstance().getReference();;
    navigationDrawer();
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          switch(item.getItemId()) {
              case R.id.add:
          drawerLayout.closeDrawer(GravityCompat.START);
          break;
              case R.id.profilUserActivity:
                  startActivity(new Intent(PrincipalActivity.this,ProfilActivity.class));
                  drawerLayout.closeDrawer(GravityCompat.START);
                  break;
              case R.id.ticketElectrique:
                  startActivity(new Intent(PrincipalActivity.this,TicketElectrique.class));
                  drawerLayout.closeDrawer(GravityCompat.START);
                  break;
          }
              return true;
        }
    });
    }

    private void addDevice(String deviceName, String deviceValue) {
        HashMap<String,String>deviceMap = new HashMap<>();
        deviceMap.put("name",deviceName);
        deviceMap.put("value",deviceValue);
        reference.child("Devices").push().setValue(deviceMap);
        nameDevice.setText("");
        valueDevice.setText("");
        nameDevice.clearFocus();
        valueDevice.clearFocus();
        Toast.makeText(this, "Device added successfully", Toast.LENGTH_SHORT).show();

    }

    private void navigationDrawer() {
      navigationView.bringToFront();
      navigationView.setNavigationItemSelectedListener(this);
      navigationView.setCheckedItem(R.id.add);
      menuIcon.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                  drawerLayout.closeDrawer(GravityCompat.START);
              } else drawerLayout.openDrawer((GravityCompat.START));

          }
      });

      drawerLayout.setScrimColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
