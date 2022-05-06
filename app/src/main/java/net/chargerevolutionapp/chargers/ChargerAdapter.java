package net.chargerevolutionapp.chargers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.chargerevolutionapp.R;

import java.util.ArrayList;

public class ChargerAdapter
        extends RecyclerView.Adapter<ChargerAdapter.ViewHolder>
        implements Filterable {
    // Member variables.
    private ArrayList<Charger> mChargerData;
    private ArrayList<Charger> mChargerDataAll;
    private Context mContext;
    private int lastPosition = -1;

    ChargerAdapter(Context context, ArrayList<Charger> itemsData) {
        this.mChargerData = itemsData;
        this.mChargerDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ChargerAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(ChargerAdapter.ViewHolder holder, int position) {
        // Get current sport.
        Charger currentItem = mChargerData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);


        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        if (mChargerData != null) return mChargerData.size();
        else return 0;
    }


    /**
     * RecycleView filter
     **/
    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Charger> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                if(mChargerDataAll != null){
                    results.count = mChargerDataAll.size();
                } else {
                    results.count = 0;
                }

                results.values = mChargerDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Charger item : mChargerDataAll) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                if(filteredList != null){
                    results.count = filteredList.size();
                } else {
                    results.count = 0;
                }

                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mChargerData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mItemName;
        private TextView mAddress;
        private TextView mConnectors;
        private TextView mMaxPower;

        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mItemName = itemView.findViewById(R.id.itemName);
            mAddress = itemView.findViewById(R.id.address);
            mConnectors = itemView.findViewById(R.id.connectors);
            mMaxPower = itemView.findViewById(R.id.maxPower);

            itemView.findViewById(R.id.chargerDetailsBtn).setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ChargerDetailsActivity.class);
                Log.i("details", "ChargerName");
                Log.i("details", mItemName.getText().toString());
                intent.putExtra("ChargerName", mItemName.getText().toString());

                mContext.startActivity(intent);
                //((ChargingStationListActivity) mContext).updateAlertIcon();
            });
        }

        void bindTo(Charger currentItem) {
            mItemName.setText(currentItem.getName());
            mAddress.setText(currentItem.getAddress());
            mConnectors.setText(currentItem.getConnectorTypes());
            mMaxPower.setText(String.valueOf(currentItem.getMaxPowerInkW()));



            //mPriceText.setText(currentItem.getMaxPowerInkW());
            //mRatingBar.setRating(currentItem.getRatedInfo());

            // Load the images into the ImageView using the Glide library.
            //Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
        }
    }
}
