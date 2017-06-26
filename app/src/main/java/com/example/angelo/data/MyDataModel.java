package com.example.angelo.data;

import java.util.Observable;

/**
 * Created by angelo on 14/06/17.
 */

public class MyDataModel extends Observable {
    String myData;
    static MyDataModel singleInstance;

    private MyDataModel(){

    }

    public static MyDataModel getInstance(){
        if(singleInstance==null){
            singleInstance=new MyDataModel();
        }
        return singleInstance;
    }

    public String getMyData() {
        return myData;
    }

    public void setMyData(String myData) {
        this.myData=myData;
        setChanged();
        notifyObservers();

    }
}
