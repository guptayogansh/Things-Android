package com.example.things;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class RecommendationsFrag extends Fragment {
    View view;
    RecyclerView recyclerView;
    //GridLayoutManager grid;
    Button on;

    public static RecommendationsFrag newInstance() {
        return new RecommendationsFrag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {

        view =  inflater.inflate(R.layout.fragment_recommendations, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewRecommend);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //grid = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String authorization = getActivity().getIntent().getStringExtra("Authorization");
        String URL = "http://api.thingify.xyz:3000/v1/users/getRec";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        ArrayList<String> recommendationName = new ArrayList<>();
                        //ArrayList<String> recommendationId = new ArrayList<>();
                        JSONArray info = null;
                        ArrayList<String> text_accept = new ArrayList<>();
                        ArrayList<String> text_ignore= new ArrayList<>();
                        ArrayList<String> deviceName = new ArrayList<>();
                        ArrayList<String> id = new ArrayList<>();
                        ArrayList<Boolean> power = new ArrayList<>();

                        try {
                            info = response.getJSONArray("data");
                            for(int i = 0;i<info.length();i++) {
                                JSONObject individualRecommendation = info.getJSONObject(i);
                                recommendationName.add(individualRecommendation.getString("rec"));
                                //recommendationId.add(individualRecommendation.getString("recID"));
                                JSONArray calltoaction = individualRecommendation.getJSONArray("cta");
                                int count = calltoaction.length();
                                JSONObject accept = calltoaction.getJSONObject(count - 2);
                                JSONObject ignore = calltoaction.getJSONObject(count - 1);
                                text_accept.add(accept.getString("text"));
                                text_ignore.add(ignore.getString("text"));
                                JSONObject action_accept = accept.getJSONObject("action");
                                JSONObject action_ignore = ignore.getJSONObject("action");
                                deviceName.add(action_accept.getString("deviceName"));
                                JSONObject state = action_accept.getJSONObject("state");
                                power.add(state.getBoolean("Power"));
                                id.add(action_ignore.getString("_id"));
                            }
                                Log.d("tag","text_accept:"+text_accept);
                                Log.d("tag","text_ignore:"+text_ignore);
                                Log.d("tag","deviceName:"+deviceName);
                                Log.d("tag","id:"+id);
                                Log.d("tag","power"+power);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RecommendationsResponse", "Recommendations_JSON:"+response);
                        RecommendationRecycleAdapter Adapter = new RecommendationRecycleAdapter(getContext(), recommendationName,authorization,text_accept,
                                text_ignore,deviceName,id,power,response);
                        recyclerView.setAdapter(Adapter);
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


        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(postRequest);


    }
}