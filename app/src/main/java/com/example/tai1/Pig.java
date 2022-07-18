package com.example.tai1;

public class Pig {
    private int resourceId;
    private String name, age, origin, weight;

    public Pig(int resourceId, String name, String age, String origin, String weight) {
        this.resourceId = resourceId;
        this.name = name;
        this.age = age;
        this.origin = origin;
        this.weight = weight;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}