package com.example.sport_e;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link best#newInstance} factory method to
 * create an instance of this fragment.
 */
public class best extends Fragment {
    TextView league,player,assist;
    private RequestQueue mQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;
    String teamna;
    int url = 0;

    public best() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment best.
     */
    // TODO: Rename and change types and number of parameters
    public static best newInstance(String param1, String param2) {
        best fragment = new best();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best, container, false);
        mQueue = Volley.newRequestQueue(getActivity());
        league = view.findViewById(R.id.league);
        player = view.findViewById(R.id.best);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        //getting the favourite team
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    teamna = userProfile.team;

                }

                //works here Toast.makeText(Standings.this, ""+teamna, Toast.LENGTH_SHORT).show();

                if( teamna.equals("Arsenal")){

                    url = 2790;

                }
                else if( teamna.equals("Juventus")){

                    url = 2857;

                }
                else if ( teamna.equals("Real Madrid" )){

                    url = 2833;

                }


                jsonParse(url);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went Wrong!!!", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }


    private  void jsonParse(final int url){
        String URL ="https://api-football-v1.p.rapidapi.com/v2/topscorers/"+url+"?";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject api = response.getJSONObject("api");
                            int res = api.getInt("results");

                            JSONArray jsonArray = api.getJSONArray("topscorers");
                            //     Toast.makeText(Standings.this, ""+jsonArray.length(), Toast.LENGTH_SHORT).show();

                            for(int i = 0;i< jsonArray.length();i++)
                            {
                                JSONObject all = jsonArray.getJSONObject(i);
                                String plnameapi = all.getString("player_name");
                                String team = all.getString("team_name");
                                String position = all.getString("position");
                                JSONObject games = all.getJSONObject("games");
                                int appearances = games.getInt("appearences");
                                JSONObject goalsassist = all.getJSONObject("goals");
                                int goals = goalsassist.getInt("total");
                                int assists = goalsassist.getInt("assists");




                                // player.append(k + 1 + ". " + Tname + "\n" + "Points: " + String.valueOf(points) + "   " + "GP: " + String.valueOf(mplayed) + "   " + "Won: " + String.valueOf(won) + "   " + "Lost: " + String.valueOf(lost) + "\n\n");

                                player.append(i + 1 + ". " + plnameapi + "\n" + "Positon:    " + position + "\n" + "Team:    " + team + "\n" + "Position:    " + position + "\n" +"Appearances:    "+String.valueOf(appearances)+ "\n"+ "GOALS:    "+ String.valueOf(goals)+ "\n" + "Assists:    "+ String.valueOf(assists)+ "\n\n\n");



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