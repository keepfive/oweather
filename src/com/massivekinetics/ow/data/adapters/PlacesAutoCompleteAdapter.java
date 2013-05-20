package com.massivekinetics.ow.data.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.massivekinetics.ow.application.OWApplication;
import com.massivekinetics.ow.data.Autocompleter;
import com.massivekinetics.ow.data.model.Prediction;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: bovy
 * Date: 5/10/13
 * Time: 1:44 AM
 */
public class PlacesAutoCompleteAdapter extends ArrayAdapter<Prediction> implements Filterable {
        private ArrayList<Prediction> resultList = new ArrayList<Prediction>();
        private Autocompleter autocompleter = new Autocompleter();
        private int layoutID;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.layoutID = textViewResourceId;
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = View.inflate(OWApplication.getInstance(), layoutID, null);
        updateView(convertView, position);
        return convertView;
    }

    private void updateView(View view, int position){
        TextView text = (TextView) view;
        text.setTypeface(OWApplication.getInstance().getFontThin());
        text.setText(getItem(position).getDescription());
    }

    @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Prediction getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        ArrayList<Prediction> predictions = autocompleter.getPlacePredictions(constraint.toString());
                        if(predictions != null)
                            resultList = predictions;
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }
