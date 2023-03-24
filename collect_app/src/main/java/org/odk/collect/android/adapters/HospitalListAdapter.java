package org.odk.collect.android.adapters;
        import android.content.Context;
        import android.text.Html;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import androidx.recyclerview.widget.RecyclerView;
        import org.odk.collect.android.R;
        import org.odk.collect.android.adapters.model.Hospital;
        import java.util.List;
        import com.google.common.eventbus.Subscribe;
        import com.google.gson.Gson;
        import timber.log.Timber;
public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.ViewHolder> {

    private final Context context;
    private List<Hospital> hospitals;
    private final HospitalItemClickListener listener;

    public  HospitalListAdapter(List<Hospital> hospitals, Context context, HospitalItemClickListener listener) {
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
        holder.title.setText(hospital.getName());
        holder.preview.setText(hospital.getType().name());
        holder.level.setText(hospital.getLevel().name());
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
        private final TextView title;
        private final TextView preview;
        private final TextView level;

        ViewHolder(View view, HospitalItemClickListener listener) {
            super(view);
            this.listener = listener;
            title = view.findViewById(R.id.hospitalTitle);
            preview = view.findViewById(R.id.hospitalPreview);
            level = view.findViewById(R.id.hospitalLevel);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }
}