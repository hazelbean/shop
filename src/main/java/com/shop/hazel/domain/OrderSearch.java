package com.shop.hazel.domain;

public class OrderSearch {

    private String memberEmail;      //회원 이름
    private OrderStatus orderStatus;//주문 상태[ORDER, CANCEL]

    //Getter, Setter
    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}