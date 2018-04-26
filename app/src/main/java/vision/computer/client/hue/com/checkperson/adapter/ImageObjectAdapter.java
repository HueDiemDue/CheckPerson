package vision.computer.client.hue.com.checkperson.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vision.computer.client.hue.com.checkperson.R;
import vision.computer.client.hue.com.checkperson.models.ImgObject;

/**
 * Created by huedi on 4/26/2018.
 */

public class ImageObjectAdapter extends RecyclerView.Adapter<ImageObjectAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ImgObject> lstImg = new ArrayList<>();

    private String TAG = "ImageObjectAdapter";

    public ImageObjectAdapter(Context mContext, ArrayList<ImgObject> lstImg) {
        this.mContext = mContext;
        this.lstImg.addAll(lstImg);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageObjectAdapter.MyViewHolder holder, final int position) {
        final ImgObject imgObject = lstImg.get(position);
        holder.tvDatetime.setText("Datetime:  " + imgObject.getTime());
        String filePath = imgObject.getData();
        if (!filePath.equals("")) {
            Bitmap myImg = BitmapFactory.decodeFile(filePath);
            Log.d(TAG, myImg.toString() + " bitmap");
            holder.imgObject.setImageBitmap(myImg);
        }
    }

    @Override
    public int getItemCount() {
        return lstImg.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDatetime;
        private ImageView imgObject;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgObject = (ImageView) itemView.findViewById(R.id.img_object);
            tvDatetime = (TextView) itemView.findViewById(R.id.tv_datetime_object);
        }
    }


}


