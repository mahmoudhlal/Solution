package com.hlal.m7moud.mysolutiontask.Data;

public class DataItem {
  private   String OrderType , OrderDesc ,  OrderDate , OrderStatuse  , ImageUrl;


    public DataItem(){}

    public DataItem(String orderType, String orderDesc ,String orderDate, String orderStatuse , String imageUrl ) {
        OrderType = orderType;
        OrderDesc = orderDesc ;
        OrderDate = orderDate;
        OrderStatuse = orderStatuse;
        ImageUrl = imageUrl;
    }

    public String getOrderDesc() {
        return OrderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        OrderDesc = orderDesc;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderStatuse() {
        return OrderStatuse;
    }

    public void setOrderStatuse(String orderStatuse) {
        OrderStatuse = orderStatuse;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
