package com.saceone.pickerlibrary;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Ramón on 08/03/2018.
 */

public class HorizontalRulerLayoutManager extends LinearLayoutManager {
    private int mParentWidth;
    private int mItemWidth;

    public HorizontalRulerLayoutManager(Context context, int orientation, boolean reverseLayout, int mParentWidth, int mItemWidth) {
        super(context, orientation, reverseLayout);
        this.mParentWidth = mParentWidth;
        this.mItemWidth = mItemWidth;
    }

    @Override
    public int getPaddingLeft(){
        return Math.round(mParentWidth / 2f - mItemWidth / 2f);
    }
    @Override
    public int getPaddingRight(){
        return getPaddingLeft();
    }
}
