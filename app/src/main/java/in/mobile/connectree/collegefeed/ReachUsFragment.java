package in.mobile.connectree.collegefeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReachUsFragment extends Fragment {

    public ReachUsFragment(){}

    private GoogleMap map;
    static final LatLng NIT = new LatLng(15.412517, 73.978095);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reach_us, container, false);

        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        Marker nit = map.addMarker(new MarkerOptions().position(NIT).title("National Institute Of Technology, Goa").icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_launcher)));
        nit.showInfoWindow();

//              .snippet("Kiel is cool")
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_launcher)));

                // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NIT, 5));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        map.setBuildingsEnabled(true);
        map.setMyLocationEnabled(true);

        return rootView;
    }
}
