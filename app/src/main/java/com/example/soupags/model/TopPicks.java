package com.example.soupags.model;

public class TopPicks {
    private String foodName;
    private int foodImgId;
    private int foodId;
    public TopPicks(){}

    public TopPicks(String foodName, int foodImgId) {
        this.foodName = foodName;
        this.foodImgId = foodImgId;
    }


    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodImgId() {
        return foodImgId;
    }

    public void setFoodImgId(int foodImgId) {
        this.foodImgId = foodImgId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
}
