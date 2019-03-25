package com.example.rinaldy.restauranthygienechecker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity {

    private static final String LIST_KEY = "establishment_list";

    private ArrayList<Establishment> mEstablishmentList;

    public static void start(Context context, ArrayList<Establishment> establishmentList) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(LIST_KEY, establishmentList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Map");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mEstablishmentList = (ArrayList<Establishment>) getIntent().getSerializableExtra(LIST_KEY);

        initialisePage();
    }

    public void initialisePage() {
        MapViewFragment mvf = new MapViewFragment();
        mvf.initialiseList(mEstablishmentList);
        getSupportFragmentManager().beginTransaction().add(R.id.map, mvf).commit();
//        String url = EndPoint.URLEstablishmentByID(FHRSID);
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        establishment = GsonUtils.getInstance().getGson().fromJson(response, Establishment.class);
//
//                        businessName.setText(Utils.check(establishment.getBusinessName()));
//                        String date = "Date Awarded: " + (establishment.getHumanlyDate().isEmpty() ? "Not Specified" : establishment.getHumanlyDate());
//                        rating.setBackgroundResource(Utils.getRatingImage(establishment));
//
//                        address.setText(String.format("%s", establishment.getFullAddress("\n")));
//
//                        Double lat = establishment.getGeocode().getLatitude();
//                        Double lon = establishment.getGeocode().getLongitude();
//                        if (lat != null && lon != null) {
//
//                            address.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    showOnMap();
//                                }
//                            });
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Snackbar.make(findViewById(android.R.id.content), "Fail to request item API", Snackbar.LENGTH_LONG).show();
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("x-api-version", "2");
//                headers.put("accept", "application/json");
//                headers.put("content-type", "application/json");
//                return headers;
//            }
//        };
//
//        EndPoint.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_details, menu);
//        mMenu = menu;
//        seetFavoriteIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_favorite:
//                mIsInFavorite = mFavoriteManager.isEstablishmentInFavorites(FHRSID);
//                if (mIsInFavorite) {
//                    mFavoriteManager.removeFromFavorites(establishment);
//                } else {
//                    mFavoriteManager.addToFavorites(establishment);
//                }
//                seetFavoriteIcon();
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);

        }
    }



}
