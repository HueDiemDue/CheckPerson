package vision.computer.client.hue.com.checkperson.models;

import java.io.Serializable;

/**
 * Created by huedi on 4/24/2018.
 */

public class ImgObject implements Serializable{
    private String time;
    private String filePath;
    private byte[] buffer;
    private String data;

    public ImgObject() {

    }

    public ImgObject(String time, byte[] buffer) {
        this.time = time;
        this.buffer = buffer;
    }

    public ImgObject(String time, String data) {
        this.time = time;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
