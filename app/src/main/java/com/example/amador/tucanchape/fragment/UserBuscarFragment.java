package com.example.amador.tucanchape.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.adapter.SearchAdapter;
import com.example.amador.tucanchape.model.Empresa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserBuscarFragment extends Fragment implements SearchAdapter.OnSearchClick{

    private TextView no_result;
    private EditText search_edit_text;
    private RecyclerView rv_busqueda;
    private List<Empresa> empresas;
    private SearchAdapter adapter;

    public UserBuscarFragment() {
        // Required empty public constructor
    }

    public static UserBuscarFragment newInstance() {
        UserBuscarFragment fragment = new UserBuscarFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_buscar, container, false);

        no_result = view.findViewById(R.id.no_result);
        search_edit_text = view.findViewById(R.id.search_edit_text);
        rv_busqueda = view.findViewById(R.id.rv_busqueda);

        no_result.setVisibility(View.VISIBLE);
        no_result.setText("Ingrese una empresa a buscar");
        rv_busqueda.setVisibility(View.GONE);

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!search_edit_text.getText().toString().isEmpty()) {

                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            empresas = new ArrayList<>();
                            for (DataSnapshot postSnapshot : dataSnapshot.child("empresa").getChildren()) { //empresas
                                if(postSnapshot.getValue(Empresa.class).getNombre().toLowerCase().contains(search_edit_text.getText().toString())){
                                    empresas.add(postSnapshot.getValue(Empresa.class));
                                }
                            }

                            if(empresas.isEmpty()) {
                                no_result.setVisibility(View.VISIBLE);
                                no_result.setText("Empresa no encontrada, intente con otra busqueda");
                                rv_busqueda.setVisibility(View.GONE);
                            }else {
                                adapter = new SearchAdapter(getContext() ,empresas);
                                adapter.setListener(UserBuscarFragment.this);
                                rv_busqueda.setAdapter(adapter);
                                rv_busqueda.setHasFixedSize(true);
                                rv_busqueda.setLayoutManager(new LinearLayoutManager(getContext()));
                                no_result.setVisibility(View.GONE);
                                rv_busqueda.setVisibility(View.VISIBLE);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getContext(), "Estamos presentando de conexión, le pedimos que lo intente de nuevo", Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    no_result.setVisibility(View.VISIBLE);
                    no_result.setText("Ingrese una empresa a buscar");
                    rv_busqueda.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        return view;
    }

    @Override
    public void onSearch(int posi) {
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        CanchaReservaFragment canchaReservaFragment = CanchaReservaFragment.newInstance(empresas.get(posi).getIdusuario(), R.id.fragment_container);
        manager.beginTransaction().add(R.id.fragment_container, canchaReservaFragment).commit();
    }
}
