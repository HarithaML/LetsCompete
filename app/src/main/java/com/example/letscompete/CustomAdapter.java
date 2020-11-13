package com.example.letscompete;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        decideOnClick(viewHolder.itemView, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

    private void decideOnClick(View view, final int position)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sometext(view);
            }
        });
    }
    private void sometext(View view)
    {
        Log.i("S", "yay");
        Toast yay = Toast.makeText(view.getContext(), "ok", Toast.LENGTH_SHORT);
        yay.show();
    }
}

