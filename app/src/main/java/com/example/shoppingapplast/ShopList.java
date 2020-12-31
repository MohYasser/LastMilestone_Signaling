package com.example.shoppingapplast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shoppingapplast.app.AppConfig;
import com.example.shoppingapplast.app.AppController;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopList extends AppCompatActivity implements DetailedAdapter.OnShopItemClicked{
    String TAG = "ShopList" ;

    private static  final String BASE_URL_links = "http://192.168.1.17/android_login_api/GetLinks.php";
    private static  final String BASE_URL_shops = "http://192.168.1.17/android_login_api/GetShops.php";

    private TextView mShopName, mPrice, mSpOffers, mDistance;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    RecyclerView mRecyclerView;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    Location CurrentLocation ;

    private ArrayList<Link> links;
    private ArrayList<Shop> shops;
    private ArrayList<Shop_enhanced> arraylist;

    private ArrayList<String> shop_ids;
    private ArrayList<Double> shop_prices;
    private ArrayList<String> shop_sp_offers;
    private double[] shop_lat;
    private double[] shop_long;

    String product_id;
    ArrayList<String> product_ID;
    float distanceInMeters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_layout);

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        shop_ids = new ArrayList<>();
        shop_prices= new ArrayList<>();
        shop_sp_offers= new ArrayList<>();

        links = new ArrayList<>();
        shops = new ArrayList<>();
        //product_ID = new ArrayList<>();
        arraylist = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            product_id = (String) bundle.get("product_id");
        }

        Log.d(TAG, "onCreate: product id is: "+product_id);

        getDeviceLocation(product_id);

        mAdapter = new DetailedAdapter(arraylist, ShopList.this);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getDeviceLocation(String product_id){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{

            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();
                        if(currentLocation!= null){
                            //Toast.makeText(MainActivity.this, "Please open the location", Toast.LENGTH_SHORT).show();
                            ShopList.this.CurrentLocation =currentLocation ;
                            GetLinkTable(product_id) ;
                        }
                        else{
                            Toast.makeText(ShopList.this, "Open the Location Please!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(ShopList.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void GetLinkTable(String product_id){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL_links,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                //Log.d(TAG, "onResponse: " + object);
                                String shop_id = object.getString("shop_ID");
                                String prod_id = object.getString("product_ID");
                                double price = Double.parseDouble(object.getString("price"));
                                String sp_offers = object.getString("sp_offers");

                                Link link = new Link(shop_id, prod_id, price, sp_offers);
                                links.add(link);
                            }

                            for (int i=0; i<links.size(); i++) {
                                Link l = links.get(i);
                                if (product_id.equals(l.getProduct_id())) {
                                    //product_ID.add(product_id);
                                    shop_ids.add(l.getShop_id());
                                    shop_prices.add(l.getPrice());
                                    shop_sp_offers.add(l.getSp_offers());
                                }
                            }

                            GetShops(links, shop_ids, shop_prices, shop_sp_offers);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void GetShops(ArrayList<Link> links, ArrayList<String> shop_ids, ArrayList<Double> shop_prices, ArrayList<String> shop_sp_offers){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL_shops,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                //Log.d(TAG, "ooooooooooooooooooooooooooooooooo"+object);

                                String id = object.getString("id");
                                String shop_name = object.getString("shop_name");
                                float lattitude = Float.parseFloat(object.getString("lattitude"));
                                float longitude = Float.parseFloat(object.getString("longitude"));

                                Shop shop = new Shop(id, shop_name, lattitude, longitude);
                                shops.add(shop);
                            }

                            String[] shop_names = new String[shop_ids.size()];
                            double[] shop_lat = new double[shop_ids.size()];
                            double[] shop_long = new double[shop_ids.size()];

                            int k = 0;
                            for (int i=0; i< shop_ids.size(); i++) {
                                String id = shop_ids.get(i);
                                for (int j=0; j<shops.size(); j++) { //looping over all the shops we want to get:
                                    Shop s = shops.get(j);
                                    if (id.equals(s.getId())) { //the shop that has one of the ids in shop_ids and put that shop's name
                                        shop_names[k] = s.getShop_name(); // in the shop_names array
                                        shop_lat[k] = s.getLattitude(); // and put its lat in the shop_lat array
                                        shop_long[k] = s.getLongitude(); // and put its long in the shop_long array
                                        k++;
                                    }
                                }
                                Location loc = new Location("");
                                loc.setLatitude(shop_lat[i]);
                                loc.setLongitude(shop_long[i]);

                                float distanceInMeters = loc.distanceTo(ShopList.this.CurrentLocation);

                                arraylist.add(new Shop_enhanced(shop_names[i],
                                        shop_prices.get(i), Math.round(distanceInMeters / 1000), shop_sp_offers.get(i)));
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    @Override
    public void OnShopItemClick(int position) {
        Log.d(TAG , "clicked") ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortDist:
                Toast.makeText(getApplicationContext(), "distance Clicked", Toast.LENGTH_LONG).show();

                return true;
            case R.id.sortPrice:
                Toast.makeText(getApplicationContext(), "price Clicked", Toast.LENGTH_LONG).show();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }
}