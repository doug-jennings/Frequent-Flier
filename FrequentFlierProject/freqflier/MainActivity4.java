package com.example.freqflier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        String pid=getIntent().getStringExtra("pid");
        RequestQueue queue= Volley.newRequestQueue(MainActivity4.this);
        Spinner spinner=findViewById(R.id.spinner);
        TextView textView9=findViewById(R.id.textView9);
        String url="http://10.0.2.2:8080/frequentflier/Flights.jsp?pid=" + pid;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String result=s.trim();
                String[] rows=s.split("#");
                ArrayList<String> flightIds = new ArrayList<String>();
                for (String row : rows) flightIds.add(row.split(",")[0]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity4.this, android.R.layout.simple_spinner_item, flightIds);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                }

        }, null);
        queue.add(request);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFlightId = parent.getSelectedItem().toString();
                String flightDetailsUrl = "http://10.0.2.2:8080/frequentflier/FlightDetails.jsp?flightid=" + selectedFlightId;
                StringRequest flightDetailsRequest = new StringRequest(Request.Method.GET, flightDetailsUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String result=s.trim();
                        String[] rows = s.split("#");

                        StringBuilder formattedData = new StringBuilder();
                        String[] firstRow = rows[0].split(",");
                        String departure = firstRow[0];
                        String arrival = firstRow[1];
                        String miles = firstRow[2];
                        formattedData.append(String.format("Departure:%s\n", departure));
                        formattedData.append(String.format("Arrival:%s\n", arrival));
                        formattedData.append(String.format("Miles: %s\n", miles));
                        formattedData.append(String.format("%-10s %s\n", "TripID", "Trip Miles"));

                        for (String row : rows) {
                            String[] columns = row.split(",");
                            formattedData.append(String.format("%-10s %s\n", columns[3], columns[4]));
                        }

                        textView9.setText(formattedData.toString());
                    }
                }, null);
                queue.add(flightDetailsRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}