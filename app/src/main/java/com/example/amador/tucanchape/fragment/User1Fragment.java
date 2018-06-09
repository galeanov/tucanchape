package com.example.amador.tucanchape.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.activity.InicioSesionActivity;
import com.example.amador.tucanchape.activity.PerfilActivity;
import com.example.amador.tucanchape.firebaseRealTimeDataBase.FirebaseReferences;
import com.example.amador.tucanchape.model.Empresa;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User1Fragment extends Fragment  implements OnMapReadyCallback {

    private static int PETICION_PERMISO_LOCALIZACION = 101;

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mViewo;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    private FirebaseDatabase mfirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewo = inflater.inflate(R.layout.fragment_user_1, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = mfirebaseDatabase.getReference(FirebaseReferences.EMPRESA_REFERENCE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot: dataSnapshot.getChildren()) { //empresas

                        Empresa empresa = datasnapshot.getValue(Empresa.class);
                    final String llave = datasnapshot.getKey();
                        String nombre = empresa.getNombre();
                        double lat = empresa.getLat();
                        double lng = empresa.getLng();

                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title(nombre)
                            .snippet(llave)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.prueba4)));

                    mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            if (marker.getSnippet() !=null){
                                Intent intent = new Intent(getActivity(),PerfilActivity.class);
                                String claveone = marker.getSnippet();
                                intent.putExtra("CLAVEONE",claveone);
                                startActivity(intent);
                            }

                            else {
                                Log.d("Nothing", "no hay nada varon");
                            }

                            return;




                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        return mViewo;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mViewo.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        UiSettings uiSettings = googleMap.getUiSettings();

        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);

        miUbicacion();
    }

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,0,locListener);
        }
    }

    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            AgregarMarcador(lat, lng);
        }
    }

    LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            ActualizarUbicacion(location);
            /*setLocation(location);*/
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

            locationStart();

        }
    };

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled){
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
    }


    private void AgregarMarcador(double lat, double lng) {
            LatLng coordenadas = new LatLng(lat, lng);
            CameraUpdate MiUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 17);
            if (marcador != null) marcador.remove();
            marcador = mGoogleMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title("Mi Ubicación"));

            mGoogleMap.animateCamera(MiUbicacion);
        }

    }














