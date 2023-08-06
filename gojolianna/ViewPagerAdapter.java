package com.example.gojolianna;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    //declare lang pede dito lods wag mo kalimutan ligalig mo
    Context context;
    int[] images;
    int selectedPosition = -1;
    LayoutInflater mLayoutInflater;


    // we use the shit we declare here
    public ViewPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //kunin yung size ng image dko alam para san override bro
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item, container, false);

        // reference dun sa item.xml
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        // set yung image dun sa array
        imageView.setImageResource(images[position]);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Image clicked, add it to the cart or perform any other action
                addToCart(position);
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
    public int getSelectedPosition() {
        return selectedPosition;
    }
// Method to add the clicked image to the cart
private void addToCart(int position) {
    selectedPosition = position; // Update the selected position
    notifyDataSetChanged();
    }
}

