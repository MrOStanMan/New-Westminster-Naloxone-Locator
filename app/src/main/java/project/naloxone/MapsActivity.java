package project.naloxone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //with training on Nalxone kit use
    private Naloxone LONDON_DRUGS_ADDRESS = new Naloxone("N/A","London Drugs Pharmacy","Community Pharmacies Carrying Naloxone and ProvidingOverdose Response Training in British Columbia", "Pharmacy", "Mon-Sat 8AM-10PM Sun 10AM-8PM", "100–555 6th Street", " V3L 5H1", "604-448-4803", "N/A", "https://www.londondrugs.com/ldhome/","-122.919370","49.212762");
    private Naloxone SAFEWAY_MCBRIDE_ADDRESS = new Naloxone("N/A", "Safeway Pharmacy","Community Pharmacies Carrying Naloxone and ProvidingOverdose Response Training in British Columbia", "Pharmacy", "Mon-Sun 7AM-11PM", "52-800 McBride Boulevard", "V3L 2B8", "604-524-4418", "N/A", "https://www.safeway.ca", "-122.913916","49.221832");
    private Naloxone SAFEWAY_CARNARVON_ADDRESS = new Naloxone("N/A","Safeway Pharmacy","Community Pharmacies Carrying Naloxone and ProvidingOverdose Response Training in British Columbia","Pharmacy", "Mon-Sun 6AM-12AM ", "220-800 Carnarvon Street","V3M 0G3", "604-522-2069", "N/A", "https://www.safeway.ca", "-122.912602","49.201069");


    //Without training on Nalxone kit use
    private Naloxone PHARMASAVE_ADDRESS = new Naloxone("N/A", "Pharmasave", "Community Pharmacies Carrying Naloxone But May Not Provide Training","Pharmacy", "Mon-Fri 9AM-9PM Sat 930AM-6PM Sun 10AM-6PM", "130-1005 Columbia Street", "V3M 6H5","604-525-5607","N/A","http://www.pharmasave.com/","-122.917109","49.200799");
    private Naloxone SAVEONFOODS_ADDRESS = new Naloxone("N/A", "Save-on Foods Pharmacy", "Community Pharmacies Carrying Naloxone But May Not Provide Training", "Pharmacy", "Mon-Sun 7AM-11PM", "270 E Columbia Street", " V3L 0E3", "604-523-2583","N/A","https://www.saveonfoods.com/store/columbia-square/","-122.892454","49.224389");

    //Free Locations Not Included In JSON
    private Naloxone LOWERMAINLANDDRUGFREEDOM_ADDRESS = new Naloxone("N/A", "Lower Mainland Drug Freedom", "Vancouver-based nonprofit organization that specializes in providing information and referral regarding community, government and social services in BC. Our help line services include 211, the Alcohol and Drug Information and Referral Service (ADIRS), the Problem Gambling Help Line, VictimLink BC, and the Youth Against Violence Line.","Nonprofit organization","N/A","25 Blackwood", "V3L 2T3", "604-527-1068", "N/A", "http://redbookonline.bc211.ca/organization/9489527/lower_mainland_drug_freedom_inc", "-122.904818","49.205356" );
    private Naloxone WESTMINSTERMEDICALCLINIC_ADDRESS = new Naloxone("N/A", "Westminister Medical Clinic", "Medical Clinic with medical Supplies", "Clinic", "N/A","7636 6th St","V5N 3M5", "604-777-7095","N/S","","-122.931413","49.222162");
    private Naloxone CONSTRUCTIONINDUSTRYREHABPLAN_ADDRESS = new Naloxone("N/A", "Construction Industry Rehab Plan", "CIRP was founded in the mid 1980s in response to a growing need to provide services to men and women in the construction industry with substance use issues. Since its initial inception the program has evolved and expanded, and today our mission is to provide not only the highest quality of mental health and addiction services for these men and women, but also services that are grounded in evidence and empirically based practices","Non-profit","N/A","402-223 Nelson Crescent", "V2L 0E4","604-521-8611","info@constructionrehabplan.com", "constructionrehabplan.com","-122.892167","49.224000");

    //URL to the Json file.
    private static final String JSON_URL = "http://opendata.newwestcity.ca/downloads/health/HEALTH_MENTAL_HEALTH_AND_ADDICTIONS_SERVICES.json";

    private GoogleMap mMap;

    //Buttom for sending the direction
    FloatingActionButton floatingActionButton;

    double lat = 0;
    double lng = 0;

    //Collection which contains the objects of Free Naloxone data from JSON.
    ArrayList<Naloxone> locations = new ArrayList<Naloxone>();

    ArrayList<Naloxone> freeLocations = new ArrayList<Naloxone>();
    //Collection which contains the objects of static data of kits given with no training
    ArrayList<Naloxone> locationsNoTraining = new ArrayList<Naloxone>();

    //Collection which contains the objects of static data of kits with training on how to use them
    ArrayList<Naloxone> locationsWithTraining = new ArrayList<Naloxone>();

    ArrayList<Naloxone> toBePassed;

    int tabhold;

    private String TAG = MapsActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    //Custom bounce animation
    public void bounceAnimation(View view)
    {
        FloatingActionButton floatingButton = (FloatingActionButton)findViewById(R.id.btnDir);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        floatingButton.startAnimation(myAnim);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_details:
                Intent intent = new Intent(MapsActivity.this,DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("locations", (Serializable)toBePassed);
                intent.putExtras(bundle);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Adding static data to locationNoTraining
        locationsNoTraining.add(LONDON_DRUGS_ADDRESS);
        locationsNoTraining.add(SAFEWAY_MCBRIDE_ADDRESS);
        locationsNoTraining.add(SAFEWAY_CARNARVON_ADDRESS);

        //Adding static data to locationsWithTraining
        locationsWithTraining.add(PHARMASAVE_ADDRESS);
        locationsWithTraining.add(SAVEONFOODS_ADDRESS);
        locationsWithTraining.add(LONDON_DRUGS_ADDRESS);
        locationsWithTraining.add(SAFEWAY_MCBRIDE_ADDRESS);
        locationsWithTraining.add(SAFEWAY_CARNARVON_ADDRESS);


        freeLocations.add(LOWERMAINLANDDRUGFREEDOM_ADDRESS);
        freeLocations.add(WESTMINSTERMEDICALCLINIC_ADDRESS);
        freeLocations.add(CONSTRUCTIONINDUSTRYREHABPLAN_ADDRESS);

        //Setting the Button object to the btnDir :: acitivty_maps
        floatingActionButton = (FloatingActionButton)findViewById(R.id.btnDir);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(MapsActivity.this, R.anim.bounce);
                view.startAnimation(animation);
            }
        });
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        new GetContacts().execute();
        createTabs();
    }

    private class GetCoordinates extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MapsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try {
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s", address);
                response = http.getHTTPData(url);
                return response;
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);

                lat = parseDouble(((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString());

                lng = parseDouble(((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString());

                onMapReady(mMap);
                if (dialog.isShowing())
                    dialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        LatLng coordinate1;

        //Camera Set to London Drugs in New West
        LatLng coordinateCamera = new LatLng(Double.parseDouble(LONDON_DRUGS_ADDRESS.getY()),Double.parseDouble(LONDON_DRUGS_ADDRESS.getX()));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinateCamera, 13), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

                //Here you can take the snapshot or whatever you want
                for(Naloxone i :locations) {
                    LatLng coordinate1 = new LatLng(parseDouble(i.getY()), parseDouble(i.getX()));
                    dropPinEffect( mMap.addMarker(new MarkerOptions().position(coordinate1).title(i.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))));
                }
            }

            @Override
            public void onCancel() {
            }
        });
        toBePassed = locationsWithTraining;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        LatLng p1 = null;
        List<Address> address;
        try {
            address = coder.getFromLocationName(strAddress, 2);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpDataHandler sh = new HttpDataHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(JSON_URL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray locationsJsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < locationsJsonArray.length(); i++) {

                        JSONObject c = locationsJsonArray.getJSONObject(i);
                        String jsonFeatureType = c.getString("json_featuretype");
                        String name = c.getString("Name");
                        String description = c.getString("Description");
                        String category = c.getString("Category");
                        String hours = c.getString("Hours");
                        String location = c.getString("Location");
                        String pc = c.getString("PC");
                        String phone = c.getString("Phone");
                        String email = c.getString("Email");
                        String website = c.getString("Website");
                        String x = c.getString("X");
                        String y = c.getString("Y");


                        Naloxone naloxneData = new Naloxone(jsonFeatureType,name,description,category,hours,location,pc,phone,email,website,x,y);
                        locations.add(naloxneData);
                    }

                }

                catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }
    }
    public void switchTabs(int tab){
        TabHost host = (TabHost) findViewById(R.id.tabhost);

        switch(tab){
            case 0:
                host.setCurrentTab(0);
                break;
            case 1:
                host.setCurrentTab(1);
                break;
            case 2:
                host.setCurrentTab(2);
                break;
            default:
                host.setCurrentTab(0);
                break;
        }
    }

    public void createTabs(){
        TabHost host = (TabHost) findViewById(R.id.tabhost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Free");
        host.addTab(spec);

        spec = host.newTabSpec("Tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Buy");
        host.addTab(spec);

        spec = host.newTabSpec("Tab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Info");
        host.addTab(spec);


        switchTabs(getFocus());

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                LatLng coordinate1;

                switch(tabId) {
                    case "Tab1":
                        mMap.clear();
                        //With training markers set to blue

                        for(Naloxone i :locations) {
                            if(i.getLocation().equals("40 Begbie Street") || i.getLocation().equals("610 Sixth Street")) {
                                freeLocations.add(i);
                            }
                        }

                        for(Naloxone i: freeLocations){
                            coordinate1 = new LatLng(parseDouble(i.getY()), parseDouble(i.getX()));
                            mMap.addMarker(new MarkerOptions().position(coordinate1).title(i.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }
                        toBePassed = freeLocations;
                        break;
                    case "Tab2":
                        mMap.clear();
                        //With training markers set to blue
                        for(Naloxone i :locationsWithTraining) {
                            coordinate1 = new LatLng(parseDouble(i.getY()), parseDouble(i.getX()));
                           mMap.addMarker(new MarkerOptions().position(coordinate1).title(i.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }
                        toBePassed = locationsWithTraining;
                        break;

                    case "Tab3":
                        mMap.clear();
                        //With training markers set to blue
                        for(Naloxone i :locations) {
                            coordinate1 = new LatLng(parseDouble(i.getY()), parseDouble(i.getX()));
                           mMap.addMarker(new MarkerOptions().position(coordinate1).title(i.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }
                        toBePassed = locations;
                        break;

                    default:

                        break;
                }
            }
        });
    }
    public int getFocus(){
        Intent intent = getIntent();
        int focus = intent.getIntExtra("tab", 1);
        return focus;
    }





    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15);
                } else {
                    marker.showInfoWindow();

                }
            }
        });
    }


}
