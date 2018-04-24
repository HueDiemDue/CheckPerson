package vision.computer.client.hue.com.checkperson.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import vision.computer.client.hue.com.checkperson.R;
import vision.computer.client.hue.com.checkperson.models.ImgObject;

public class MainActivity extends AppCompatActivity {
    private Button btnConnect;
    private TextView tvResult;
    private EditText edtRequest;
    private ImageView imgResult;
    private TextView tvDate;
    private String TAG = "MainActivity";

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
        imgResult = (ImageView) findViewById(R.id.img_result);
        tvDate = (TextView) findViewById(R.id.tv_date);
    }

    private void initAction() {
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtRequest.getText().equals("")) {
                    Toast.makeText(MainActivity.this,
                            "You must enter a request", Toast.LENGTH_LONG).show();

                } else if (edtRequest.getText().toString().trim().equals("connect")) {
                    Log.d(TAG, "request true");
                    MyClient cs = new MyClient(MainActivity.this);
                    cs.execute();

                } else {
                    Toast.makeText(MainActivity.this,
                            "Invalid login request", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private class MyClient extends AsyncTask<Void, Void, Void> {
        private String message = "";
        private Socket sk;
        private String filePath = "";
        private String date = "";
        private Context context;

        public MyClient(Context context) {
            this.context = context;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground");
            try {
                Log.d(TAG, "init");
                sk = new Socket("192.168.18.101", 7817);
                Log.d(TAG, "init ok" + sk.isConnected());
                DataOutputStream os = new DataOutputStream(sk.getOutputStream());
                os.writeUTF(edtRequest.getText().toString().trim());


                while (true) {
                    //read text
//                    DataInputStream is = new DataInputStream(sk.getInputStream());
//                    message = "Server: " + is.readUTF();
//                    Log.d("Client", message);

                    // read object img

                    ObjectInputStream ois = new ObjectInputStream(sk.getInputStream());
                    Log.d(TAG, "read img");

                    try {
                        Log.d(TAG, "read imgObjectss");
//                        byte[] buffer = (byte[]) ois.readObject();
                        ImgObject imgObject = (ImgObject) ois.readObject();
                        Log.d(TAG, "read imgObject");
//                            filePath = saveImageFromBuffer(imgObject.getData().getBytes());

                        date = imgObject.getTime();
                        Log.d(TAG, date + "_" + imgObject.getData());

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "IOException : " + e.toString() + "_" + "Error Connect!!!");
            }
            if (sk != null) {
                try {
                    sk.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!filePath.equals("")) {
                Bitmap myImg = BitmapFactory.decodeFile(filePath);
                Log.d(TAG, myImg.toString() + " bitmap");
                imgResult.setImageBitmap(myImg);
            }
            tvDate.setText("Datetime : " + date.toString());
            tvResult.setText(message);

        }
    }

}
