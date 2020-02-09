package nl.ckramer.primenotes.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@DatabaseTable(tableName = "notes")
public class Note implements Serializable {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField
    private Date createdDate;

    public Note() {
    }

    public Note(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdDate = new Date();
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
