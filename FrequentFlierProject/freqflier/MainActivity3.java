package com.example.freqflier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        TextView textView=findViewById(R.id.textView7);
        String pid=getIntent().getStringExtra("pid");
        RequestQueue queue= Volley.newRequestQueue(MainActivity3.this);
        String url="http://10.0.2.2:8080/frequentflier/Flights.jsp?pid=" + pid;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String result=s.trim();
                String[] rows = s.split("#");

                StringBuilder formattedData = new StringBuilder();
                formattedData.append(String.format("%-10s %-10s %s\n", "FlightID", "Flight Miles", "Destination"));

                for (String row : rows) {
                    String[] columns = row.split(",");
                    formattedData.append(String.format("%-10s %-10s %s\n", columns[0], columns[1], columns[2]));
                }

                textView.setText(formattedData.toString());
            }
        }, null);
        queue.add(request);
    }
}