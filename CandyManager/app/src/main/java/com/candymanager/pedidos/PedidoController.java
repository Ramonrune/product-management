package com.candymanager.pedidos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.candymanager.R;
import com.candymanager.menu.MenuPrincipal;


/**
 * A simple {@link Fragment} subclass.
 */
public class PedidoController extends Fragment {


    public PedidoController() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        ((MenuPrincipal) getActivity())
                .setActionBarTitle("Pedidos");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedido, container, false);
    }

}
