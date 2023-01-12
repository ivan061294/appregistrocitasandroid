package com.app.proyecto_citamedica;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.app.proyecto_citamedica.adapter.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Entidades.SliderItem;
import Entidades.util;

public class inicioapp extends Fragment {


    private TextView tvNombre,tvCorreo,gvCategorias,TextoAdicional,rcvPlatillosRecomendados;
    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;
    String nombre,apellido,dni;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_inicioapp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initAdapter();
        loadData();
    }
    private void init(View v){
        svCarrusel = v.findViewById(R.id.svCarrusel);
        ViewModelProvider vmp = new ViewModelProvider(this);
        //Categorías
        gvCategorias = v.findViewById(R.id.gvCategorias);
        //Platillos
        rcvPlatillosRecomendados = v.findViewById(R.id.rcvPlatillosRecomendados);
        TextoAdicional=v.findViewById(R.id.textoAdicional);
        tvNombre=v.findViewById(R.id.tvNombre);
        tvCorreo=v.findViewById(R.id.tvCorreo);
        apellido=util.apellido;
        nombre=util.nombre;
        dni=util.dni;
        tvNombre.setText(nombre+" "+apellido);
        tvCorreo.setText(dni);
    }
    private void initAdapter() {
        //Carrusel de Imágenes
        sliderAdapter = new SliderAdapter(getContext());
        svCarrusel.setSliderAdapter(sliderAdapter);
        svCarrusel.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        svCarrusel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        svCarrusel.setIndicatorSelectedColor(Color.WHITE);
        svCarrusel.setIndicatorUnselectedColor(Color.GRAY);
        svCarrusel.setScrollTimeInSec(4); //set scroll delay in seconds :
        svCarrusel.startAutoCycle();

    }
    private void loadData() {

        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem(R.drawable.slider1, "Los Mejores Atencion"));
        lista.add(new SliderItem(R.drawable.slider2, "Los Mejores Compromisos"));
        lista.add(new SliderItem(R.drawable.slider3, "Los Mejores Medicos"));
        lista.add(new SliderItem(R.drawable.slider4, "Los Mayor Rapidez En Atencion"));
        sliderAdapter.updateItem(lista);
       /* categoriaViewModel.listarCategoriasActivas().observe(getViewLifecycleOwner(), response -> {
            if(response.getRpta() == 1){
                categoriaAdapter.clear();
                categoriaAdapter.addAll(response.getBody());
                categoriaAdapter.notifyDataSetChanged();
            }else{
                System.out.println("Error al obtener las categorías activas");
            }
        });
        platilloViewModel.listarPlatillosRecomendados().observe(getViewLifecycleOwner(), response -> {
            adapter.updateItems(response.getBody());
        });*/

    }



}