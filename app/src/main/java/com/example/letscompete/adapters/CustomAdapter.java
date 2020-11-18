package com.example.letscompete.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.R;
import com.example.letscompete.activities.Setting_Activity;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context mContext;
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

        public ViewHolder(View view, Context context) {
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
        mContext = null;
    }
    public CustomAdapter(String[] dataSet, String[] des, Context context) {
        localDataSet = dataSet;
        desc = des;
        mContext = context;
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
        if(mContext instanceof Setting_Activity)
        switch(position)
        {
            case 0:
                view.setOnClickListener(view1 ->((Setting_Activity) mContext).showImagePicDialog());
                break;
            case 1:
                view.setOnClickListener(view1 ->((Setting_Activity) mContext).showNamePhoneUpdateDialog("name"));
                break;
            case 3:
                view.setOnClickListener(view1 -> sometext(view1));
                break;
            default:
                break;
        }
    }
    private void sometext(View view)
    {
        Context context = view.getContext();
        AlertDialog.Builder bdialog = new AlertDialog.Builder(context);
        bdialog.setTitle("Change Privacy");

        String []choices = {"Public", "Friends Only", "Private"};
        bdialog.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i)
                        {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        AlertDialog dialog = bdialog.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
    }
}

