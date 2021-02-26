package com.example.sport_e;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search extends Fragment {
    TextView player;
    Button search;
    EditText lname;
    String url;
   // String lnames;
    private RequestQueue mQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search.
     */
    // TODO: Rename and change types and number of parameters
    public static search newInstance(String param1, String param2) {
        search fragment = new search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
         player = view.findViewById(R.id.player);
         lname = view.findViewById(R.id.pnamesrch);
         search = view.findViewById(R.id.search);
        mQueue = Volley.newRequestQueue(getActivity());

         search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String lnames= lname.getText().toString();
                 jsonParse(lnames);
             }
         });




        return view;


    }


    private  void jsonParse(final String lname){
        String URL ="https://api-football-v1.p.rapidapi.com/v2/players/search/"+lname+"?";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject api = response.getJSONObject("api");
                            JSONArray jsonArray = api.getJSONArray("players");
                            //     Toast.makeText(Standings.this, ""+jsonArray.length(), Toast.LENGTH_SHORT).show();

                                for(int i = 0;i< jsonArray.length();i++)
                                {
                                    JSONObject all = jsonArray.getJSONObject(i);
                                    String plnameapi = all.getString("player_name");
                                    String position = all.getString("position");
                                    int age = all.getInt("age");
                                    String national = all.getString("nationality");


                                   // player.append(k + 1 + ". " + Tname + "\n" + "Points: " + String.valueOf(points) + "   " + "GP: " + String.valueOf(mplayed) + "   " + "Won: " + String.valueOf(won) + "   " + "Lost: " + String.valueOf(lost) + "\n\n");

                                     player.append(i + 1 + ". " + plnameapi + "\n" + "nationality: " + national + "\n" + "Age: " + String.valueOf(age) + "\n" + "Position: " + position +  "\n----------------------------------"+"\n\n\n");



                             }





                            // abc();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("x-rapidapi-host","api-football-v1.p.rapidapi.com");
                params.put("x-rapidapi-key", "d654579843msh1a3b8cd3ad5bc1dp194125jsnaed6fbcaf160");
                return params;
            }
        };
        mQueue.add(request);
    }
}