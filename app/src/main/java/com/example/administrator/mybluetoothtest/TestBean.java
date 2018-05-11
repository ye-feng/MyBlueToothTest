package com.example.administrator.mybluetoothtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestBean {
    private int test_number;
    private String sample_name;
    private String evaluation_methods;
    private String person;
    private int wet;
    private int gluten;
    private int water;
    private int dough_time;
    private Date test_date;
    private float Rm;
    private float R50;
    private float E;
    private float Power;
    private float Pull_rate;
    private List<Float> test_datas=new ArrayList<>();

    public int getTest_number() {
        return test_number;
    }

    public void setTest_number(int test_number) {
        this.test_number = test_number;
    }

    public String getSample_name() {
        return sample_name;
    }

    public void setSample_name(String sample_name) {
        this.sample_name = sample_name;
    }

    public String getEvaluation_methods() {
        return evaluation_methods;
    }

    public void setEvaluation_methods(String evaluation_methods) {
        this.evaluation_methods = evaluation_methods;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getWet() {
        return wet;
    }

    public void setWet(int wet) {
        this.wet = wet;
    }

    public int getGluten() {
        return gluten;
    }

    public void setGluten(int gluten) {
        this.gluten = gluten;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getDough_time() {
        return dough_time;
    }

    public void setDough_time(int dough_time) {
        this.dough_time = dough_time;
    }

    public Date getTest_date() {
        return test_date;
    }

    public void setTest_date(Date test_date) {
        this.test_date = test_date;
    }

    public float getRm() {
        return Rm;
    }

    public void setRm(float rm) {
        Rm = rm;
    }

    public float getR50() {
        return R50;
    }

    public void setR50(float r50) {
        R50 = r50;
    }

    public float getE() {
        return E;
    }

    public void setE(float e) {
        E = e;
    }

    public float getPower() {
        return Power;
    }

    public void setPower(float power) {
        Power = power;
    }

    public float getPull_rate() {
        return Pull_rate;
    }

    public void setPull_rate(float pull_rate) {
        Pull_rate = pull_rate;
    }

    public List<Float> getTest_datas() {
        return test_datas;
    }

    public void setTest_datas(List<Float> test_datas) {
        this.test_datas = test_datas;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "test_number=" + test_number +
                ", sample_name='" + sample_name + '\'' +
                ", evaluation_methods='" + evaluation_methods + '\'' +
                ", person='" + person + '\'' +
                ", wet=" + wet +
                ", gluten=" + gluten +
                ", water=" + water +
                ", dough_time=" + dough_time +
                ", test_date=" + test_date +
                ", Rm=" + Rm +
                ", R50=" + R50 +
                ", E=" + E +
                ", Power=" + Power +
                ", Pull_rate=" + Pull_rate +
                ", test_datas=" + test_datas +
                '}';
    }
}
