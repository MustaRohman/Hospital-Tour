package com.example.mustarohman.prototype.Backend.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.Serializable;

/**
 * Created by yezenalnafei on 04/03/2016.
 */
public class Media implements Serializable{

    public enum DataType {
        IMAGE, VIDEO
    }

    private String name;
    private String directory;
    private String description;
    private int mediaID;

    private byte[] bitmapBytes;

    private File filePath;

    private DataType datatype;
    private String inBucketName;
    public Media (String name, String description, DataType datatype, String inBucketName, int mediaID){
        this.name = name;
        this.description = description;
        this.datatype = datatype;
        this.inBucketName = inBucketName;
        this.mediaID = mediaID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public DataType getDatatype() {return datatype;}

    public void setDatatype(DataType datatype) {this.datatype = datatype;}

    public String getInBucketName() {
        return inBucketName;
    }

    public void setInBucketName(String inBucketName) {
        this.inBucketName = inBucketName;
    }

    public int getMediaID() {
        return mediaID;
    }

    public void setMediaID(int mediaID) {
        this.mediaID = mediaID;
    }

    public void setBitmapBytes(byte[] bytes){
        this.bitmapBytes = bytes;
    }

    public Bitmap returnBitmap(){
        BitmapFactory.Options bmo = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath(), bmo);
        Log.d("returnBitmap", "IT WORKS");
        return bitmap;
    }

    public byte[] getBitmapBytes(){
        return bitmapBytes;
    }

    public File getFilePath() {
        return filePath;
    }

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }


}
