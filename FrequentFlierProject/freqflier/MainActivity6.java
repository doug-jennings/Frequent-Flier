package com.example.freqflier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        String pid = getIntent().getStringExtra("pid");
        RequestQueue queue = Volley.newRequestQueue(MainActivity6.this);
        Spinner spinner = findViewById(R.id.spinner3);
        EditText editText3=findViewById(R.id.editText3);
        Button button=findViewById(R.id.button7);
        String url = "http://10.0.2.2:8080/frequentflier/GetPassengerids.jsp?pid=" + pid;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String[] rows = s.trim().split("#");
                ArrayList<String> passengerIds = new ArrayList<String>();
                for (String passengerId : rows) passengerIds.add(passengerId);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity6.this, android.R.layout.simple_spinner_item, passengerIds);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

        }, null);
        queue.add(request);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dpid = parent.getSelectedItem().toString();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String transferPointsUrl = "http://10.0.2.2:8080/frequentflier/TransferPoints.jsp?spid=" + pid + "&dpid=" + dpid + "&npoints=" + editText3.getText().toString();
                        StringRequest transferPointsRequest = new StringRequest(Request.Method.GET, transferPointsUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                String result = s.trim();
                                Toast.makeText(MainActivity6.this, result, Toast.LENGTH_LONG).show();
                            }
                        }, null);
                        queue.add(transferPointsRequest);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}