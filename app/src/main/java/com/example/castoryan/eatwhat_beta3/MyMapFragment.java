package com.example.castoryan.eatwhat_beta3;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MyMapFragment extends Fragment{
    // ...
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    LatLng res_lng;
    private GoogleMap map;
    LocationManager locationManager;
    Location mylocation = null;
    Marker maker_me;
    Marker maker_res;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_map, null, false);
        Bundle bundle1 = getArguments();
        String lon = bundle1.getString("lon");
        String lat = bundle1.getString("lat");
        res_lng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        //getActivity().setContentView(R.layout.fragment_my_map);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        FragmentManager fr = getChildFragmentManager();
        Fragment kk = fr.findFragmentById(R.id.mapf);
        SupportMapFragment mapaa = (SupportMapFragment)kk;
        map =   mapaa.getMap();

        maker_res = map.addMarker(new MarkerOptions().position(res_lng)
                .title("the resturant"));


        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(res_lng, 18));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String myprovider = locationManager.getBestProvider(criteria,true);
        mylocation = locationManager.getLastKnownLocation(myprovider);


        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                mylocation = location;
                LatLng myLat = new LatLng(mylocation.getLatitude(), mylocation.getLongitude());
                if(mylocation != null) {
                    maker_me = map.addMarker(new MarkerOptions()
                            .position(myLat)
                            .title("Me")
                            .snippet("This is where I am")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.ic_launcher)));

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    builder.include(maker_me.getPosition());
                    builder.include(maker_res.getPosition());

                    LatLngBounds bounds = builder.build();

                    int padding = 150; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    map.animateCamera(cu);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(myprovider, 0, 0, locationListener);


    }

}
