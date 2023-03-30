package com.example.tugas15.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas15.R;
import com.example.tugas15.models.UserSessionModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterDataSession extends RecyclerView.Adapter<AdapterDataSession.HolderData> {
    List<UserSessionModel> listData;
    LayoutInflater inflater;
    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public AdapterDataSession(Context context , List<UserSessionModel> listData){
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_data_history_session, parent, false);
        return new HolderData(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.titleTextView.setText(String.format("History #%d", position + 1));
        if(listData.get(position).Login != null){
            holder.loginTextView.setText(String.format("Login  : %s", DateFor.format(listData.get(position).Login)));
        }
        if(listData.get(position).Logout != null){
            holder.logoutTextView.setText(String.format("Logout : %s", DateFor.format(listData.get(position).Logout)));
        }else{
            holder.logoutTextView.setText(String.format("Logout : %s", "-"));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    public class HolderData  extends  RecyclerView.ViewHolder{
        TextView titleTextView, loginTextView, logoutTextView;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitleHistorySession);
            loginTextView = itemView.findViewById(R.id.textLoginHistorySession);
            logoutTextView = itemView.findViewById(R.id.textLogoutHistorySession);
        }
    }
}
