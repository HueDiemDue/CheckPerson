package vision.computer.client.hue.com.checkperson;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by huedi on 4/21/2018.
 */

public class ConnectServer extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... strings) {
        try {
            Socket sk = new Socket("192.168.18.103", 7800);
            DataOutputStream os = new DataOutputStream(sk.getOutputStream());
            os.writeUTF("connect");

            DataInputStream is = new DataInputStream(sk.getInputStream());
            String x = is.readUTF();
            sk.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }
}
