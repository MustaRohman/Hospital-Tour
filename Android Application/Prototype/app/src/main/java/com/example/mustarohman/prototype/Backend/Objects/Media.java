package com.example.mustarohman.prototype.Backend.Objects;

/**
 * Created by yezenalnafei on 04/03/2016.
 */
public class Media {
    private String name;
    private String directory;
    private String description;
    private int order;


    public Media (String name, String directory, String description, int order){
        this.name = name;
        this.directory = directory;
        this.description = description;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
