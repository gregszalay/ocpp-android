package net.chargerevolutionapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChargingStationAdapter
        extends RecyclerView.Adapter<ChargingStationAdapter.ViewHolder>
        implements Filterable {
    // Member variables.
    private ArrayList<ChargingStation> mChargingStationData;
    private ArrayList<ChargingStation> mChargingStationDataAll;
    private Context mContext;
    private int lastPosition = -1;

    ChargingStationAdapter(Context context, ArrayList<ChargingStation> itemsData) {
        this.mChargingStationData = itemsData;
        this.mChargingStationDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ChargingStationAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChargingStationAdapter.ViewHolder holder, int position) {
        // Get current sport.
        ChargingStation currentItem = mChargingStationData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);


        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mChargingStationData.size();
    }


    /**
     * RecycleView filter
     * **/
    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ChargingStation> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mChargingStationDataAll.size();
                results.values = mChargingStationDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(ChargingStation item : mChargingStationDataAll) {
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mChargingStationData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private ImageView mItemImage;
        private RatingBar mRatingBar;

        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mItemImage = itemView.findViewById(R.id.itemImage);
            mRatingBar = itemView.findViewById(R.id.ratingBar);
            mPriceText = itemView.findViewById(R.id.price);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(view -> ((ChargingStationListActivity)mContext).updateAlertIcon());
        }

        void bindTo(ChargingStation currentItem){
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getConnectorTypes());
            mPriceText.setText(currentItem.getMaxPowerInkW());
            //mRatingBar.setRating(currentItem.getRatedInfo());

            // Load the images into the ImageView using the Glide library.
            //Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
        }
    }
}
