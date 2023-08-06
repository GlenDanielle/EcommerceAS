package com.example.gojolianna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    private Button ATC, CO; //add to cart and check out
    private String prod;
    private Set<Integer> cartItems = new HashSet<>();

    int[] images = {R.drawable.fransnew, R.drawable.cdbnew, R.drawable.hcnnew, R.drawable.doneww,
            R.drawable.munew};

    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");

        mViewPager = findViewById(R.id.viewPagerMain);
        mViewPagerAdapter = new ViewPagerAdapter(MainActivity.this, images);
        mViewPager.setAdapter(mViewPagerAdapter);

        ATC = findViewById(R.id.addToCart);
        CO = findViewById(R.id.checkOut);

        ATC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() >= 0 && mViewPager.getCurrentItem() < images.length) {
                    int selectedImageIndex = mViewPager.getCurrentItem();
                    if (cartItems.contains(selectedImageIndex)) {
                        Toast.makeText(getApplicationContext(), "Click the check-out if you want to customize", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Product has been put in the cart", Toast.LENGTH_SHORT).show();
                        cartItems.add(selectedImageIndex);

                        //Toast.makeText(getApplicationContext(), cartItems.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        CO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cartItems.isEmpty()) {
                    // Proceed to checkout
                    Intent intent = new Intent(MainActivity.this, checkOut.class);
                    // Pass the cart items or indices to the checkout activity as intent extras
                    int[] cartIndices = new int[cartItems.size()];
                    int index = 0;
                    for (Integer item : cartItems) {
                        cartIndices[index] = item;
                        index++;
                    }
                    intent.putExtra("cartIndices", cartIndices);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}