package co.dekhok.railway;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import co.dekhok.railway.model.PendingModel;

class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder>{

    private ArrayList<PendingModel> data;
    Context context;

    public PendingAdapter(ArrayList<PendingModel> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pending_adapter, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final PendingAdapter.ViewHolder viewHolder, final int i) {
        final PendingModel abc = data.get(i);
        viewHolder.date.setText(abc.getDate());
        viewHolder.train.setText(abc.getTrain());
        viewHolder.upload.setTag(abc.getId());

        viewHolder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date, train;
        Button upload;


        public ViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            train = (TextView) view.findViewById(R.id.train);
            upload = (Button) view.findViewById(R.id.upload);



        }
    }
}
