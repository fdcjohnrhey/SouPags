package com.example.soupags.model;

public class Food {
    private String foodName;
    private String foodDesc;
    private int foodPrice;
    private int foodImgId;
    private boolean top;

    public Food(){

    }

    public Food(String foodName, String foodDesc, int foodPrice, int foodImgId, boolean top) {
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.foodPrice = foodPrice;
        this.foodImgId = foodImgId;
        this.top = top;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodImgId() {
        return foodImgId;
    }

    public void setFoodImgId(int foodImgId) {
        this.foodImgId = foodImgId;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }
}
