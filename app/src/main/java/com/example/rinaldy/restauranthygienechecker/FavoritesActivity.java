package com.example.rinaldy.restauranthygienechecker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity {

    private Integer FHRSID;
    private Establishment establishment;

    private TextView businessName;
    private ImageView rating;

    private TextView address;
    private FavoriteManager mFavoriteManager;
    private Menu mMenu;

    private boolean mIsInFavorite;


    private ListView mListView;
    private CustomViewAdapter mListViewAdapter;
    private ArrayList<Establishment> mEstablishments = new ArrayList<>();

    final AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Establishment clickedItem = mListViewAdapter.getItem(i);
            Intent intent = new Intent(FavoritesActivity.this, EstablishmentDetail.class);
            intent.putExtra("establishmentID", clickedItem.getFHRSID());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("My Favorites");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mFavoriteManager = new FavoriteManager(this);
        mEstablishments = mFavoriteManager.getFavorites();
        mListView = (ListView) findViewById(R.id.favoriteList);


        if (mEstablishments == null) {
            mListView.setVisibility(View.GONE);
            findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
            mEstablishments = new ArrayList<>();
        } else {
            mListView.setVisibility(View.VISIBLE);
            findViewById(R.id.tv_no_data).setVisibility(View.GONE);
        }

        mListViewAdapter = new CustomViewAdapter(this, mEstablishments);
        mListView.setOnItemClickListener(itemClickListener);
        mListView.setAdapter(mListViewAdapter);

        registerForContextMenu(mListView);

//        businessName = (TextView) findViewById(R.id.establishment_item_name);
//        rating = (ImageView) findViewById(R.id.establishment_item_rating);
//
//        address = (TextView) findViewById(R.id.address);
//
//        FHRSID = getIntent().getIntExtra("establishmentID", -1);
//
//        mFavoriteManager = new FavoriteManager(this);
//        mIsInFavorite = mFavoriteManager.isEstablishmentInFavorites(FHRSID);
//        initialisePage();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == mListView) {
            menu.add(0, 0, 0, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.e("dd", String.valueOf(menuInfo.position));
        if (menuInfo.position >= 0) {
            Establishment establishment = mListViewAdapter.getItem(menuInfo.position);
            mFavoriteManager.removeFromFavorites(establishment);
            mListViewAdapter.remove(establishment);
        }

        return super.onContextItemSelected(item);
    }

    private void showOnMap() {
        Double lat = establishment.getGeocode().getLatitude();
        Double lon = establishment.getGeocode().getLongitude();
        if (lat != null && lon != null) {
//            Intent intent = new Intent(EstablishmentDetail.this, DetailMapActivity.class);
//            intent.putExtra("est_name", establishment.getBusinessName());
//            intent.putExtra("est_longitude", lon);
//            intent.putExtra("est_latitude", lat);
//            startActivity(intent);
        } else {
            Toast.makeText(this, "Please try again.", Toast.LENGTH_SHORT);
        }
    }

    public void initialisePage() {
        String url = EndPoint.URLEstablishmentByID(FHRSID);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        establishment = GsonUtils.getInstance().getGson().fromJson(response, Establishment.class);

                        businessName.setText(Utils.check(establishment.getBusinessName()));
                        String date = "Date Awarded: " + (establishment.getHumanlyDate().isEmpty() ? "Not Specified" : establishment.getHumanlyDate());
                        rating.setBackgroundResource(Utils.getRatingImage(establishment));

                        address.setText(String.format("%s", establishment.getFullAddress("\n")));

                        Double lat = establishment.getGeocode().getLatitude();
                        Double lon = establishment.getGeocode().getLongitude();
                        if (lat != null && lon != null) {

                            address.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showOnMap();
                                }
                            });
                        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterForContextMenu(mListView);
    }
}
