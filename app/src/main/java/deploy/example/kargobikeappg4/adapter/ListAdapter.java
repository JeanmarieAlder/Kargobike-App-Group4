package deploy.example.kargobikeappg4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import deploy.example.kargobikeappg4.R;
import deploy.example.kargobikeappg4.db.entities.Product;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ListAdapter<T> extends ArrayAdapter<T> {

    //Attributes
    private int resource;
    private List<T> data;


    //Constructor
    public ListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> data) {
        super(context, resource, data);
        this.resource = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    //determine the clicked item
    @Override
    public int getPosition(@Nullable T item) {
        //get position if the item is a Product
        if (item instanceof Product) {
            for (int i = 0; i < data.size(); i++) {
                Product p = (Product) data.get(i);
                if (p.getName().equals(((Product) item).getName())) {
                    return i;
                }
            }
        }

        return super.getPosition(item);
    }

    //Give this Item back
    public T getItem(int position) {
        return data.get(position);
    }


    //Display the informations in the view
    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.RowList);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        T item = getItem(position);
        if (item != null) {
            viewHolder.itemView.setText(item.toString());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView itemView;
    }

    public void updateData(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }
}