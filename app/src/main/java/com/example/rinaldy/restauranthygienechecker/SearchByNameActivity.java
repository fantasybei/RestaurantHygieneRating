package com.example.rinaldy.restauranthygienechecker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchByNameActivity extends AppCompatActivity {

    private Integer FHRSID;
    private Establishment establishment;

    private TextView businessName;
    private ImageView rating;

    private TextView address;
    private FavoriteManager mFavoriteManager;
    private Menu mMenu;

    private boolean mIsInFavorite;

    private SearchView mSearchView;

    private RecyclerView mRecyclerView;

    private ItemAdapter mItemAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchByNameActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);
        mSearchView = findViewById(R.id.searchView);
        mSearchView.setIconifiedByDefault(false);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItemAdapter = new ItemAdapter(this, new ArrayList<Establishment>());
        mRecyclerView.setAdapter(mItemAdapter);

        mItemAdapter.setListener(new ItemAdapter.OnEstablishmentClickListener() {
            @Override
            public void onEstablishmentClicked(Establishment e) {
                Intent intent = new Intent(SearchByNameActivity.this, EstablishmentDetail.class);
                intent.putExtra("establishmentID", e.getFHRSID());
                startActivity(intent);
                finish();
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    initialisePage(newText);
                } else {
                    mItemAdapter.clear();
                }
                return false;
            }
        });

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //initialisePage();
    }

    private void showOnMap() {
        Double lat = establishment.getGeocode().getLatitude();
        Double lon = establishment.getGeocode().getLongitude();
        if (lat != null && lon != null) {
            Intent intent = new Intent(SearchByNameActivity.this, DetailMapActivity.class);
            intent.putExtra("est_name", establishment.getBusinessName());
            intent.putExtra("est_longitude", lon);
            intent.putExtra("est_latitude", lat);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT);
        }
    }

    public class Result implements Serializable {
        private List<Establishment> establishments;

        public List<Establishment> getEstablishments() {
            return establishments;
        }

        public void setEstablishments(List<Establishment> establishments) {
            this.establishments = establishments;
        }
    }

    public void initialisePage(String searchText) {
        String url = EndPoint.URLEstablishmentByName(searchText, "");
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Result result = GsonUtils.getInstance().getGson().fromJson(response, Result.class);

                        if (result.getEstablishments().size() > 0) {

                            mItemAdapter.addGirlList(result.getEstablishments());
                        }
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(findViewById(android.R.id.content), "Fail to request item API", Snackbar.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-version", "2");
                headers.put("accept", "application/json");
                headers.put("content-type", "application/json");
                return headers;
            }
        };

        EndPoint.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        mMenu = menu;
        //seetFavoriteIcon();
        return true;
    }

    private void seetFavoriteIcon() {
        mIsInFavorite = mFavoriteManager.isEstablishmentInFavorites(FHRSID);
        if (mIsInFavorite) {
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.icon_bookmark_filled));
        } else {
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.icon_bookmark));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_favorite:
                mIsInFavorite = mFavoriteManager.isEstablishmentInFavorites(FHRSID);
                if (mIsInFavorite) {
                    mFavoriteManager.removeFromFavorites(establishment);
                } else {
                    mFavoriteManager.addToFavorites(establishment);
                }
                seetFavoriteIcon();
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);

        }
    }



}
