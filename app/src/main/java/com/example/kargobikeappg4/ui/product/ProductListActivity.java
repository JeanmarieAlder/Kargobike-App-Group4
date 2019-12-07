package com.example.kargobikeappg4.ui.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kargobikeappg4.R;
import com.example.kargobikeappg4.adapter.RecyclerAdapter;
import com.example.kargobikeappg4.db.entities.Product;
import com.example.kargobikeappg4.viewmodel.order.OrderListViewModel;
import com.example.kargobikeappg4.viewmodel.product.ProductListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerAdapter<Product> adapter;
    private List<Product> products;
    private ProductListViewModel listViewModel;
    private RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //initializes recyclerview
        rView = findViewById(R.id.recycler_view_storage);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true); //size never changes

        products = new ArrayList<>();

        //Add click listener, opens details of the selected act
        adapter = new RecyclerAdapter<>((v, position) -> {
            Intent intent = new Intent(ProductListActivity.this,
                    ProductDetailActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION |
                            Intent.FLAG_ACTIVITY_NO_HISTORY
            );
            intent.putExtra("productId", products.get(position).getIdProduct());
            intent.putExtra("isEdit", true);
            startActivity(intent);
        });

        ProductListViewModel.Factory factory = new ProductListViewModel.Factory(
                getApplication()
        );

        listViewModel = ViewModelProviders.of(this, factory)
                .get(ProductListViewModel.class);
        listViewModel.getAllProducts().observe(this, productEntities -> {
            if (productEntities != null) {
                products = productEntities;
                adapter.setData(products);
            }
        });
        rView.setAdapter(adapter);

    }

    public void Product_button_productAdd(View view)
    {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("isEdit", false);

        startActivity(intent);
    }
}
