package com.example.dell.arduinocontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

// دي الاكتفيتي اللي هتظهرلنا فيها فيها الاجهزة المقترنه وهنختار منها جهاز ونرجع بال adress بتاعه للاكتفيتي الاولي

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final BluetoothAdapter myBluetooth ;
        Set<BluetoothDevice> pairedDevices;
        // ال set دي حاجه كدا شبه ال array كدا هنحط جواها الاجهزه المقترنه
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        final ListView devicelist = (ListView)findViewById(R.id.list);
        // دي ليست هنعرض عليها الاجهزه المقترنه

        pairedDevices = myBluetooth.getBondedDevices();
        final ArrayList list = new ArrayList();
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }

        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);

        devicelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);
                Intent i = new Intent(Main2Activity.this, MainActivity.class);
                i.putExtra("adress", address);
                startActivity(i);
            }


        });



    }
}
