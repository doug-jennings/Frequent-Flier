package com.example.freqflier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText=findViewById(R.id.editText);
        EditText editText2=findViewById(R.id.editText2);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=editText.getText().toString();
                String password=editText2.getText().toString();
                RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
                String url="http://10.0.2.2:8080/frequentflier/login?user=" + username + "&pass="+ password;
                Intent intent=new Intent(MainActivity.this, MainActivity2.class);
                StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String result=s.trim();
                        if (result.contains("Yes")) {
                            String pid=result.split(":")[1];
                            intent.putExtra("pid", pid);
                            startActivity(intent);
                        } else Toast.makeText(MainActivity.this, "Invalid username or password.", Toast.LENGTH_LONG).show();

                    }
                }, null);
                queue.add(request);
            }
        });
    }
}