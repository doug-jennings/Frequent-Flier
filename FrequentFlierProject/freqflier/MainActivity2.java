package com.example.freqflier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ImageView imageView=findViewById(R.id.imageView);
        String pid=getIntent().getStringExtra("pid");

        switch(pid){
            case "1":
                imageView.setImageResource(R.drawable._1);
                break;
            case "2":
                imageView.setImageResource(R.drawable._2);
                break;
            case "3":
                imageView.setImageResource(R.drawable._3);
                break;
            case "4":
                imageView.setImageResource(R.drawable._4);
                break;
            case "5":
                imageView.setImageResource(R.drawable._5);
                break;
        }

        TextView username=findViewById(R.id.textView6);
        TextView rewardPoints=findViewById(R.id.textView3);

        RequestQueue queue= Volley.newRequestQueue(MainActivity2.this);
        String url="http://10.0.2.2:8080/frequentflier/Info.jsp?pid="+pid;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String[] result=s.trim().split(",");
                username.setText(result[0]);
                rewardPoints.setText(result[1]);

            }
        }, null);
        queue.add(request);


        Button flightDetails=findViewById(R.id.button2);
        Button allFlights=findViewById(R.id.button3);
        Button redemptionInfo=findViewById(R.id.button4);
        Button transferPoints=findViewById(R.id.button5);
        Button exit=findViewById(R.id.button6);


        flightDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity2.this, MainActivity3.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
            }
        });

        allFlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity2.this, MainActivity4.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
            }
        });

        redemptionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity2.this, MainActivity5.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
            }
        });

        transferPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity2.this, MainActivity6.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}