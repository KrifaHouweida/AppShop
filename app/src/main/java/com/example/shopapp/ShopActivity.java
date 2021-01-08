package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {


    ViewFlipper imgBanner;

    private RecyclerView mRecyclerView, cRecyclerView;
    private PopularAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private CategoryAdapter cAdapter;

    private List<Popular> mPopulars;
    private List<Category> cCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

    imgBanner = findViewById(R.id.imgBanner);

    int sliders[] = {
      R.drawable.banner1, R.drawable.banner2
    };

    for (int slide:sliders){
        bannerFliper(slide);
    }

        showCategories();
        showPopularProducts();


    }

    private void showCategories() {

    cRecyclerView = findViewById(R.id.category_view);
    cRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
    cRecyclerView.setLayoutManager(mLayoutManager);

    cCategory = new ArrayList<>();
    mDatabaseRef = FirebaseDatabase.getInstance().getReference("Category");
    mDatabaseRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                Category category = postSnapshot.getValue(Category.class);
                cCategory.add(category);
            }
            cAdapter = new CategoryAdapter(ShopActivity.this, cCategory);
            cRecyclerView.setAdapter(cAdapter);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(ShopActivity.this, databaseError.getMessage() , Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void showPopularProducts() {
            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            mPopulars = new ArrayList<>();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("popular");

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        Popular popular = postSnapshot.getValue(Popular.class);
                        mPopulars.add(popular);
                    }

                    mAdapter = new PopularAdapter(ShopActivity.this, mPopulars);
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(ShopActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void bannerFliper(int image){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);
        imgBanner.addView(imageView);
        imgBanner.setFlipInterval(6000);
        imgBanner.setAutoStart(true);
        imgBanner.setInAnimation(this, android.R.anim.fade_in);
        imgBanner.setOutAnimation(this, android.R.anim.fade_out);
    }
}