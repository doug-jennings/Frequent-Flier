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

public class MainActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        String pid = getIntent().getStringExtra("pid");
        RequestQueue queue = Volley.newRequestQueue(MainActivity5.this);
        Spinner spinner = findViewById(R.id.spinner2);
        TextView textView11 = findViewById(R.id.textView11);
        String url = "http://10.0.2.2:8080/frequentflier/AwardIds.jsp?pid=" + pid;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String[] rows = s.trim().split("#");
                ArrayList<String> awardIds = new ArrayList<String>();
                for (String awardId : rows) awardIds.add(awardId);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity5.this, android.R.layout.simple_spinner_item, awardIds);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

        }, null);
        queue.add(request);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAwardId = parent.getSelectedItem().toString();
                String flightDetailsUrl = "http://10.0.2.2:8080/frequentflier/RedemptionDetails.jsp?awardid=" + selectedAwardId + "&pid=" + pid;
                StringRequest flightDetailsRequest = new StringRequest(Request.Method.GET, flightDetailsUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String result = s.trim();
                        String[] rows = s.split("#");

                        StringBuilder formattedData = new StringBuilder();
                        String[] firstRow = rows[0].split(",");
                        String description = firstRow[0];
                        String pointsNeeded = firstRow[1];
                        formattedData.append(String.format("Prize desc:%s\n", description));
                        formattedData.append(String.format("Points needed:%s\n", pointsNeeded));
                        formattedData.append(String.format("%-10s %s\n", "Redemption Date", "Exchg Center"));

                        for (String row : rows) {
                            String[] columns = row.split(",");
                            formattedData.append(String.format("%-10s %s\n", columns[2], columns[3]));
                        }

                        textView11.setText(formattedData.toString());
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