package com.example.amador.tucanchape.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.model.Empresa;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class PerfilFragment extends Fragment {

    private final int REQUEST_CODE_ASK_PERMISSIONS = 1992;

    private TextView tel1prof, tel2prof, get_id, dir_prof;
    private Empresa empresa;
    private Button btn_hor;
    private Intent i;
    private ImageView call_send2, call_send;

    public PerfilFragment() {
        // Required empty public constructor
    }

    public static PerfilFragment newInstance(Empresa empresa) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putSerializable("empresa", empresa);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        if (getArguments() != null) {
            empresa = (Empresa) getArguments().getSerializable("empresa");
        }

        tel1prof  = view.findViewById(R.id.tel_1_prof);
        tel2prof  = view.findViewById(R.id.tel_2_prof);
        get_id  = view.findViewById(R.id.get_id);
        dir_prof  = view.findViewById(R.id.dir_prof);
        btn_hor  = view.findViewById(R.id.btn_hor);
        call_send2  = view.findViewById(R.id.call_send2);
        call_send  = view.findViewById(R.id.call_send);

        tel1prof.setText(empresa.getTelefono1());
        tel2prof.setText(empresa.getTelefono2());
        get_id.setText(empresa.getNombre());
        Direction(empresa.getLat(), empresa.getLng());

        btn_hor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                CanchaReservaFragment canchaReservaFragment = CanchaReservaFragment.newInstance(empresa.getIdusuario(), R.id.fragment_container);
                manager.beginTransaction().add(R.id.fragment_container, canchaReservaFragment).commit();
            }
        });

        call_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + empresa.getTelefono1()));
                i.setPackage("com.android.server.telecom");
                requestPermisison();
            }
        });

        call_send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse( "tel:" + empresa.getTelefono2()));
                i.setPackage("com.android.server.telecom");
                requestPermisison();
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startActivity(i);
            else {
                Toast.makeText(getContext(), "Permiso denegado, debe conceder permisos para realizar la llamada", Toast.LENGTH_LONG).show();
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermisison() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasAccountsPermission = getContext().checkSelfPermission(Manifest.permission.CALL_PHONE);
            if (hasAccountsPermission != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{
                        Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_PERMISSIONS);
            else
                startActivity(i);
        } else
            startActivity(i);
    }

    public void Direction(double lat, double lon){
        LatLng company = new LatLng(lat, lon);
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (company.latitude != 0.0 && company.longitude != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        company.latitude, company.longitude, 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    dir_prof.setText(DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
