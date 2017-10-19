package com.garena.osam.countrylove.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.garena.osam.countrylove.Adapter.CustomAdapter;
import com.garena.osam.countrylove.Adapter.jsonData;
import com.garena.osam.countrylove.Method.RequestHttpConnection;
import com.garena.osam.countrylove.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // 변수 선언
    static public ArrayList<jsonData> staticJsonDataArrayList = null;
    private LocationManager locationManager = null;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            currentLocationText.setText(conversionToAddress(location.getLatitude(), location.getLongitude(), false));
            currentLocation.setLatitude(location.getLatitude());
            currentLocation.setLongitude(location.getLongitude());
            runOnUiThread(new Runnable() {
                public void run() {
                    for (int i = 0; i < jsonDataArrayList.size(); i++) {
                        Location objectLocation = new Location("object");
                        objectLocation.setLatitude(jsonDataArrayList.get(i).getLatitude());
                        objectLocation.setLongitude(jsonDataArrayList.get(i).getLongitude());
                        int distance = (int) currentLocation.distanceTo(objectLocation);
                        jsonDataArrayList.get(i).setDistance(distance);
                    }
                    sortArray(jsonDataArrayList);

                    customAdapter = new CustomAdapter(getApplicationContext(), jsonDataArrayList);
                    Log.d("TESTTEST", "onLocationChanged 1");
                    listView.setAdapter(customAdapter);
                    Log.d("TESTTEST", "onLocationChanged 2");
                    customAdapter.notifyDataSetChanged();
                    Log.d("TESTTEST", "onLocationChanged 3");
                }
            });
            Log.d("현위치", "위도 : " + currentLocation.getLatitude() + ", 경도 : " + currentLocation.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    private Geocoder geocoder = null;
    private List<Address> addressList = null;
    private WebView webView = null;
    private ListView listView = null;
    private TextView currentLocationText = null;
    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;
    static public Location currentLocation = null;
    private String url = null;
    private String data = null;
    private String urlServer = null;
    private String urlImage = null;
    private String urlAppend = null;
    private Bitmap bitmapImage = null;
    private ArrayList<jsonData> jsonDataArrayList = null;
    private CustomAdapter customAdapter = null;
    private Button refreshData = null;
    private MainFragment mainFragment = null;
    private ProgressDialog progressDialog = null;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("데이터 다운로드 중");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 스플래시 액티비티 띄우기
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
        // 로딩화면이 실행되는 동안 쓰레드를 통해 변수 초기화, 서버 데이터 다운로드 및 화면 띄우기
        initialize();

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost1) ;
        tabHost.setup() ;

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("",this.getResources().getDrawable(R.drawable.list2)) ;
        tabHost.addTab(ts1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator( "",this.getResources().getDrawable(R.drawable.map));
        tabHost.addTab(ts2) ;

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost.newTabSpec("Tab Spec 3") ;
        ts3.setContent(R.id.content3) ;
        ts3.setIndicator("",this.getResources().getDrawable(R.drawable.upload3)) ;
        tabHost.addTab(ts3);
    }

    @Override
    protected void onRestart() {
        // 앱이 다시 그려질 때 초기화(상세보기 후 돌아왔을 때)
        super.onRestart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, locationListener);
        customAdapter = new CustomAdapter(this, jsonDataArrayList);
        Log.d("TESTTEST", "onRestart 1");
        listView.setAdapter(customAdapter);
        Log.d("TESTTEST", "onRestart 2");
        customAdapter.notifyDataSetChanged();
        Log.d("TESTTEST", "onRestart 3");
    }

    @Override
    protected void onStop() {
        // 앱이 종료될 때 갱신 그만하기
        locationManager.removeUpdates(locationListener);
        super.onStop();
    }

    private void initialize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                webView=(WebView)findViewById(R.id.webView1);
                refreshData = (Button) findViewById(R.id.refresh);
                currentLocationText = (TextView) findViewById(R.id.curruent_location);
                listView = (ListView) findViewById(R.id.list);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);
                currentLocation = new Location("current");
                url = "http://10.53.128.148:5023";
                urlServer = url + "/sql/getallinfo";
                urlAppend = url + "/append";

                // 데이터 체크
                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                if (sharedPreferences.getBoolean("DATA", false)) {
                    // 데이터가 있을 때
                    Log.d("데이터 다운로드", "이미 데이터가 있습니다.");
                    data = sharedPreferences.getString("JSON", null);
                    jsonQuery(data);
                } else {
                    // 데이터가 없을 때 assets 폴더로부터 데이터 받아오기
                    Log.d("데이터 다운로드", "다운로드를 시작합니다.");
                    AssetManager assetManager = getResources().getAssets();
                    InputStream inputStream = null;
                    try {
                        inputStream = assetManager.open("data.json");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        String result = "";
                        String line = "";

                        while((line = bufferedReader.readLine()) != null) {
                            result += line;
                        }
                        data = result;
                        // 데이터 받은 후 공유 프리퍼런스에 저장
                        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("JSON", data);
                        editor.putBoolean("DATA", true);
                        editor.commit();
                        jsonQuery(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 위치 수신 권한이 없는 경우
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "권한을 설정해주세요", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            return;
                        }
                        // 10초동안 10m 이상 이동했을 때 감지
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, locationListener);

                        // 웹뷰 띄우기
                        setWebView(webView, urlAppend);
                    }
                });

                // 리스트 항목 상세보기
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        intent.putExtra("company", jsonDataArrayList.get(position).getCompany());
                        intent.putExtra("detail_address", jsonDataArrayList.get(position).getDetail_address());
                        intent.putExtra("classification", jsonDataArrayList.get(position).getClassification());
                        intent.putExtra("detail_info", jsonDataArrayList.get(position).getDetail_info());
                        intent.putExtra("company_info", jsonDataArrayList.get(position).getCompany_info());
                        intent.putExtra("company_tel", jsonDataArrayList.get(position).getCompany_tel());
                        intent.putExtra("image", jsonDataArrayList.get(position).getImage());
                        startActivity(intent);
                    }
                });
                // 데이터 새로고침
                refreshData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NetworkTask networkTask = new NetworkTask(urlServer, null);
                        networkTask.execute();
                    }
                });

                //지도
                mainFragment = new MainFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, mainFragment, "main")
                        .commitAllowingStateLoss();
            }
        }).start();
    }

    private void setWebView(WebView webView, String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLightTouchEnabled(true);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onCloseWindow(WebView w) {
                super.onCloseWindow(w);
                finish();
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                final WebSettings settings = view.getSettings();
                settings.setDomStorageEnabled(true);
                settings.setJavaScriptEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setAllowContentAccess(true);
                view.setWebChromeClient(this);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
                return false;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooser(uploadMsg, acceptType, "");
            }

            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                Log.d(getClass().getName(), "openFileChooser : "+acceptType+"/"+capture);
                mUploadMessage = uploadFile;
                imageChooser();
            }

            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                System.out.println("WebViewActivity A>5, OS Version : " + Build.VERSION.SDK_INT + "\t onSFC(WV,VCUB,FCP), n=3");
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;
                imageChooser();
                return true;
            }

            private void imageChooser() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(getClass().getName(), "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:"+photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if(takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, 1);
            }
        });
        webView.setWebViewClient(new WebViewClient());


        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    private String conversionToAddress(Double latitude, Double longitude, boolean detail) {
        String address = null;
        geocoder = new Geocoder(this, Locale.KOREA);
        try {
            if(geocoder != null) {
                addressList = geocoder.getFromLocation(latitude, longitude, 1);

                if(detail) {
                    if(addressList != null && addressList.size() > 0) {
                        address = addressList.get(0).getAddressLine(0);
                    }
                } else {
                    address = addressList.get(0).getCountryName() + " " + addressList.get(0).getAdminArea() + " " + addressList.get(0).getLocality() + " " + addressList.get(0).getThoroughfare();
                    if(addressList.get(0).getCountryName().equals("대한민국")) {
                        address = address.substring(5);
                    }
                    if(address.contains("null")){
                        address = address.replace("null", "");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    private void sortArray(ArrayList arrayList) {
        Collections.sort(arrayList, new Comparator<jsonData>() {
            @Override
            public int compare(jsonData o1, jsonData o2) {
                int result;
                if(o1.getDistance() < o2.getDistance()) {
                    result = -1;
                } else if(o1.getDistance() == o2.getDistance()) {
                    result = 0;
                } else {
                    result = 1;
                }
                return result;
            }
        });
    }

    private boolean isConnected(final String _url) {
        final boolean[] isConnected = new boolean[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(_url);
                    Log.d("isConnected Test", "1");
                    HttpURLConnection urlConnection = urlConnection = (HttpURLConnection) url.openConnection();;
                    Log.d("isConnected Test", "2");
                    urlConnection.setConnectTimeout(2000);
                    Log.d("isConnected Test", "3");
                    urlConnection.connect();
                    Log.d("isConnected Test", "4");

                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        Log.d("isConnected Test", "5");
                        isConnected[0] = true;
                    } else {
                        Log.d("isConnected Test", "6");
                        isConnected[0] = false;
                    }
                } catch (SocketTimeoutException e) {
                     e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("isConnected Test", "7");
        Log.d("isConnected Test", "iscConnected[] : " + isConnected[0]);
        return isConnected[0];
    }

    private void jsonQuery(String data) {
        jsonDataArrayList = new ArrayList<>();
        try {
            jsonArray = new JSONArray(data);

            for(int i=0; i<jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Bitmap image = bitmapImage;
                String img_url = jsonObject.getString("img_url");
                urlImage = url + img_url;
                ImageTask imageTask = new ImageTask(urlImage, image);
                imageTask.execute();
                int id = jsonObject.getInt("id");
                String company = jsonObject.getString("company");
                Double latitude = jsonObject.getDouble("latitude");
                Double longitude = jsonObject.getDouble("longitude");
                String area = conversionToAddress(latitude, longitude, false);
                String classification = jsonObject.getString("classification");
                String info = jsonObject.getString("info");
                String date = jsonObject.getString("date");
                String detail_address = conversionToAddress(latitude, longitude, true);
                String detail_info = jsonObject.getString("detail_info");
                String company_info = jsonObject.getString("company_info");
                String company_tel = jsonObject.getString("company_tel");
                Location objectLocation = new Location("object");
                objectLocation.setLatitude(latitude);
                objectLocation.setLongitude(longitude);
                int distance = (int) currentLocation.distanceTo(objectLocation);
                Log.d("DISTANCE", "currentLocation : " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
                Log.d("DISTANCE", "objectLocation : " + objectLocation.getLatitude() + ", " + objectLocation.getLongitude());
                Log.d("DISTANCE", "" + distance);

                image = bitmapImage;
                jsonDataArrayList.add(new jsonData(id, company, latitude, longitude, area, classification, info, date, detail_address, detail_info, company_info, company_tel, distance, image));
            }
            sortArray(jsonDataArrayList);
            staticJsonDataArrayList = jsonDataArrayList;
            Log.d("TESTTEST", "jsonQuery 1");
            customAdapter = new CustomAdapter(getApplicationContext(), jsonDataArrayList);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TESTTEST", "jsonQuery 2");
                    listView.setAdapter(customAdapter);
                    Log.d("TESTTEST", "jsonQuery 3");
                    customAdapter.notifyDataSetChanged();
                    Log.d("TESTTEST", "jsonQuery 4");
                    if(progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ImageTask extends AsyncTask<Void, Integer, Bitmap> {
        private String url;
        private Bitmap _bitmap;
        HttpURLConnection conn;

        public ImageTask(String url, Bitmap bitmap) {
            this.url = url;
            _bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Log.d("ImageTask", "onPreExecute 2-1");
                URL _url = new URL(url);
                Log.d("ImageTask", "onPreExecute 3");
                conn = (HttpURLConnection) _url.openConnection();
                Log.d("ImageTask", "onPreExecute 4");
                conn.setDoInput(true);
                if(conn != null) {
                    conn.connect();
                    Log.d("ImageTask", "onPreExecute 4-1");
                }
                Log.d("ImageTask", "onPreExecute 6");

                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                bitmapImage = bitmap;
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d("이미지 다운로드", "이미지 다운로드 성공");
            _bitmap = bitmapImage;
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private ContentValues values;
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TESTTEST", "NetworkTask onPreExecute 1");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("데이터 다운로드 중");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
            result = requestHttpConnection.request(url, values);
            data = result;
            Log.d("데이터 다운로드", "결과 : " + result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 데이터 받은 후 공유 프리퍼런스에 저장
            Log.d("데이터 다운로드", "다운로드 완료");
            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("JSON", data);
            editor.putBoolean("DATA", true);
            editor.commit();
            jsonQuery(data);
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "서버로부터 데이터를 받았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
