package com.wakeonlan.app.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wakeonlan.app.R;

public class CustomHolderSaves extends RecyclerView.ViewHolder {
    protected TextView mac;
    protected TextView name;

    public CustomHolderSaves(@NonNull View itemView) {
        super(itemView);
        mac = itemView.findViewById(R.id.mac_address);
        name = itemView.findViewById(R.id.name_of_device);
    }
}
