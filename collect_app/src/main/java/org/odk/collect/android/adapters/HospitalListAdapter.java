package org.odk.collect.android.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.model.Hospital;

import java.util.List;

public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.ViewHolder> {

    private final Context context;
    private List<Hospital> hospitals;
    private final HospitalItemClickListener listener;

    public HospitalListAdapter(List<Hospital> hospitals, Context context, HospitalItemClickListener listener) {
        this.context = context;
        this.hospitals = hospitals;
        this.listener = listener;
    }

    @Override
    public HospitalListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.hospital_item_layout, parent, false);
        return new ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hospital hospital = hospitals.get(position);
        holder.name.setText(hospital.getName());
        holder.type.setText(hospital.getType());
        holder.level.setText(hospital.getLevel());

        holder.boutonAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.audio_file); // Remplacez "audio_file" par le nom de votre fichier audio dans le dossier res/raw
                mediaPlayer.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public interface HospitalItemClickListener {
        void onClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final HospitalItemClickListener listener;
        private final TextView name;
        private final TextView type;
        private final TextView level;
        private final Button boutonAudio;

        ViewHolder(View view, HospitalItemClickListener listener) {
            super(view);
            this.listener = listener;
            name = view.findViewById(R.id.hospitalName);
            type = view.findViewById(R.id.hospitalType);
            level = view.findViewById(R.id.hospitalLevel);
            boutonAudio = view.findViewById(R.id.boutonaudio);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }
}