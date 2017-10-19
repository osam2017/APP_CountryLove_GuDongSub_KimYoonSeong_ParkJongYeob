package com.garena.osam.countrylove.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.garena.osam.countrylove.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-10-17.
 */

public class CustomAdapter extends BaseAdapter{
    Context context;
    ArrayList<jsonData> jsonDatas;
    TextView companyText;
    TextView areaText;
    TextView classificationText;
    TextView infoText;
    TextView distanceText;
    ImageView imageView;

    public CustomAdapter(Context context, ArrayList<jsonData> jsonDatas) {
        this.context = context;
        this.jsonDatas = jsonDatas;
    }

    @Override
    public int getCount() {
        return jsonDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return jsonDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
        }
        companyText = (TextView) convertView.findViewById(R.id.company);
        areaText = (TextView) convertView.findViewById(R.id.area);
        classificationText = (TextView) convertView.findViewById(R.id.classification);
        infoText = (TextView) convertView.findViewById(R.id.info);
        distanceText = (TextView) convertView.findViewById(R.id.distance);
        imageView = (ImageView) convertView.findViewById(R.id.image);
        companyText.setText(jsonDatas.get(position).getCompany());
        areaText.setText(jsonDatas.get(position).getArea());
        classificationText.setText(jsonDatas.get(position).getClassification());
        infoText.setText(jsonDatas.get(position).getInfo());
        if(jsonDatas.get(position).getDistance() >= 1000) {
            String distance = String.format("%.2f", jsonDatas.get(position).getDistance() * 0.001);
            distanceText.setText(distance + "km 떨어짐");
        } else {
            distanceText.setText(jsonDatas.get(position).getDistance() + "m 떨어짐");
        }
        imageView.setImageBitmap(jsonDatas.get(position).getImage());

        return convertView;
    }
}
