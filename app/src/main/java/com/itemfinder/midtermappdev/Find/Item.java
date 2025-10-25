package com.itemfinder.midtermappdev.Find;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Item {
    private String id;
    private String name;
    private String category;
    private String location;
    private String status;
    private String date;
    private String imageUrl;
    private boolean claimed;

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public Item(String name, String category, String location, String status, String date) {
        this.name = name;
        this.category = category;
        this.location = location;
        this.status = status;
        this.date = date;
        this.imageUrl = null;
    }

    public Item(String name, String category, String location, String status, String date, String imageUrl) {
        this.name = name;
        this.category = category;
        this.location = location;
        this.status = status;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
    public String getImageUrl() { return imageUrl; }
    public boolean isClaimed() { return claimed; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setLocation(String location) { this.location = location; }
    public void setStatus(String status) { this.status = status; }
    public void setDate(String date) { this.date = date; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }

    // ✅ New method to approve and remove item from Firebase "FindItems"
    public void approveAndRemove() {
        if (id != null && !id.isEmpty()) {
            DatabaseReference itemRef = FirebaseDatabase.getInstance()
                    .getReference("FindItems")
                    .child(id);
            itemRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Item approved and removed successfully.");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Failed to remove item: " + e.getMessage());
                    });
        } else {
            System.err.println("Cannot remove item: ID is null or empty.");
        }
    }
}