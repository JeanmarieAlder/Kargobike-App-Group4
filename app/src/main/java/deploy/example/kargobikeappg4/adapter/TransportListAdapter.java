package deploy.example.kargobikeappg4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import deploy.example.kargobikeappg4.R;

import java.util.List;

public class TransportListAdapter extends RecyclerView.Adapter<TransportListAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;


    public TransportListAdapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder viewHolder, int i){

        String title = data.get(i);
        viewHolder.textTitle.setText(title);
    }

    @Override
    public int getItemCount(){

        return data.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = layoutInflater.inflate(R.layout.transport_item, viewGroup, false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.test_itemText);
        }
    }


}
