package com.example.things;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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


public class RulesFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    GridLayoutManager grid;

        public static RulesFragment newInstance() {
            return new RulesFragment();
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_rules, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewRules);
        grid = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(grid);
        return view;
    }


    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
          final String authorization = getActivity().getIntent().getStringExtra("Authorization");
          String URL = "http://api.thingify.xyz:3000/v1/users/getRules";

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int code;
                        ArrayList<String> ruleName = new ArrayList<>();
                        ArrayList<String> ruleId = new ArrayList<>();
                        JSONArray info = null;

                        try {
                            info = response.getJSONArray("data");
                            for(int i = 0;i<info.length();i++) {
                                JSONObject individualRule = info.getJSONObject(i);
                                ruleName.add(individualRule.getString("ruleName"));
                                ruleId.add(individualRule.getString("_id"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RulesRecycleAdapter Adapter = new RulesRecycleAdapter(getContext(), ruleName,authorization,ruleId);
                        recyclerView.setAdapter(Adapter);
                        Log.d("RulesResponse", "Rules_JSON:"+response);
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
                    jsonObject.put("limit",5);
                    jsonObject.put("ruleName","");
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

