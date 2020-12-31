package com.example.shoppingapplast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends AppCompatActivity implements RecyclerAdapter.OnItemClicked{
    private String TAG;

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Product> products;

    private static  final String BASE_URL = "http://192.168.1.17/android_login_api/GetProducts.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_layout);

        TAG = "ProductView";
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getProducts();

    }

    private void getProducts (){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //Log.d(TAG, "The retrieved sting" + response);
                            JSONArray array = new JSONArray(response);
                            products = new ArrayList<Product>();

                            for (int i = 0; i<array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);

                                products.add(new Product(obj.getString("id"), obj.getString("item_name"),
                                        obj.getString("description"), obj.getString("image_url") ));

                            }

                            mRecyclerView = findViewById(R.id.recyclerView);
                            mRecyclerView.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            mAdapter = new RecyclerAdapter(products ,ProductList.this);
                            mRecyclerView.setAdapter(mAdapter);

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
    public void onItemClick(int position) {
        Log.d(TAG , "Click" +position) ;
        Intent intent = new Intent( ProductList.this , ShopList.class) ;
        intent.putExtra("product_id", (products.get(position)).getId());
        startActivity(intent);
    }

}
