package com.example.gojolianna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class checkOut extends AppCompatActivity {
    private Button Order, Return;
    private DatabaseReference db;
    private int[] productPrices = {110, 100, 115, 65, 50};
    private Intent intent;
    private LinearLayout product_place;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        String[] productNames = {"Caramel Frappuccino", "Cold Brew", "Hot Chocolate", "Apple Cider Doughnut", "Blueberry Muffin"};

        intent = getIntent();
        int[] cartIndices = intent.getIntArrayExtra("cartIndices");

        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        String[] quantities = new String[cartIndices.length];
        Arrays.fill(quantities, "1");

        Order = findViewById(R.id.orderButton);
        Return = findViewById(R.id.ReturnButton);
        product_place = findViewById(R.id.product_place);
        TextView total = new TextView(this);
        total.setText("total: " + TOTAL(cartIndices, quantities));
        total.setTextSize(50);
        product_place.addView(total);

        for (int index : cartIndices) {
            TextView textView = new TextView(this);
            textView.setText(productNames[index] + " - $" + productPrices[index] + " quantity: ");
            textView.setTextSize(20);

            EditText editView = new EditText(this);
            editView.setText("1");
            editView.setTag(index); // Use positive index for tagging


            product_place.addView(textView);
            product_place.addView(editView);

            final EditText finalEditView = editView; // Create final copy

            editView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String input = finalEditView.getText().toString().trim(); // Use finalEditView
                    int quantity = 1;
                    if (!input.isEmpty() && TextUtils.isDigitsOnly(input)) {
                        quantity = Integer.parseInt(input);
                    }
                    int currentIndex = (int) finalEditView.getTag(); // Use finalEditView
                    quantities[currentIndex] = String.valueOf(quantity);
                    total.setText("total: " + TOTAL(cartIndices, quantities));
                }
            });
        }

        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = auth.getCurrentUser().getEmail();
                int atIndex = email.indexOf("@");
                String user = email.substring(0, atIndex);
                user = user.replaceAll("[^a-zA-Z0-9]", "_");

                placeOrder(quantities, cartIndices, user, String.valueOf(total.getText()));
                Toast.makeText(checkOut.this, "Your order is on the way", Toast.LENGTH_LONG).show();
            }
        });

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String TOTAL(int[] cartIndices, String[] quantities) {
        int total = 0;

        for (int i = 0; i < cartIndices.length; i++) {
            int index = cartIndices[i];
            int quantity = Integer.parseInt(quantities[index]);
            total += productPrices[index] * quantity;
        }

        return String.valueOf(total);
    }

    private void placeOrder(String[] quantities, int[] cartIndices, String user, String BILL) {
        String numericValue = BILL.replaceAll("[^0-9]", "");
        int bill = Integer.parseInt(numericValue);

        DatabaseReference ordersRef = db.child("Orders").child(user);

        for (int i = 0; i < cartIndices.length; i++) {
            int index = cartIndices[i];
            int quantity = Integer.parseInt(quantities[i]);

            String productKey = String.valueOf(index + 1);
            DatabaseReference productRef = db.child("Products").child(productKey);

            // Retrieve the product name
            productRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String productName = task.getResult().getValue(String.class);
                    ordersRef.child(productName).setValue(quantity);

                } else {
                    Toast.makeText(checkOut.this, "There is a problem in the system try again later", Toast.LENGTH_LONG).show();
                }
            });
        }

        // Store the total debt under user's order
        ordersRef.child("Total").setValue(bill);
    }


}
