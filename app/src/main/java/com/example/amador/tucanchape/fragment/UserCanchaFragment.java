package com.example.amador.tucanchape.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.model.Empresa;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserCanchaFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap2;
    private List<Empresa> empresas = new ArrayList<>();
    private Location myLocation = null;
    private GoogleApiClient mGoogleApiClient;

    public UserCanchaFragment() {
        // Required empty public constructor
    }

    private void prepareGeo() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public static UserCanchaFragment newInstance() {
        UserCanchaFragment fragment = new UserCanchaFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_cancha, container, false);

        prepareGeo();
        SupportMapFragment mapfragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapfragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapfragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map2, mapfragment).commit();
        }
        mapfragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap2 = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap2.setMyLocationEnabled(true);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                empresas = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.child("empresa").getChildren()) { //empresas
                    empresas.add(postSnapshot.getValue(Empresa.class));
                }
                for(int i = 0; i<empresas.size(); i++){
                    Marker marker =  mMap2.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                            .position(new LatLng(empresas.get(i).getLat(), empresas.get(i).getLng()))
                            .title(empresas.get(i).getNombre()))
                            ;
                    marker.setTag(i);
                }

                // Add a marker in Sydney and move the camera
                mMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation!=null?myLocation.getLatitude():-12.0463709,myLocation!=null?myLocation.getLongitude():-77.0777737), 18));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Estamos presentando de conexión, le pedimos que lo intente de nuevo", Toast.LENGTH_LONG).show();
            }
        });

        mMap2.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final EditText tvTel1 = new EditText(getContext());
                final EditText tvTel2 = new EditText(getContext());

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                tvTel1.setText("Cel.1: " + empresas.get((Integer) marker.getTag()).getTelefono1());
                tvTel2.setText("Tel.2: " + empresas.get((Integer) marker.getTag()).getTelefono2());

                tvTel1.setLayoutParams(lp);
                tvTel2.setLayoutParams(lp);

                tvTel1.setEnabled(false);
                tvTel2.setEnabled(false);

                tvTel1.setTextColor(getContext().getResources().getColor(R.color.colorAccent));;
                tvTel2.setTextColor(getContext().getResources().getColor(R.color.colorAccent));;

                layout.addView(tvTel1);
                layout.addView(tvTel2);

                new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setView(layout)
                        .setTitle(empresas.get((Integer) marker.getTag()).getNombre())
                        .setNegativeButton("Cancelar", null)// sin listener
                        .setPositiveButton("Ver perfil", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.support.v4.app.FragmentManager manager = getFragmentManager();
                                PerfilFragment perfilFragment = PerfilFragment.newInstance(empresas.get((Integer) marker.getTag()));
                                manager.beginTransaction().add(R.id.fragment_container, perfilFragment).commit();

                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            myLocation = location;
                        }else
                            builAlertMessageNoGps();
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {
        builAlertMessageNoGps();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        builAlertMessageNoGps();
    }

    private void builAlertMessageNoGps() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setMessage("Su Gps esta desabilitado, desea habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("Unussed")final DialogInterface dialog, @SuppressWarnings ("Unussed") final int id){
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, @SuppressWarnings ("Unussed") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert=builder.create();
        alert.show();
    }
}














