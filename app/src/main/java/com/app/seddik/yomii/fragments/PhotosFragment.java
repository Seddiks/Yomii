package com.app.seddik.yomii.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.seddik.yomii.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import com.app.seddik.yomii.adapters.GalleryPhotosAdapter;
import com.app.seddik.yomii.models.GalleryPhotosItems;
import com.app.seddik.yomii.models.ResponsePhotoItems;
import com.app.seddik.yomii.models.UserItems;
import com.app.seddik.yomii.networks.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.app.seddik.yomii.utils.SessionManager;

import static com.app.seddik.yomii.config.AppConfig.URL_UPLOAD_PHOTOS;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {
    private SessionManager session;
    EventBus bus = EventBus.getDefault();

    private  ArrayList<ResponsePhotoItems.Paths> paths;
    GalleryPhotosAdapter adapter;
    ArrayList<GalleryPhotosItems> galleryPhotosItemses;
    RecyclerView recyclerView;
    GridLayoutManager mGridLayoutManager;
    private ProgressDialog pDialog;

    private final Integer image_ids[] = {
            R.drawable.bgvenise,
            R.drawable.bg_paris,
            R.drawable.bg_london,
            R.drawable.bg_moscow,
            R.drawable.bg_madrid,
            R.drawable.bg_munich,
            R.drawable.bg_barca,
            R.drawable.bg_ny,
    };



    public PhotosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        session = new SessionManager(getActivity());
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setFocusable(false);

        galleryPhotosItemses = getPathPhotosPublished();
        adapter = new GalleryPhotosAdapter(getActivity(), galleryPhotosItemses);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return rootView;
    }

    private ArrayList<GalleryPhotosItems> getPathPhotosPublished(){
        pDialog.setMessage("Chargement en cours ...");
        showDialog();

        final ArrayList<GalleryPhotosItems> theimage = new ArrayList<>();
        session = new SessionManager(getActivity());
        UserItems user ;
        user = session.getUserDetails();
        int id_user = user.getUser_id();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(URL_UPLOAD_PHOTOS).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ApiService API = retrofit.create(ApiService.class);
        Call<ResponsePhotoItems> api =API.getUserPathPhotosPublished(1,id_user);
        api.enqueue(new Callback<ResponsePhotoItems>() {
            @Override
            public void onResponse(Call<ResponsePhotoItems> call, Response<ResponsePhotoItems> response) {
                hideDialog();
                ResponsePhotoItems List = response.body();
                boolean success = List.getSuccess();
                String message = List.getMessage();
                if (success){
                    paths = List.getData();
                    int parent;

                    for(int i=0; i< paths.size(); i++){
                        parent = paths.get(i).getParent();
                        if (parent == -1 || parent == 0){
                            GalleryPhotosItems galleryPhotosItems = new GalleryPhotosItems();
                            galleryPhotosItems.setParent(parent);
                            galleryPhotosItems.setPhoto_id(paths.get(i).getPhoto_id());
                            galleryPhotosItems.setPhoto_path(paths.get(i).getPhoto_path());
                            galleryPhotosItems.setParent(parent);
                            theimage.add(galleryPhotosItems);

                        }
                    }
                    adapter = new GalleryPhotosAdapter(getActivity(), theimage);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //Send data to Albums fragment by BUS event
                    bus.post(new ResponsePhotoItems(List.getData()));


                }else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponsePhotoItems> call, Throwable t) {
                hideDialog();
                Toast.makeText(getActivity(),
                        "Error", Toast.LENGTH_LONG)
                        .show();

            }
        });
        return theimage;

    }


    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onPause() {
        super.onPause();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
