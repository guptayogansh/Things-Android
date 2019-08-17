package com.example.things;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class DevicesFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    GridLayoutManager grid;



    public static DevicesFragment newInstance() {

        return new DevicesFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_devices, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewDevices);
        grid = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(grid);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String authorization = getActivity().getIntent().getStringExtra("Authorization");

        String URL = "http://api.thingify.xyz:3000/v1/users/getDevices";
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<String> deviceName = new ArrayList<>();
                        ArrayList<String> deviceId = new ArrayList<>();
                        JSONArray info = null;

                        try {
                            info = response.getJSONArray("data");
                            for(int i = 0;i<info.length();i++) {
                                JSONObject individualdevice = info.getJSONObject(i);
                                deviceName.add(individualdevice.getString("deviceName"));
                                deviceId.add(individualdevice.getString("_id"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DeviceRecycleAdapter Adapter = new DeviceRecycleAdapter(getContext(), deviceName,authorization,deviceId);
                        recyclerView.setAdapter(Adapter);
                        Log.d("Deviceresponse", "JSON"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
                        Toast.makeText(getContext(), "Cannot Fetch", Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                String authorization = getActivity().getIntent().getStringExtra("Authorization");
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Authorization",authorization);
                return headers;
            }

            @Override
            public String getBodyContentType(){
                return "application/json";
            }

            @Override
            public byte[] getBody(){
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("offset", 0);
                    jsonObject.put("limit",40);
                    jsonObject.put("isFav",0);
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }

        };

          RequestQueue requestQueue = Volley.newRequestQueue(getContext());
          requestQueue.add(postRequest);



    }



}

