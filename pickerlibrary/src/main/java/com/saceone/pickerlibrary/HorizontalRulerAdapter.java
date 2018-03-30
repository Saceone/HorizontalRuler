package com.saceone.pickerlibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramón on 27/02/2018.
 */

public class HorizontalRulerAdapter extends RecyclerView.Adapter<HorizontalRulerAdapter.HorizontalRulerAdapterViewHolder> {

    private List<Object> itemList;
    private Context context;

    private List<HorizontalRulerDivisor> divisorList;

    private int divisorColor;
    private boolean loadedDivisorColor = false;

    private int valueColor;
    private boolean loadedValueColor = false;

    public HorizontalRulerAdapter(ArrayList<Object> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.divisorList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(final HorizontalRulerAdapterViewHolder HorizontalRulerAdapterViewHolder, int i) {
        final Object item = itemList.get(i);
        int height = 35;
        int maxHeightForThisInterval = 0;
        for(HorizontalRulerDivisor div : this.divisorList){
            if(i % div.itemInterval == 0){
                if(div.pxDivisor > maxHeightForThisInterval){
                    height = div.pxDivisor;
                    maxHeightForThisInterval = div.pxDivisor;
                }
            }
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) HorizontalRulerAdapterViewHolder.mTxItemLine.getLayoutParams();
        params.height = height;
        HorizontalRulerAdapterViewHolder.mTxItemLine.setLayoutParams(params);
        HorizontalRulerAdapterViewHolder.mTxItemValue.setText(String.valueOf(item));

        if(loadedDivisorColor) HorizontalRulerAdapterViewHolder.mTxItemLine.setBackgroundResource(divisorColor);
        if(loadedValueColor) HorizontalRulerAdapterViewHolder.mTxItemValue.setTextColor(valueColor);


    }

    public void addDivisor(HorizontalRulerDivisor divisor) {
        this.divisorList.add(divisor);
    }

    public Object getItemAt(int index) {
        if (index>=0 && index<itemList.size()) return itemList.get(index);
        return null;
    }

    public int indexOf(Object obj){
        return itemList.indexOf(obj);
    }

    public void setDivisorColor(int divisorColor){
        loadedDivisorColor = true;
        this.divisorColor = divisorColor;
    }

    public void setValueColor(int valueColor){
        loadedValueColor = true;
        this.valueColor = valueColor;
    }

    @Override
    public HorizontalRulerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ruler_horizontal_item, viewGroup, false);
        return new HorizontalRulerAdapterViewHolder(itemView);
    }


    public static class HorizontalRulerAdapterViewHolder extends RecyclerView.ViewHolder {

        protected TextView mTxItemValue;
        protected View mTxItemLine;

        public HorizontalRulerAdapterViewHolder(View v) {
            super(v);
            mTxItemLine = v.findViewById(R.id.line_ruler_horizontal_item);
            mTxItemValue = v.findViewById(R.id.tx_ruler_horizontal_item);
        }
    }
}