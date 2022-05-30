package com.sabid.aspasn.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sabid.aspasn.DataModels.Clients;
import com.sabid.aspasn.R;

import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ViewHolder> {
    final View.OnClickListener onClickListener = new MyOnClickListener();
    Context context;
    List<Clients> clientsList;
    RecyclerView rvClients;

    public ClientsAdapter(Context context, List<Clients> clientsList, RecyclerView rvClients) {
        this.context = context;
        this.clientsList = clientsList;
        this.rvClients = rvClients;

    }

    @NonNull
    @Override
    public ClientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup ViewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_client, ViewGroup, false);
        view.setOnClickListener(onClickListener);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsAdapter.ViewHolder viewHolder, int position) {
        Clients clients = clientsList.get(position);
        viewHolder.rowName.setText(clients.getName());
        viewHolder.rowAddress.setText(clients.getAddress());
        viewHolder.rowMobile.setText(clients.getMobile());
    }

    @Override
    public int getItemCount() {
        return clientsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rowName;
        TextView rowAddress;
        TextView rowMobile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.txtName);
            rowAddress = itemView.findViewById(R.id.txtAddress);
            rowMobile = itemView.findViewById(R.id.txtMobile);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = rvClients.getChildLayoutPosition(view);
            String name = clientsList.get(itemPosition).getName();
            Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
        }
    }
}
