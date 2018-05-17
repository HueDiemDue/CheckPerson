package vision.computer.client.hue.com.checkperson.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

import vision.computer.client.hue.com.checkperson.R;
import vision.computer.client.hue.com.checkperson.adapter.ImageObjectAdapter;
import vision.computer.client.hue.com.checkperson.models.ImgObject;

import static vision.computer.client.hue.com.checkperson.utils.Utils.saveImageFromBuffer;

public class MainActivity extends AppCompatActivity {
    private Button btnConnect;
    private TextView tvResult;
    private EditText edtRequest;
    private ImageView imgResult;
    private TextView tvDate, tvNoti;
    private String TAG = "MainActivity";
    private String IP = "";
    private int PORT = 0;
    private RecyclerView rcvImg;
    private ArrayList<ImgObject> lstImg = new ArrayList<>();
    private ImageObjectAdapter imgAdapter;
    private Timer timer = new Timer();
    Handler messageHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAction();
    }

    private void initView() {
        btnConnect = (Button) findViewById(R.id.btn_connect);
        tvResult = (TextView) findViewById(R.id.tv_result);
        edtRequest = (EditText) findViewById(R.id.edt_request);
        tvNoti = (TextView) findViewById(R.id.tv_noti);
        rcvImg = (RecyclerView) findViewById(R.id.rcv_lstResult);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        rcvImg.setLayoutManager(mLayoutManager);
        rcvImg.setItemAnimator(new DefaultItemAnimator());
        rcvImg.setHasFixedSize(true);
        imgAdapter = new ImageObjectAdapter(MainActivity.this, lstImg);
        rcvImg.setAdapter(imgAdapter);
        tvNoti.setVisibility(View.GONE);

    }

    private void initAction() {
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edtRequest.getText().equals("")) {
                    Toast.makeText(MainActivity.this,
                            "You must enter a request", Toast.LENGTH_LONG).show();

                } else {
                    Log.d(TAG, "request true");
//                    MyClient myClient = new MyClient(MainActivity.this);
//                    myClient.execute();
                    new Thread(new connectToServer()).start();

                }
//                else {
//                    Toast.makeText(MainActivity.this,
//                            "Invalid login request", Toast.LENGTH_LONG).show();
//                }
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    private void refreshData(final String message) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(TAG, "message : " + message);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Log.d(TAG, "update lst back đ");
                Log.d(TAG, lstImg.size() + " refrsher ");

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "update lst ");
                if (lstImg.size() > 0) {
                    Log.d(TAG, lstImg.size() + " list");
                    tvResult.setText(message + "\n" +
                            "Server : Dangerous");
                    imgAdapter = new ImageObjectAdapter(MainActivity.this, lstImg);
                    rcvImg.setAdapter(imgAdapter);
                    imgAdapter.notifyDataSetChanged();
                    tvNoti.setVisibility(View.GONE);
                } else {
                    tvResult.setText(message);
                    tvNoti.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }

    private class connectToServer implements Runnable {
        private String message = "";
        private Socket server;
        private String filePath = "";
        private String date = "";

        @Override
        public void run() {
            try {
                Log.d(TAG, "init");
                server = new Socket(edtRequest.getText().toString().trim(), 7819);
                Log.d(TAG, "init ok" + server.isConnected());

                DataOutputStream os = new DataOutputStream(server.getOutputStream());
                DataInputStream is = new DataInputStream(server.getInputStream());
                final ObjectInputStream ois = new ObjectInputStream(server.getInputStream());

//                os.writeUTF(edtRequest.getText().toString().trim());

                os.writeUTF("connect");
                message = "Server: " + is.readUTF();
                Log.d(TAG, message);

                while (ois != null) {
                    Log.d(TAG, "while");

                    // read object img
                    try {
                        date = (String) ois.readObject();
                        byte[] buffer = (byte[]) ois.readObject();
                        Log.d(TAG, "read imgObject" + ois.available());
                        filePath = saveImageFromBuffer(buffer);
                        lstImg.clear();
                        lstImg.add(new ImgObject(date, filePath));
                        Log.d(TAG, "dd " + lstImg.size());

                        Log.d(TAG, date + "_" + buffer.length);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        Log.d(TAG, "error " + e.toString());
                    }
                    refreshData(message);
                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "IOException : " + e.toString() + "_" + "Error Connect!!!");
            }

            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
