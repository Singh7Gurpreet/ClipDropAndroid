package com.example.clipdrop;

import java.util.ArrayList;

public class ObserverForText {
    String text;
    ArrayList<ObserverWorker> list =new ArrayList<ObserverWorker>();

    public void setText(String text) {
        this.text = text;
        for(ObserverWorker work: list) {
            work.doWork();
        }
    }

    public String getText() {
        return text;
    }

    void register(ObserverWorker work) {
        list.add(work);
    }
}
