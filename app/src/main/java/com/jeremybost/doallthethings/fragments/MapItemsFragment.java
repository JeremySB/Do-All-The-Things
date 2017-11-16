package com.jeremybost.doallthethings.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremybost.doallthethings.OnMapAndViewReadyListener;
import com.jeremybost.doallthethings.R;
import com.jeremybost.doallthethings.TodoItemRepository;
import com.jeremybost.doallthethings.models.TodoItem;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapItemsFragment extends Fragment implements OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private OnFragmentInteractionListener mListener;
    private GoogleMap mMap;

    private boolean firstLoadComplete = false;
    private boolean pinsAdded = false;
    private LatLng lastPos;

    public MapItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapItemsFragment newInstance() {
        MapItemsFragment fragment = new MapItemsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firstLoadComplete = false;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_items, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        OnMapAndViewReadyListener l = new OnMapAndViewReadyListener(mapFragment, this);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addPins();
    }

    private void addPins() {
        List<TodoItem> items = TodoItemRepository.getInstance().getItems();

        for(int i = 0; i<items.size(); i++) {
            // Add a marker for each task and move the camera
            if(items.get(i).hasLocation()) {
                LatLng pos = new LatLng(items.get(i).getLatitude(), items.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(pos).title(items.get(i).getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                lastPos = pos;
            }
        }

        if(lastPos != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lastPos));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
                }
            }, 400);
        }


    }
}
