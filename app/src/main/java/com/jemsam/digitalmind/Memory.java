package com.jemsam.digitalmind;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by jeremy.toussaint on 12/10/16.
 */

public class Memory extends SugarRecord {

    private String title;
    private String description;
    private Date date;

    public Memory() {
    }

    public Memory(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Memory(String title, String description, String date) {
        this.title = title;
        this.description = description;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static List<Memory> getAllMemories(){
        return Memory.listAll(Memory.class);
    }
}
