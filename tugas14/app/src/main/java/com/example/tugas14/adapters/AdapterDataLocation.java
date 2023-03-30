package com.example.tugas14.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas14.R;
import com.example.tugas14.models.UserLocationModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterDataLocation extends RecyclerView.Adapter<AdapterDataLocation.HolderData> {
    List<UserLocationModel> listData;
    LayoutInflater inflater;
    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public AdapterDataLocation(Context context , List<UserLocationModel> listData){
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_data_history_location, parent, false);
        return new HolderData(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.titleTextView.setText(String.format("History Location #%d", position + 1));
        if(listData.get(position).Address != null){
            holder.countryTextView.setText(String.format("Country : %s", listData.get(position).Address.getCountryName()));
            holder.addressTextView.setText(String.format("Address : %s", listData.get(position).Address.getAddressLine(0)));
            holder.postalCodeTextView.setText(String.format("Postal Code : %s", listData.get(position).Address.getPostalCode()));
        }
        holder.createdAtTextView.setText(String.format("Get On : %s", DateFor.format(listData.get(position).CreatedAt)));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    public class HolderData  extends  RecyclerView.ViewHolder{
        TextView titleTextView, countryTextView, addressTextView, postalCodeTextView, createdAtTextView;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitle);
            countryTextView = itemView.findViewById(R.id.textCountry);
            addressTextView = itemView.findViewById(R.id.textAddress);
            postalCodeTextView = itemView.findViewById(R.id.textPostalCode);
            createdAtTextView = itemView.findViewById(R.id.textCreatedAt);
        }
    }
}
