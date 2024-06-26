package com.genius.employee.model;

public class SpinnerModelForReim {
    String itemName;
    int itemId,categoryID;

    public SpinnerModelForReim(String itemName, int itemId,int categoryID ) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.categoryID=categoryID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}
