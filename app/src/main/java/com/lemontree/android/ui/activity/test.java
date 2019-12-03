package com.lemontree.android.ui.activity;


public class test {
    public static void main(String[] args) {
        String mPhoneNum = "023234";
        if (mPhoneNum.startsWith("0")) {
            mPhoneNum = mPhoneNum.replaceFirst("^0*", "");
            System.out.println(mPhoneNum);
        }
    }

}
