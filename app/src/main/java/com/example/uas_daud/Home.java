package com.example.uas_daud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    TextView hoby,hobbi;
    TextInputEditText nama;
    RequestQueue queue,mque;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        queue = Volley.newRequestQueue(this);
        mque = Volley.newRequestQueue(this);
        hoby = findViewById(R.id.hobi);
        nama = findViewById(R.id.namaInp);
        hobbi = findViewById(R.id.hobis);

    }

    public void data(View view) {
        String URL = "http://192.168.5.219/UAS_daud/show.php";
        String userNama = nama.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("kegemaran")){
                            Toast.makeText(Home.this, "Ini response nya : "+response, Toast.LENGTH_SHORT).show();
                            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                                    "http://192.168.5.219/MPA/jsondb/show.php", null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray array = response.getJSONArray("kegemaran");
                                        for (int i = 0; i < array.length(); i++){
                                            JSONObject mahasantri = array.getJSONObject(i);
                                            String gemar = mahasantri.getString("nama_hobby");
                                            hobbi.append(gemar+"\n");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Home.this, "error :"+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            mque.add(objectRequest);
                        }else {
                            Toast.makeText(Home.this, "Nama tidak terdaftar", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("nama",userNama);
                return params;
            }
        };
        queue.add(request);

    }
}