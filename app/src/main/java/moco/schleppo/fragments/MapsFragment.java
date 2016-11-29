package moco.schleppo.fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import moco.schleppo.MainActivity;


import moco.schleppo.Manifest;
import moco.schleppo.R;

import static android.view.Gravity.CENTER;



public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private View rootView;
    GoogleMap myMap;
    MapView mMapView;

    private static final LatLng Koeln = new LatLng(50.941278, 6.958281);

    @Override public void onDetach() {
        super.onDetach();
    }

    @Override public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        try {
            rootView = inflater.inflate(R.layout.fragment_map, container, false);
            MapsInitializer.initialize(this.getActivity());
            mMapView = (MapView) rootView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
            Button standortMark = (Button) rootView.findViewById(R.id.standort);

            standortMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(50.941278, 6.958281))
                            .title("Marker"));
                }



            });





        }
        catch (InflateException e){

        }


        return rootView;
    }





    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState);
    }
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override public void onDestroyView() {
        super.onDestroyView();
    }
    @Override public void onMapReady(GoogleMap googleMap) {
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Koeln, 15));

            myMap = googleMap;



            }





        }





    }