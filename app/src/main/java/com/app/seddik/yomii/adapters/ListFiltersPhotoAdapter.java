package com.app.seddik.yomii.adapters;

/**
 * Created by Seddik on 02/01/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.seddik.yomii.activities.FiltersPhotoActivity;
import com.app.seddik.yomii.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import com.app.seddik.yomii.models.PopulairCategoriesHorizontalItems;
import com.app.seddik.yomii.utils.filter.GPUImageFilterTools;

import static com.app.seddik.yomii.R.id.imageView;


public class ListFiltersPhotoAdapter extends RecyclerView.Adapter<ListFiltersPhotoAdapter.ViewHolder> {
    private ArrayList<PopulairCategoriesHorizontalItems> List;
    private ArrayList<PopulairCategoriesHorizontalItems> ListMarkFilter;
    private String pathPhoto;
    private Context context;
    private static final int[] typeMarkFilter = {
            R.drawable.ic_current_filter_photo_off,
            R.drawable.ic_current_filter_photo,
    };




    public ListFiltersPhotoAdapter(Context context, ArrayList<PopulairCategoriesHorizontalItems> List, String pathPhoto,ArrayList<PopulairCategoriesHorizontalItems> ListMarkFilter) {
        this.List = List;
        this.ListMarkFilter = ListMarkFilter;
        this.pathPhoto = pathPhoto;
        this.context = context;
    }


    @Override
    public ListFiltersPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filters_photo_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListFiltersPhotoAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.title.setText(List.get(i).getTitle());
        viewHolder.currentFilter_img.setImageResource(ListMarkFilter.get(i).getMarkFilter());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


        Glide.with(context)
                    .asBitmap()
                    .load(List.get(i).getStream().toByteArray())
                    .into(viewHolder.img);


        viewHolder.img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                switch(i) {
                    case 0:
                        FiltersPhotoActivity.changeFilter(new GPUImageFilter(),0,0,context);

                        break;
                    case 1:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_1977),0,1,context);


                        break;
                    case 2:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_VALENCIA),0,2,context);

                        break;
                    case 3:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_AMARO),0,3,context);

                        break;
                    case 4:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_BRANNAN),0,4,context);

                        break;
                    case 5:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_EARLYBIRD),0,5,context);

                        break;
                    case 6:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_HEFE),0,6,context);

                        break;
                    case 7:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_HUDSON),0,7,context);

                        break;
                    case 8:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_INKWELL),0,8,context);

                        break;
                    case 9:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_LOMO),0,9,context);

                        break;
                    case 10:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_LORDKELVIN),0,10,context);

                        break;
                    case 11:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_NASHVILLE),0,11,context);

                        break;
                    case 12:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_RISE),0,12,context);

                        break;
                    case 13:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_SIERRA),0,13,context);

                        break;
                    case 14:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_SUTRO),0,14,context);

                        break;
                    case 15:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_TOASTER),0,15,context);

                        break;
                    case 16:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_WALDEN),0,16,context);

                        break;
                    case 17:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.I_XPROII),0,17,context);

                        break;

                }

            }
        });
    }

    @Override
    public int getItemCount() {
            return List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private ImageView img;
        private ImageView currentFilter_img;
        private TextView title;
        public ViewHolder(View view) {
            super(view);

            img =  view.findViewById(imageView);
            title =  view.findViewById(R.id.title);
            currentFilter_img = view.findViewById(R.id.currentFilter_img);

        }


    }
    public void swapItems(int pos){
        boolean isFirst = false;
            for(int i = 0; i< this.ListMarkFilter.size(); i++) {
                if (i == pos){
                    for(int j = 0; j< this.ListMarkFilter.size(); j++) {
                        if (this.ListMarkFilter.get(j).getMarkFilter() == R.drawable.ic_current_filter_photo){
                            isFirst = true;
                            this.ListMarkFilter.get(j).setMarkFilter(R.drawable.ic_current_filter_photo_off);
                            notifyItemChanged(j);
                            this.ListMarkFilter.get(i).setMarkFilter(R.drawable.ic_current_filter_photo);
                            notifyItemChanged(pos);

                        }
                    }

                }
             }
             if (!isFirst) {
                 this.ListMarkFilter.get(pos).setMarkFilter(R.drawable.ic_current_filter_photo);
                 notifyItemChanged(pos);

             }

        }

}