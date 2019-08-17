package com.example.things;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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

public class RulesRecycleAdapter extends RecyclerView.Adapter<RulesRecycleAdapter.MyViewHolder>{


    private Context context;
    private ArrayList<String> ruleName;
    private ArrayList<String> ruleId;
    private String authorization;

    public RulesRecycleAdapter(Context context, ArrayList<String> ruleName, String authorization, ArrayList<String> ruleId){
        this.context = context;
        this.ruleName = ruleName;
        this.authorization=authorization;
        this.ruleId = ruleId;
    }


    @NonNull
    @Override
    public RulesRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.card_rules, viewGroup,false);
        return new RulesRecycleAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RulesRecycleAdapter.MyViewHolder myViewHolder,final  int i) {

        myViewHolder.rules.setText(ruleName.get(i));
       myViewHolder.state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               String URL = "http://api.thingify.xyz:3000/v1/users/disableOrEnableRule";

               if(b){
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
                               jsonObject.put("_id", ruleId.get(i));
                               jsonObject.put("enabled","true");
                           }catch(JSONException e){
                               e.printStackTrace();
                           }
                           return jsonObject.toString().getBytes();
                       }
                   };

                   RequestQueue requestQueue = Volley.newRequestQueue(myViewHolder.itemView.getContext());
                   requestQueue.add(postRequest); }


               else{

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
                               jsonObject.put("_id", ruleId.get(i));
                               jsonObject.put("enabled","false");
                           }catch(JSONException e){
                               e.printStackTrace();
                           }
                           return jsonObject.toString().getBytes();
                       }
                   };

                   RequestQueue requestQueue = Volley.newRequestQueue(myViewHolder.itemView.getContext());
                   requestQueue.add(postRequest);

               }
           }
       });

    }

    @Override
    public int getItemCount(){
        return ruleName.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rules;
        ToggleButton state;

        public MyViewHolder(View view) {
            super(view);
            rules = view.findViewById(R.id.rule_name);
            state = view.findViewById(R.id.rule_toggle);
        }


    }
}
