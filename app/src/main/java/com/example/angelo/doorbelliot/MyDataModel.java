package com.example.angelo.doorbelliot;

import java.util.Observable;

/**
 * Created by angelo on 14/06/17.
 */

public class MyDataModel extends Observable {
    String myData;
    static MyDataModel singleInstance;

    private MyDataModel(){

    }
    // Singleton design pattern
    // usando questo pattern, siamo sicuri di avere
    // una sola istanza del nostro modello dati
    // e lo rendiamo reperibile agli utilizzatori tramite un metodo che ritorna la sua istanza
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
        // ogni volta che i dati cambiano, gli osservanti vengono informati
        // il metodo update verrà chiamato su di essi
    }
}
