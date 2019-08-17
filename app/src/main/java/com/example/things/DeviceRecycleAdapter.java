package com.example.things;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class DeviceRecycleAdapter extends RecyclerView.Adapter<DeviceRecycleAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> deviceName;
    private ArrayList<String> deviceId;
    private String authorization;



    public DeviceRecycleAdapter(Context context, ArrayList<String> deviceName, String authorization, ArrayList<String> deviceId){
        this.context = context;
        this.deviceName = deviceName;
        this.authorization=authorization;
        this.deviceId = deviceId;
    }
    @NonNull
    @Override
    public DeviceRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.card_devices, viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceRecycleAdapter.MyViewHolder myViewHolder, final int i) {

        myViewHolder.devices.setText(deviceName.get(i));

        myViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (myViewHolder.container.getCardBackgroundColor().getDefaultColor() == -16777216) {
                   //myViewHolder.container.setBackgroundColor(Color.parseColor("#398DEF"));
                int color = myViewHolder.container.getContext().getResources().getColor(R.color.navBottomBlue);
                Log.d("tag","value:"+color);
                myViewHolder.container.setCardBackgroundColor(color);
                String URL = "http://api.thingify.xyz:3000/v1/users/triggerDevice";
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        })
                    {
                    @Override
                    public Map<String,String> getHeaders() throws AuthFailureError {
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
                            jsonObject.put("_id", deviceId.get(i));
                            jsonObject.put("deviceName",deviceName.get(i));
                            jsonObject.put("state",state());
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        return jsonObject.toString().getBytes();
                    }

                    private JSONObject state() throws JSONException{
                        JSONObject params = new JSONObject();
                        params.put("Power", true);
                        return  params;
                    }


                };

                    RequestQueue requestQueue = Volley.newRequestQueue(myViewHolder.itemView.getContext());
                requestQueue.add(postRequest); }


                else{
                    //myViewHolder.container.setBackgroundColor(Color.parseColor("#398DEF"));
                    int color = myViewHolder.container.getContext().getResources().getColor(R.color.black);
                    myViewHolder.container.setCardBackgroundColor(color);
                    Log.d("tag","value1:"+color);
                    String URL = "http://api.thingify.xyz:3000/v1/users/triggerDevice";
                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            })
                    {
                        @Override
                        public Map<String,String> getHeaders() throws AuthFailureError {
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
                                jsonObject.put("_id", deviceId.get(i));
                                jsonObject.put("deviceName",deviceName.get(i));
                                jsonObject.put("state",state());
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                            return jsonObject.toString().getBytes();
                        }

                        private JSONObject state() throws JSONException{
                            JSONObject params = new JSONObject();
                            params.put("Power", false);
                            return  params;
                        }


                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(myViewHolder.itemView.getContext());
                    requestQueue.add(postRequest); }




            }

        });

    }


    @Override
    public int getItemCount() {
        return deviceName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView devices;
        CardView container;

        public MyViewHolder(View view) {
            super(view);
            devices = view.findViewById(R.id.device_name);
            container = view.findViewById(R.id.device_card);
        }


    }
}
