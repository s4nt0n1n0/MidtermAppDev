package com.itemfinder.midtermappdev.Find;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itemfinder.midtermappdev.R;

import java.util.ArrayList;
import java.util.List;

public class Finditem extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    List<Item> itemList;
    List<Item> filteredList;

    Button btnAll, btnAcademic, btnWriting, btnPersonal, btnClothing, btnGadgets, btnIDs;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_item);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize lists
        itemList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        // Start showing all items
        itemAdapter = new ItemAdapter(filteredList);
        recyclerView.setAdapter(itemAdapter);

        // Load data from Firebase
        loadItemsFromFirebase();

        // Find buttons
        btnAll = findViewById(R.id.btnAll);
        btnAcademic = findViewById(R.id.btnAcademic);
        btnWriting = findViewById(R.id.btnWriting);
        btnPersonal = findViewById(R.id.btnPersonal);
        btnClothing = findViewById(R.id.btnClothing);
        btnGadgets = findViewById(R.id.btnGadgets);
        btnIDs = findViewById(R.id.btnIDs);

        // Default highlight "All"
        resetCategoryButtons();
        highlightButton(btnAll);

        // Set listeners
        btnAll.setOnClickListener(v -> {
            showAllItems();
            resetCategoryButtons();
            highlightButton(btnAll);
        });

        btnAcademic.setOnClickListener(v -> {
            filterCategory("Academic Materials");
            resetCategoryButtons();
            highlightButton(btnAcademic);
        });

        btnWriting.setOnClickListener(v -> {
            filterCategory("Writing & Drawing Tools");
            resetCategoryButtons();
            highlightButton(btnWriting);
        });

        btnPersonal.setOnClickListener(v -> {
            filterCategory("Personal Belongings");
            resetCategoryButtons();
            highlightButton(btnPersonal);
        });

        btnClothing.setOnClickListener(v -> {
            filterCategory("Clothing & Accessories");
            resetCategoryButtons();
            highlightButton(btnClothing);
        });

        btnGadgets.setOnClickListener(v -> {
            filterCategory("Gadgets & Electronics");
            resetCategoryButtons();
            highlightButton(btnGadgets);
        });

        btnIDs.setOnClickListener(v -> {
            filterCategory("IDs & Cards");
            resetCategoryButtons();
            highlightButton(btnIDs);
        });
    }

    // ✅ Load items from Firebase (skip approved/claimed items)
    private void loadItemsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);

                    // ✅ Only show items not yet approved or claimed
                    if (item != null && !item.isClaimed()) {
                        itemList.add(item);
                    }
                }
                showAllItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // ✅ Filter items by category (excluding claimed)
    @SuppressLint("NotifyDataSetChanged")
    private void filterCategory(String category) {
        filteredList.clear();
        for (Item item : itemList) {
            if (item.getCategory().equals(category) && !item.isClaimed()) {
                filteredList.add(item);
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

    // ✅ Show all items (excluding claimed)
    @SuppressLint("NotifyDataSetChanged")
    private void showAllItems() {
        filteredList.clear();
        for (Item item : itemList) {
            if (!item.isClaimed()) {
                filteredList.add(item);
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

    // Reset all category buttons
    private void resetCategoryButtons() {
        Button[] buttons = {btnAll, btnAcademic, btnWriting, btnPersonal, btnClothing, btnGadgets, btnIDs};
        for (Button btn : buttons) {
            btn.setBackgroundColor(Color.LTGRAY);
            btn.setTextColor(Color.BLACK);
        }
    }

    // Highlight selected button
    private void highlightButton(Button button) {
        button.setBackgroundColor(Color.parseColor("#F44336")); // Red
        button.setTextColor(Color.WHITE);
    }
}
