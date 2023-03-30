package com.example.tugas10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas10.models.UserSessionModel;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    List<UserSessionModel> listData;
    LayoutInflater inflater;

    public AdapterData(Context context, List<UserSessionModel> listData) {
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
    public void onBindViewHolder(@NonNull AdapterData.HolderData holder, int position) {
        holder.titleTextView.setText(String.format("History #%d", position + 1));
        if (listData.get(position).login != null) {
            holder.loginTextView.setText(String.format("Login  : %s",
                    listData.get(position).login.toString()));
        }
        if (listData.get(position).logout != null) {
            holder.logoutTextView.setText(String.format("Logout : %s",
                    listData.get(position).logout.toString()));
        } else {
            holder.logoutTextView.setText(String.format("Logout : %s", "-"));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView titleTextView, loginTextView, logoutTextView;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitleHistorySession);
            loginTextView = itemView.findViewById(R.id.textLoginHistorySession);
            logoutTextView = itemView.findViewById(R.id.textLogoutHistorySession);
        }
    }
}
