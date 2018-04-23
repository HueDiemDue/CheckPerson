package vision.computer.client.hue.com.checkperson;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Button btnConnect;
    private TextView tvResult;
    private EditText edtRequest;
    private ImageView imgResult;
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
        String message = "";
        Socket sk;
        private Context context;

        public MyClient(Context context) {
            this.context = context;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground");
            try {
                Log.d(TAG, "init ok");
                sk = new Socket("192.168.18.101", 7806);
                Log.d(TAG, "init ok" + sk.isConnected());
                DataOutputStream os = new DataOutputStream(sk.getOutputStream());
                os.writeUTF(edtRequest.getText().toString().trim());


//                while (true) {
//                    //read text
//                    DataInputStream is = new DataInputStream(sk.getInputStream());
//                    message = message + "\n" + "Server: " + is.readUTF();
//                    Log.d("Client", message);
//
//                    //read img
//
//                }
                // read img
                ObjectInputStream ois = new ObjectInputStream(sk.getInputStream());

                try {
                    byte[] buffer = (byte[]) ois.readObject();
                    FileOutputStream fos = new FileOutputStream("sdcard/DCIM/hue.png");
                    fos.write(buffer);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "IOException" + e.toString());
            }
            try {
                sk.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            File imgFile = new File("/sdcard/DCIM/hue.png");
            if (imgFile.exists()) {
                Bitmap myImg = BitmapFactory.decodeFile(imgFile.getPath());
                Log.d(TAG, myImg.toString() + " bitmap");
                imgResult.setImageBitmap(myImg);
            }
            tvResult.setText(message);

        }
    }

}
