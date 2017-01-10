package moco.schleppo.fragments;import android.Manifest;import android.app.Dialog;import android.app.Fragment;import android.content.Context;import android.content.Intent;import android.content.pm.PackageManager;import android.graphics.Bitmap;import android.graphics.Color;import android.location.Location;import android.location.LocationListener;import android.location.LocationManager;import android.net.Uri;import android.os.AsyncTask;import android.os.Build;import android.os.Bundle;import android.support.annotation.NonNull;import android.support.annotation.Nullable;import android.support.annotation.RequiresApi;import android.support.v4.app.ActivityCompat;import android.support.v4.content.ContextCompat;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.view.Window;import android.widget.Button;import android.widget.CheckBox;import android.widget.ImageButton;import android.widget.Toast;import com.google.android.gms.analytics.HitBuilders;import com.google.android.gms.common.ConnectionResult;import com.google.android.gms.common.api.GoogleApiClient;import com.google.android.gms.location.LocationRequest;import com.google.android.gms.location.LocationServices;import com.google.android.gms.maps.CameraUpdateFactory;import com.google.android.gms.maps.GoogleMap;import com.google.android.gms.maps.MapFragment;import com.google.android.gms.maps.OnMapReadyCallback;import com.google.android.gms.maps.model.BitmapDescriptorFactory;import com.google.android.gms.maps.model.Circle;import com.google.android.gms.maps.model.CircleOptions;import com.google.android.gms.maps.model.LatLng;import com.google.android.gms.maps.model.Marker;import com.google.android.gms.maps.model.MarkerOptions;import com.parse.FindCallback;import com.parse.ParseException;import com.parse.ParseObject;import com.parse.ParseQuery;import com.parse.ParseUser;import java.util.ArrayList;import java.util.Arrays;import java.util.Date;import java.util.List;import moco.schleppo.R;import static android.os.Build.VERSION_CODES.M;/** * Created by golive on 29.11.16. */public class MapsFragment extends Fragment implements OnMapReadyCallback,        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,        View.OnClickListener, com.google.android.gms.location.LocationListener {    private GoogleMap mMap;    private LocationManager locationManager;    private LocationListener locationListener;    private GoogleApiClient mGoogleApiClient;    private Location mLastLocation, markerLocation;    private Marker mCurrLocationMarker, markerLocationMarker;    private LocationRequest mLocationRequest, forceUpdateRequester;    //Liste aller Kreise (optionen + gezeichnet)    ArrayList<WarnCircleClass> circleList = new ArrayList<WarnCircleClass>();    // Daten Speicher.    final String database_Warnings = "WarningDriver";    private String file = "locationData", file2 = "Typ", file3 = "Parkplatz", file4 = "id";    private ImageButton bWarning, bMarker, bNavigation;    private boolean geradeGeoeffnet = true;    boolean zoomAnimationRunning = false;    long zoomAnimationDelay = 3000L;    // online speicher    ParseObject warning;    ParseObject parkplatz;    /**     * Hilfsklasse zum Speichern der Warnungen     * Speichert dabei jeweils die optionen eines erstellten Umkreises sowie dem gezeichnetem Objekt     * (nur das gezeichnete objekt kann sich selbst entfernen)     */    private class WarnCircleClass {        Circle circle = null;        CircleOptions options = null;    }    public void init() {        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);        forceUpdateRequester = LocationRequest.create();        forceUpdateRequester.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);        // warnung online speicher        warning = new ParseObject(database_Warnings);        //Parkplatz online speicher        parkplatz = new ParseObject("Parkplatz");        locationListener = new LocationListener() {            @Override            public void onLocationChanged(Location location) {                mLastLocation = location;                if (mCurrLocationMarker != null) {                    mCurrLocationMarker.remove();                }                //Place current location marker                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());                if (markerLocationMarker != null)                    markerLocationMarker.remove();                MarkerOptions markerOptions = new MarkerOptions();                markerOptions.position(latLng);                markerOptions.title("Aktuelle Position");                Bitmap.Config config = Bitmap.Config.ARGB_8888;                //invisible marker                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(1, 1, config)));                mCurrLocationMarker = mMap.addMarker(markerOptions);                if (markerLocation != null) {                    MarkerOptions mMarkerOption = new MarkerOptions();                    mMarkerOption.position(new LatLng(markerLocation.getLatitude(), markerLocation.getLongitude()));                    mMarkerOption.title("Hier geparkt!!");                    mMarkerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));                    markerLocationMarker = mMap.addMarker(mMarkerOption);                }                //move map camera               if (markerOptions != null && geradeGeoeffnet) {                   mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));                   mMap.animateCamera(CameraUpdateFactory.zoomTo(17));                   geradeGeoeffnet = false;               }                //stop location updates                if (mGoogleApiClient != null) {                    stopLocationUpdates();                }                ParseQuery<ParseObject> query = ParseQuery.getQuery(database_Warnings);                ArrayList<CircleOptions> warnungen = new ArrayList<CircleOptions>();                if(query != null) {                    query.selectKeys(Arrays.asList(file, file2));                    try {                        List<ParseObject> objects = query.find();                        for(ParseObject object : objects) {                            if(object != null) {                                String s = object.fetchIfNeeded().getString(file);                                if (s != null) {                                    Location l = loadLocation(s);                                    if (l != null) { //fügt aktuelle warnungen hinzu                                        CircleOptions warnung = new CircleOptions();                                        warnung.center(new LatLng(l.getLatitude(), l.getLongitude()));                                        warnung.clickable(true);                                        warnung.radius(1000);                                        warnung.strokeColor(Color.WHITE);                                        warnung.fillColor(Color.parseColor("#32FF0000")); //20% transparentes rot                                        warnung.strokeWidth(2);                                        warnungen.add(warnung);                                    }                                }                            }                        }                    } catch (Exception e) {                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();                    }                }                for(int i = 0;i < circleList.size();i++) { //durchläuft alle alten warnungen                    CircleOptions c = circleList.get(i).options;                    boolean found = false;                    for(CircleOptions h : warnungen) { //kontrolliert ob die alte warnung immernoch aktuell ist                        if(c.equals(h)) {                            found = true;                            warnungen.remove(c); //entfernt alte Warnung von Liste der neuen warnungen                        }                    }                    if(!found) { //wenn die alte warnung nicht gefunden wird -> löschen                        Circle circle = circleList.get(i).circle;                        circle.remove();                        circleList.remove(i);                    }                }                for(CircleOptions c : warnungen) { //fügt neue warnungen hinzu                    Circle circle = mMap.addCircle(c);                    WarnCircleClass object = new WarnCircleClass();                    object.circle = circle;                    object.options = c;                    circleList.add(object);                }            }            @Override            public void onStatusChanged(String s, int i, Bundle bundle) {            }            @Override            public void onProviderEnabled(String s) {            }            @Override            public void onProviderDisabled(String s) {            }        };        if (checkLocationPermission())            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);    }    /**     * Hilfsmethode des LocationListeners     */    private void stopLocationUpdates() {        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);    }    /**     * Hilfsmethode zum animierten zoomen     */    private void animatedZooming(Location marker) {        if (marker == null)            return; //ohne marker kein zoomen möglich        LatLng lng = new LatLng(marker.getLatitude(), marker.getLongitude());        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 17));        mMap.animateCamera(CameraUpdateFactory.zoomIn());        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), (int) zoomAnimationDelay, null);        zoomAnimationRunning = true;        bWarning.postDelayed(new Runnable() {            @Override            public void run() {                zoomAnimationRunning = false;            }        }, zoomAnimationDelay);    }    @Nullable    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);        bWarning = (ImageButton) rootView.findViewById(R.id.bWarning);        bMarker = (ImageButton) rootView.findViewById(R.id.bMarkieren);        bNavigation = (ImageButton) rootView.findViewById(R.id.bNavigation);        bWarning.setOnClickListener(this);        bMarker.setOnClickListener(this);        bNavigation.setOnClickListener(this);        init();        return rootView;    }    @Override    public void onLocationChanged(Location location) {    }    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)    @Override    public void onViewCreated(View view, Bundle savedInstanceState) {        super.onViewCreated(view, savedInstanceState);       // MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);        mapFragment.getMapAsync(this);        loadMarker();    }    @Override    public void onMapReady(GoogleMap googleMap) {        mMap = googleMap;        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);  //map typ        //Initialize Google Play Services        if (android.os.Build.VERSION.SDK_INT >= M) {            if (ContextCompat.checkSelfPermission(getActivity(),                    Manifest.permission.ACCESS_FINE_LOCATION)                    == PackageManager.PERMISSION_GRANTED) {                buildGoogleApiClient();                mMap.setMyLocationEnabled(true);            }        } else {            buildGoogleApiClient();            mMap.setMyLocationEnabled(true);        }    }    protected synchronized void buildGoogleApiClient() {        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())                .addConnectionCallbacks(this)                .addOnConnectionFailedListener(this)                .addApi(LocationServices.API)                .build();        mGoogleApiClient.connect();    }    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;    public boolean checkLocationPermission() {        if (ContextCompat.checkSelfPermission(getActivity(),                Manifest.permission.ACCESS_FINE_LOCATION)                != PackageManager.PERMISSION_GRANTED) {            // Asking user if explanation is needed            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),                    Manifest.permission.ACCESS_FINE_LOCATION)) {                //Prompt the user once explanation has been shown                ActivityCompat.requestPermissions(getActivity(),                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},                        MY_PERMISSIONS_REQUEST_LOCATION);            } else {                // Keine Erklärung erforderlich.                ActivityCompat.requestPermissions(getActivity(),                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},                        MY_PERMISSIONS_REQUEST_LOCATION);            }            return false;        } else {            return true;        }    }    @Override    public void onConnected(Bundle bundle) {        mLocationRequest = new LocationRequest();        mLocationRequest.setInterval(10 * 1000);    // 10 seconds, in milliseconds        mLocationRequest.setFastestInterval(1000);  // 1 seconds, in milliseconds        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);        if (ContextCompat.checkSelfPermission(getActivity(),                Manifest.permission.ACCESS_FINE_LOCATION)                == PackageManager.PERMISSION_GRANTED) {            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,                    this);        }    }    @Override    public void onConnectionSuspended(int i) {    }    @Override    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {    }    @Override    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {        switch (requestCode) {            case MY_PERMISSIONS_REQUEST_LOCATION: {                // If request is cancelled, the result arrays are empty.                if (grantResults.length > 0                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {                    // permission was granted. Do the                    // contacts-related task you need to do.                    if ((ContextCompat.checkSelfPermission(getActivity(),                            Manifest.permission.ACCESS_FINE_LOCATION)                            == PackageManager.PERMISSION_GRANTED) &&                            (ContextCompat.checkSelfPermission(getActivity(),                            Manifest.permission.ACCESS_COARSE_LOCATION)                            == PackageManager.PERMISSION_GRANTED)) {                        if (mGoogleApiClient == null) {                            buildGoogleApiClient();                        }                        mMap.setMyLocationEnabled(true);                    }                } else {                    // Permission denied, Deaktivieren Sie die Funktionalität,                    // die von der getActivity-Berechtigung abhängt.                    Toast.makeText(getActivity(), "Erlaubnis verweigert", Toast.LENGTH_LONG).show();                }                return;            }        }    }    private boolean isParkingSpotmarked = false;    @Override    public void onClick(View v) {        if (mLastLocation == null)            return; //ohne Location -> keine Warnung/Markierung möglich        if (v.getId() == bMarker.getId()) {            //Marker            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),                    mLastLocation.getLongitude())));            if (!isParkingSpotmarked) {                markerLocation = mLastLocation;                bMarker.setImageDrawable(getResources().getDrawable(R.drawable.parking_marked));                saveLocation(getLocationString(mLastLocation));                bNavigation.setVisibility(View.VISIBLE);                isParkingSpotmarked = true;                //da Bewegung minimal -> minimale Abweichungen der Animation                // -> keine notwendige neue Animation                if (!zoomAnimationRunning)                    animatedZooming(markerLocation);            } else {                markerLocation = null;                bMarker.setImageDrawable(getResources().getDrawable(R.drawable.parking_unmarked));                bNavigation.setVisibility(View.INVISIBLE);                isParkingSpotmarked = false;                //saveLocation(null);            }        } else if (v.getId() == bWarning.getId()) {            //Warning            showWarnDialog();        } else if(v.getId() == bNavigation.getId()) {            Intent navigationsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getMapsString()));            navigationsIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");            startActivity(navigationsIntent);        }    }    /**     * Hilfsmethode um Maps link zu generieren     */    private String getMapsString() {        return ("http://maps.google.com/maps?saddr="+getLocationString(mLastLocation)                +"&daddr="+getLocationString(markerLocation));    }    /**     * Hilfsklasse zum temporären Speichern des Warntyps     */    private class TypObject {        boolean abschlepp = false, ordnung = false;        @Override        public String toString() {            if(abschlepp && ordnung)                return getString(R.string.abschleppdienst) + " & " + getString(R.string.ordnungsamt);            else if(abschlepp)                return getString(R.string.abschleppdienst);            else if(ordnung)                return getString(R.string.ordnungsamt);            else                return null;        }    }    /**     * Hilfsmethode zum Anzeigen eines Warndialogfensters zur Auswahl gegebener Optionen     */    private void showWarnDialog() {        final Dialog warnDialog = new Dialog(getActivity());        warnDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);        warnDialog.setContentView(R.layout.warndialoglayout);        warnDialog.setCancelable(true);        final CheckBox checkTowing = (CheckBox) warnDialog.findViewById(R.id.warndialoglayout_abschleppdienst);        final CheckBox checkOffice = (CheckBox) warnDialog.findViewById(R.id.warndialoglayuot_ordnungsamt);        final TypObject o = new TypObject();        //Aufrufe        warnDialog.findViewById(R.id.warndialoglayout_abbrechen)                .setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                warnDialog.cancel();            }        });        warnDialog.findViewById(R.id.warndialoglayuot_senden)                .setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {                // senden warnung                // nutze hilfsmethode unter dieser methode für location string                o.abschlepp = checkTowing.isChecked();                o.ordnung = checkOffice.isChecked();                String tmpTyp = o.toString();                if(tmpTyp != null) {                    warning.put(file, getLocationString(mLastLocation));                    warning.put(file2, tmpTyp);                    warning.saveInBackground();                    deleteWarningInXMinutes(warning, 120);                    warning = new ParseObject(database_Warnings); //neues warnobjekt (da altes schon verwendet und nur noch zur löschung genutzt werden kann)                    Toast.makeText(getActivity(), getString(R.string.msg_send_warning), Toast.LENGTH_SHORT).show();                    warnDialog.cancel();                }            }        });        warnDialog.show();    }    private void deleteWarningInXMinutes (final ParseObject warningToDelete, final int minutes) {        AsyncTask<String, Integer, Boolean> asyncTask = new AsyncTask<String, Integer, Boolean>() {            @Override            protected Boolean doInBackground(String[] params) {                Date now = new Date();                Date timeToDelete = new Date();                timeToDelete.setMinutes(timeToDelete.getMinutes() + minutes);                while (now.before(timeToDelete)) {                    try {                        Thread.sleep(60000);                    } catch (Exception e) {                        Log.d("DeleteWarningInXMinutes", e.getMessage());                        return false;                    }                    now = new Date();                }                warningToDelete.deleteInBackground();                return true;            }        };        asyncTask.execute();    }    /**     * Hilfsmethode zum Speichern der Location (OFFLINE/LOCAL)     */    private void saveLocation(String input) {        if(input == null) { //null -> löschen            parkplatz.put(file4, UserManagement.parseUser);            parkplatz.deleteInBackground();            parkplatz = new ParseObject("Parkplatz");        } else {            parkplatz.put(file3, input); //location            parkplatz.put(file4, UserManagement.parseUser); //user id zum wiederfinden/zur eindeutigen identifizierung            parkplatz.saveInBackground();        }    }    /**     * Hilfsmethode zum Laden der Location (OFFLINE)     */    private Location loadLocation(String input) {        if(input == null)            return null;        String[] lstring = input.split(",");        Location returnLocation = new Location((String) null);        try {            returnLocation.setLatitude(Double.parseDouble(lstring[0]));            returnLocation.setLongitude(Double.parseDouble(lstring[1]));        } catch(Exception e) {            Toast.makeText(getActivity(), "Fehler beim Laden der lokalen Daten aufgetreten!", Toast.LENGTH_LONG).show();            return null;        }        return returnLocation;    }    /**     * Lödt den Marker des Parkplatzes bei App-Start falls vorhanden     */    private void loadMarker() {        ParseQuery<ParseObject> query = ParseQuery.getQuery("Parkplatz"); //lädt alle variablen der tabelle Parkplatz        query.selectKeys(Arrays.asList("objectId", file3, file4));  //wählt dabei die spalten der location & id aus        query.findInBackground(new FindCallback<ParseObject>() {            @Override            public void done(List<ParseObject> objects, ParseException e) {                if(e != null) {                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();                    return;                }                for(ParseObject p : objects) {                    String location = p.getString(file3);                    String id = p.getParseObject(file4).getObjectId();                    if(id != null) {                        if(id.equals(ParseUser.getCurrentUser().getObjectId())) {                            parkplatz = p;  //setzt aktuellen parkplatz auf gefundenen vorhandenen parkplatz                            markerLocation = loadLocation(location);                            bMarker.setImageDrawable(getResources().getDrawable(R.drawable.parking_marked));                            bNavigation.setVisibility(View.VISIBLE);                            isParkingSpotmarked = true;                        }                    } else                        Toast.makeText(getActivity(), "ID = NULL ?!", Toast.LENGTH_SHORT).show(); //eigentlich unnmögliche situation                }            }        });    }    /**     * Hilfsmethode um die Location als String zurückzugeben     * zur Nutzung der Werte :     * split("/")[0] = latitude     * split("/")[1] = longitude     */    private String getLocationString(Location location) {        return (String.valueOf(location.getLatitude()) + "," +                String.valueOf(location.getLongitude()));    }}