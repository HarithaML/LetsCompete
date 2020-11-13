package com.example.letscompete;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private String[] localDataSet;
    private String[] desc;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView main;
        private final TextView sub;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            main = (TextView) view.findViewById(R.id.Main);
            sub = (TextView) view.findViewById(R.id.Sub);

        }

        public TextView getTextView() {
            return main;
        }

        public TextView getSub() {
            return sub;
        }

    }
    public CustomAdapter(String[] dataSet, String[] des) {
        localDataSet = dataSet;
        desc = des;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.setting_row_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet[position]);
        viewHolder.getSub().setText(desc[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}

