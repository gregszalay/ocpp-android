package net.chargerevolutionapp.chargers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import net.chargerevolutionapp.R;
import net.chargerevolutionapp.profiles.UserProfile;
import net.chargerevolutionapp.profiles.UserProfileRepository;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChargerAdapter
        extends RecyclerView.Adapter<ChargerAdapter.ViewHolder>
        implements Filterable {

    private static final String LOG_TAG = ChargerAdapter.class.getName();

    // Member variables.
    private final List<Charger> mChargerData;
    private final List<Charger> mChargerDataAll;
    private final Context mContext;
    private int lastPosition = -1;

    ChargerAdapter(Context context, List<Charger> itemsData) {
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

    @Override
    public Filter getFilter() {
        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private final TextView mItemName;
        private final TextView mAddress;
        private final TextView mConnectors;
        private final TextView mMaxPower;
        private final Button chargerDetailsBtn;
        private final TextView reservedNotice;
        private final ChargerRepository chargerRepository;
        private final UserProfileRepository userProfileRepository;

        ViewHolder(View itemView) {
            super(itemView);

            chargerRepository = new ChargerRepository();

            // Initialize the views.
            mItemName = itemView.findViewById(R.id.itemName);
            mAddress = itemView.findViewById(R.id.address);
            mConnectors = itemView.findViewById(R.id.connectors);
            mMaxPower = itemView.findViewById(R.id.maxPower);
            chargerDetailsBtn = itemView.findViewById(R.id.chargerDetailsBtn);
            reservedNotice = itemView.findViewById(R.id.reservedNotice);
            reservedNotice.setVisibility(View.GONE);

            userProfileRepository = new UserProfileRepository();

            itemView.findViewById(R.id.chargerDetailsBtn).setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ChargerDetailsActivity.class);
                Log.i("details", "ChargerName");
                Log.i("details", mItemName.getText().toString());
                intent.putExtra("ChargerName", mItemName.getText().toString());
                mContext.startActivity(intent);
            });

        }

        void bindTo(Charger currentItem) {
            mItemName.setText(currentItem.getName());
            mAddress.setText(currentItem.getAddress());
            mConnectors.setText(currentItem.getConnectorTypes());
            mMaxPower.setText(String.valueOf(currentItem.getMaxPowerInkW()));

            if (currentItem.getReservedUntil() > System.currentTimeMillis()) {
                reservedNotice.setVisibility(View.VISIBLE);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
                String formattedDate = fmtOut.format(new Date(currentItem.getReservedUntil()));
                reservedNotice.setText("Lefoglalva " + formattedDate + "-ig");
                if (!Objects.equals(
                        Objects.requireNonNull(
                                FirebaseAuth.getInstance().getCurrentUser()).getEmail(),
                        currentItem.getReservedByUserEmail()
                )) {
                    this.chargerDetailsBtn.setEnabled(false);
                }
            }

        }
    }
}
