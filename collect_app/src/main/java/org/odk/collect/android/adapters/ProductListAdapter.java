package org.odk.collect.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Hospital;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final Context context;
    private List<Hospital.Prestation> prestations;

    public ProductListAdapter(List<Hospital.Prestation> prestations, Context context) {
        this.context = context;
        this.prestations = prestations;
    }

    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hospital.Prestation prestation = prestations.get(position);
        holder.name.setText(prestation.getName());
        holder.price.setText(prestation.getPrice()+" cfa");
        int availabilityIcon = prestation.getAvailable() ? R.drawable.ic_available : R.drawable.ic_unavailable;
        holder.availability.setImageResource(availabilityIcon);
        holder.availability.setTag(availabilityIcon);

        int typeIcon = prestation.getType().equals("Produit") ? R.drawable.ic_product : R.drawable.ic_service;
        holder.prestationType.setImageResource(typeIcon);
        holder.prestationType.setTag(typeIcon);
    }

    @Override
    public int getItemCount() {
        return prestations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView price;
        private final ImageView availability;
        private final ImageView prestationType;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.productName);
            price = view.findViewById(R.id.productPrice);
            availability = view.findViewById(R.id.productAvailability);
            prestationType = view.findViewById(R.id.prestationType);
        }
    }
}