package com.garena.osam.countrylove.Activity;

/**
 * Created by Administrator on 2017-10-18.
 */


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.garena.osam.countrylove.Adapter.jsonData;
import com.garena.osam.countrylove.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017-10-18.
 */

public class MainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {
    View rootView;
    MapView mapView;
    GoogleMap googleMap;
    ArrayList<jsonData> jsonDataArrayList;
    Location currentLocation;

    public MainFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_main, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap _googleMap) {
        MapsInitializer.initialize(this.getActivity());
        googleMap = _googleMap;
        jsonDataArrayList = new ArrayList<>();
        currentLocation = new Location("current");

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.setMyLocationEnabled(true);
        Location myLocation = MainActivity.currentLocation;
        double myLatitude = myLocation.getLatitude();
        double myLongitude = myLocation.getLongitude();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), (float) 16));
        googleMap.setOnMyLocationChangeListener(this);
        // Updates the location and zoom of the MapView

        jsonDataArrayList = MainActivity.staticJsonDataArrayList;
        for(int i=0; i<jsonDataArrayList.size(); i++) {
            String company = jsonDataArrayList.get(i).getCompany();
            double latitude = jsonDataArrayList.get(i).getLatitude();
            double longitude = jsonDataArrayList.get(i).getLongitude();
            Bitmap bitmap = jsonDataArrayList.get(i).getImage();
            try {
                bitmap.getWidth();
            } catch (Exception e) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);
            }
            Bitmap resize = Bitmap.createScaledBitmap(bitmap,100,100,true);
            resize = getRoundedCornerBitmap(resize, 50);
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(resize))
                    .position(new LatLng(latitude, longitude)).title(company));
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), (float) 16));
        /*jsonDataArrayList = MainActivity.staticJsonDataArrayList;
        for(int i=0; i<jsonDataArrayList.size(); i++) {
            String company = jsonDataArrayList.get(i).getCompany();
            double lat = jsonDataArrayList.get(i).getLatitude();
            double lon = jsonDataArrayList.get(i).getLongitude();
            Bitmap bitmap = jsonDataArrayList.get(i).getImage();
            try {
                bitmap.getWidth();
            } catch (Exception e) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo3);
            }
            Bitmap resize = Bitmap.createScaledBitmap(bitmap,100,100,true);
            resize = getRoundedCornerBitmap(resize, 50);
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(resize))
                    .position(new LatLng(lat, lon)).title(company));
        }*/
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}