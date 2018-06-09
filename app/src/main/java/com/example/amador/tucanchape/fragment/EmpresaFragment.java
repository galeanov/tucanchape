package com.example.amador.tucanchape.fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.activity.InicioSesionActivity;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;


public class EmpresaFragment extends Fragment implements OnMapReadyCallback{

    private TextView namEmp, tel1Emp, tel2Emp, dirEmp;
    private EditText etTel1Emp, etTel2Emp, etNamEmp;
    private Button btnPic;
    private ImageView picEmp;
    private DatabaseReference empNodo;
    private GoogleMap mMap;
    private StorageReference mStorage;

    private  static  final int PICK_IMAGE_RQUEST = 1;
    private Uri mImageUri;




    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            imagePath = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
            picEmp.setImageBitmap(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }*/

    public EmpresaFragment() {
        // Required empty public constructor
    }

    public static EmpresaFragment newInstance(/*String param1*/) {

        EmpresaFragment fragment = new EmpresaFragment();

        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);*/
        fragment.setArguments(args);

        return fragment;
    }




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_empresa, container, false);

        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);*/
        }
        SupportMapFragment mapfragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapfragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapfragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map,mapfragment).commit();
        }
        mapfragment.getMapAsync(this);


        namEmp = view.findViewById(R.id.name_empresa);
        tel1Emp = view.findViewById(R.id.tel_1_emp);
        tel2Emp = view.findViewById(R.id.tel_2_emp);
        dirEmp = view.findViewById(R.id.dir_emp);
        etTel1Emp = view.findViewById(R.id.ed_tel_1_emp);
        etTel2Emp = view.findViewById(R.id.ed_tel_2_emp);
        etNamEmp = view.findViewById(R.id.et_name_empresa);
        btnPic = view.findViewById(R.id.btn_up);
        picEmp = view.findViewById(R.id.fotoper);

        mStorage = FirebaseStorage.getInstance().getReference();


        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_RQUEST);

            }
        });



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.child("empresa").getChildren()) { //empresas
                        if(postSnapshot.child("idusuario").getValue(String.class).equals(user.getUid())){
                            empNodo = postSnapshot.getRef();
                            namEmp.setText(postSnapshot.child("nombre").getValue(String.class));
                            tel1Emp.setText(postSnapshot.child("telefono1").getValue(String.class));
                            tel2Emp.setText(postSnapshot.child("telefono2").getValue(String.class));

                            etNamEmp.setText(postSnapshot.child("nombre").getValue(String.class));
                            etTel1Emp.setText(postSnapshot.child("telefono1").getValue(String.class));
                            etTel2Emp.setText(postSnapshot.child("telefono2").getValue(String.class));

                            if(postSnapshot.child("lat").getValue(Double.class)!=0){
                                mMap.clear();
                                Direction(postSnapshot.child("lat").getValue(Double.class), postSnapshot.child("lng").getValue(Double.class));
                            }

                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_RQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(picEmp);


        }
    }

    public void Editar(boolean val){
        namEmp.setVisibility(val?View.GONE:View.VISIBLE);
        tel1Emp.setVisibility(val?View.GONE:View.VISIBLE);
        tel2Emp.setVisibility(val?View.GONE:View.VISIBLE);
        etNamEmp.setVisibility(val?View.VISIBLE:View.GONE);
        etTel1Emp.setVisibility(val?View.VISIBLE:View.GONE);
        etTel2Emp.setVisibility(val?View.VISIBLE:View.GONE);

        if(val) {
            etNamEmp.setText(namEmp.getText().toString());
            etTel1Emp.setText(tel1Emp.getText().toString());
            etTel2Emp.setText(tel2Emp.getText().toString());
        }else {
            namEmp.setText(etNamEmp.getText().toString());
            tel1Emp.setText(etTel1Emp.getText().toString());
            tel2Emp.setText(etTel2Emp.getText().toString());
        }
    }

    public void Salvar(){
        if(!etNamEmp.getText().toString().equals("") && !etTel1Emp.getText().toString().equals("") && !etTel2Emp.getText().toString().equals("")) {
            empNodo.child("nombre").setValue(etNamEmp.getText().toString());
            empNodo.child("telefono1").setValue(etTel1Emp.getText().toString());
            empNodo.child("telefono2").setValue(etTel2Emp.getText().toString());
            Editar(false);
        }else
            Toast.makeText(getContext(), "No debe dejar espacios en blanco", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Direction(-12.0463709, -77.0777737);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                empNodo.child("lat").setValue(latLng.latitude);
                empNodo.child("long").setValue(latLng.longitude);
            }
        });

    }

    public void Direction(double lat, double lon){
        LatLng company = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(company).title(namEmp.getText().toString()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(company, 16));

        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (company.latitude != 0.0 && company.longitude != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        company.latitude, company.longitude, 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    dirEmp.setText(DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
