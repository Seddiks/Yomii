package com.app.seddik.yomii.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.seddik.yomii.R;
import com.app.seddik.yomii.adapters.GalleryAlbumsAdapter;
import com.app.seddik.yomii.models.GalleryAlbumsItems;
import com.app.seddik.yomii.models.ResponsePhotoItems;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment {
    private GridLayoutManager mGridLayoutManager;
    private GalleryAlbumsAdapter adapter;
    private RecyclerView recyclerView;
    private EventBus bus = EventBus.getDefault();




    public AlbumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_albums, container, false);
        bus.register(this);

        recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        // recyclerView.setFocusable(false);

        return rootView;
    }

    //Catch Event from fragment Photos
    @Subscribe
    public void onEvent(ResponsePhotoItems event) {
        fillAlbums(event);


    }

    public void fillAlbums(ResponsePhotoItems items){
        ArrayList<GalleryAlbumsItems> albums = new ArrayList<>();
        GalleryAlbumsItems galleryPhotosItems;
        GalleryAlbumsItems.Paths PathsAlbum ;
        ArrayList<GalleryAlbumsItems.Paths> ListPathsAlbum ;

        boolean isFoundCity;

        for(int i=0; i < items.getData().size(); i++){
            isFoundCity = false;
            if (albums.size()> 0){
                for(int j=0; j<albums.size(); j++){
                    if (albums.get(j).getTitle().equals(items.getData2().get(i).getCity())) {
                        isFoundCity = true;

                        GalleryAlbumsItems.Paths path = new GalleryAlbumsItems.Paths();
                        path.setPhoto_path(items.getData2().get(i).getPhoto_path());
                        albums.get(j).getData().add(path);

                    }
                }
            }

            if (isFoundCity == false || albums.size()==0){
                galleryPhotosItems = new GalleryAlbumsItems();
                ListPathsAlbum = new ArrayList<>();
                PathsAlbum = new GalleryAlbumsItems.Paths();
                galleryPhotosItems.setTitle(items.getData2().get(i).getCity());
                PathsAlbum.setPhoto_path(items.getData2().get(i).getPhoto_path());
                ListPathsAlbum.add(PathsAlbum);
                galleryPhotosItems.setData(ListPathsAlbum);
                albums.add(galleryPhotosItems);

            }

        }
        adapter = new GalleryAlbumsAdapter(getActivity(), albums);
        recyclerView.setAdapter(adapter);

    }


    }
