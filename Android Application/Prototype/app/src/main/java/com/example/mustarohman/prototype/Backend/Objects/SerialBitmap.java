package com.example.mustarohman.prototype.Backend.Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by mustarohman on 17/03/2016.
 */
public class SerialBitmap implements Serializable {

    Bitmap bitmap;
    String inBucketName;
    byte[] bitmapBytes;

    public SerialBitmap(byte[] bytes, String name){
        inBucketName = name;
        this.bitmapBytes = bytes;
    }

    public Bitmap returnBitmap(){
        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

////     Converts the Bitmap into a byte array for serialization
//    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
//        bitmapBytes = byteStream.toByteArray();
////        out.write(bitmapBytes, 0, bitmapBytes.length);
//    }
//
//    // Deserializes a byte array representing the Bitmap and decodes it
//    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        int b;
//        while((b = in.read()) != -1)
//            byteStream.write(b);
//        byte bitmapBytes[] = byteStream.toByteArray();
//        bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
//    }

}
