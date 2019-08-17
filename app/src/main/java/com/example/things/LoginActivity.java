package com.example.things;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "event";
    EditText username;
    EditText password;
    com.spark.submitbutton.SubmitButton loginbtn;
    String name, pass;
    String authorization = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.user);
        password = findViewById(R.id.passid);

        loginbtn = findViewById(R.id.btn_login);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = username.getText().toString();
                pass = password.getText().toString();

                if (name.equals(" ") || pass.equals(" ")) {
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    volleyCall(name, pass);
                }
            }
        });

    }

    private void volleyCall(final String name, String pass) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://api.thingify.xyz:3000/v1/user/login";
        Map<String, String> jsonParams = new HashMap<String, String>();
        jsonParams.put("email", name);
        jsonParams.put("pass", pass);
        Log.d(TAG, "Json:" + new JSONObject(jsonParams));


        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = null;
                        JSONObject info = null;
                        try {
                            message = (String) response.get("message");
                            info = response.getJSONObject("data");
                            authorization = info.getString("Authorization");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (message.equals("Authenticated")) {

                             new CountDownTimer(3000,1000){
                              public void onFinish(){
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                            intent.putExtra("Authorization", authorization);

                            startActivity(intent);

                        }
                        public void onTick(long millisUntilFinished){}
                        }.start();
                    }
                         else {

                            Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG,"JSON:"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Log.d(TAG, "Error: " + error
                                + "\nmessage" + error.getMessage());
                    }
                });
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(postRequest);

    }
}