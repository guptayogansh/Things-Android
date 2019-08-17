package com.example.things;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

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

public class RecommendationRecycleAdapter extends RecyclerView.Adapter<RecommendationRecycleAdapter.MyViewHolder> {

    private final JSONObject response;
    private Context context;
    private ArrayList<String> recommendationName;
    //private ArrayList<String> recommendationId;
    private String authorization;
    private ArrayList<String> text_accept;
    private ArrayList<String> text_ignore;
    private ArrayList<String> deviceName;
    private ArrayList<String> id;
    private ArrayList<Boolean> power;

    public RecommendationRecycleAdapter(Context context, ArrayList<String> recommendationName, String authorization
            , ArrayList<String> text_accept, ArrayList<String> text_ignore, ArrayList<String> deviceName, ArrayList<String> id, ArrayList<Boolean> power,
    JSONObject response) {
        this.context = context;
        this.recommendationName = recommendationName;
        this.authorization = authorization;
        //this.recommendationId = recommendationId;
        this.text_accept = text_accept;
        this.text_ignore= text_ignore;
        this.deviceName = deviceName;
        this.id = id;
        this.power = power;
        this.response = response;
    }

    @NonNull
    @Override
    public RecommendationRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.card_recommendations, viewGroup, false);
        return new RecommendationRecycleAdapter.MyViewHolder(view);

    }

    public void delete(int position){

        recommendationName.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecommendationRecycleAdapter.MyViewHolder myViewHolder, final int i) {
        myViewHolder.recommendations.setText(recommendationName.get(i));
        myViewHolder.on.setText(text_accept.get(i));
        myViewHolder.ignore.setText(text_ignore.get(i));


        myViewHolder.on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String URL = "http://api.thingify.xyz:3000/v1/users/triggerRec";
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response_no) {

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
                            jsonObject.put("rec",response);
                            jsonObject.put("isAccepted",true);
                            jsonObject.put("entityId",id);
                            jsonObject.put("updateDevicePayload",payload1());
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        return jsonObject.toString().getBytes();
                    }

                    private JSONObject payload1() throws JSONException{
                        JSONObject params = new JSONObject();
                        params.put("deviceName", deviceName);
                        params.put("deviceId",id);
                        params.put("currentState",payload2());
                        return  params;
                    }

                    private JSONObject payload2() throws JSONException{
                        JSONObject params = new JSONObject();
                        params.put("Power",power);
                        return params;
                    }


                };
                RequestQueue requestQueue = Volley.newRequestQueue(myViewHolder.itemView.getContext());
                requestQueue.add(postRequest);
                delete(myViewHolder.getAdapterPosition());
            }
        });

        myViewHolder.ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String URL = "http://api.thingify.xyz:3000/v1/users/triggerRec";
                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response_no) {

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
                            jsonObject.put("rec",response);
                            jsonObject.put("isAccepted",false);
                            jsonObject.put("entityId",id);
                            jsonObject.put("updateDevicePayload",payload1());
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        return jsonObject.toString().getBytes();
                    }

                    private JSONObject payload1() throws JSONException{
                        JSONObject params = new JSONObject();
                        params.put("deviceName", " ");
                        params.put("deviceId","");
                        params.put("currentState",payload2());
                        return  params;
                    }

                    private JSONObject payload2() throws JSONException{
                        JSONObject params = new JSONObject();
                        params.put("Power"," ");
                        return params;
                    }


                };
                RequestQueue requestQueue = Volley.newRequestQueue(myViewHolder.itemView.getContext());
                requestQueue.add(postRequest);
                delete(myViewHolder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount(){
        return recommendationName.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView recommendations;
        Button on;
        Button ignore;

        public MyViewHolder(View view) {
            super(view);
            recommendations = view.findViewById(R.id.recommendation_name);
            on = view.findViewById(R.id.recommendation_button1);
            ignore = view.findViewById(R.id.recommendation_button2);

        }


    }

}
