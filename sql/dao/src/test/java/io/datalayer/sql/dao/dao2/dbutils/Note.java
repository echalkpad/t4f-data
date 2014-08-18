package io.datalayer.sql.dao.dao2.dbutils;

import java.util.Date;

public class Note {
    private int noteID;
    private String description;
    private Date ltime;

    @Override
    public String toString() {
        return "Note [noteID=" + noteID + ", description=" + description + ", ltime=" + ltime + "]";
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLtime() {
        return ltime;
    }

    public void setLtime(Date ltime) {
        this.ltime = ltime;
    }

}
