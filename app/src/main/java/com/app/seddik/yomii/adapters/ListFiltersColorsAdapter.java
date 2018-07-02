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

import java.util.ArrayList;

import com.app.seddik.yomii.models.PopulairCategoriesHorizontalItems;
import com.app.seddik.yomii.utils.filter.GPUImageFilterTools;


public class ListFiltersColorsAdapter extends RecyclerView.Adapter<ListFiltersColorsAdapter.ViewHolder> {
    private ArrayList<PopulairCategoriesHorizontalItems> List;
    private Context context;


    public ListFiltersColorsAdapter(Context context, ArrayList<PopulairCategoriesHorizontalItems> List) {
        this.List = List;
        this.context = context;
    }

    @Override
    public ListFiltersColorsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filters_colors_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListFiltersColorsAdapter.ViewHolder viewHolder, final int i) {


        viewHolder.changed_img.setImageResource(List.get(i).getImage());
        viewHolder.title.setText(List.get(i).getTitle());

        viewHolder.title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(i) {
                    case 0:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.CONTRAST),1,0,context);

                        break;
                    case 1:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.GAMMA),1,1,context);
                        break;
                    case 2:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.GaussianBlur),1,2,context);
                        break;
                    case 3:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.BRIGHTNESS),1,3,context);
                        break;
                    case 4:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.SEPIA),1,4,context);
                        break;
                    case 5:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.SATURATION),1,5,context);
                        break;
                    case 6:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.EXPOSURE),1,6,context);
                        break;
                    case 7:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.VIGNETTE),1,7,context);
                        break;
                    case 8:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.WHITE_BALANCE),1,8,context);
                        break;
                    case 9:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.HIGHLIGHT_SHADOW),1,9,context);
                        break;
                    case 10:
                        FiltersPhotoActivity.changeFilter(GPUImageFilterTools.createFilterForType(context,
                                GPUImageFilterTools.FilterType.Temperature),1,10,context);
                        break;

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView changed_img;
        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            changed_img = (ImageView) view.findViewById(R.id.changed_img);
        }
    }

}