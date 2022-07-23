package com.example.tai1;

public class Pig {
//    private int resourceId;
    private String name, Age, Origin, Weight;

    public Pig(){

    }
//    public Pig(int resourceId, String name, String Age, String Origin, String Weight) {
    public Pig(String name, String Age, String Origin, String Weight) {
//        this.resourceId = resourceId;
        this.name = name;
        this.Age = Age;
        this.Origin = Origin;
        this.Weight = Weight;
    }

//    public int getResourceId() {
//        return resourceId;
//    }
//
//    public void setResourceId(int resourceId) {
//        this.resourceId = resourceId;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() { return Age;}

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String Origin) {
        this.Origin = Origin;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String Weight) {
        this.Weight = Weight;
    }

}