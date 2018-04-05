package com.saceone.pickerlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/*
 * Created by Ramón on 03/03/2018.
 */

/**
 * A {@link RecyclerView} that shows an arraylist as a horizontal picker.
 */
public class HorizontalRuler extends RelativeLayout {


    private Context context;
    private LinearLayoutManager layoutManager;

    private RecyclerView ruler;
    private TextView label;
    private String labelAppendix = "";
    private ImageView rulerPick;

    private HorizontalRulerAdapter adapter;
    private ArrayList<Object> list;
    private Object centeredElement;


    public HorizontalRuler(Context context) {
        super(context);
        init(context, null, 0);
    }

    public HorizontalRuler(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public HorizontalRuler(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    /**
     * Initial settings of the ruler
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.ruler_horizontal, this);

        this.ruler = alertLayout.findViewById(R.id.ruler);
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        this.ruler.setPadding(width / 2, 0, width / 2, 0);

        this.label = alertLayout.findViewById(R.id.ruler_label);
        this.rulerPick = alertLayout.findViewById(R.id.ruler_pick);

        layoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);
        this.ruler.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(this.ruler);

        this.ruler.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                updateCenteredElement();
            }
        });
    }

    /**
     * Updates the centered value in order to not to lose track of it
     */
    private void updateCenteredElement(){
        int firstVisible = layoutManager.findFirstVisibleItemPosition();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        int itemsCount = lastVisible - firstVisible + 1;

        int screenCenter = getResources().getDisplayMetrics().widthPixels / 2;

        int minCenterOffset = Integer.MAX_VALUE;

        int middleItemIndex = 0;

        for (int index = 0; index < itemsCount; index++) {

            View listItem = layoutManager.getChildAt(index);

            if (listItem == null)
                return;

            int leftOffset = listItem.getLeft();
            int rightOffset = listItem.getRight();
            int centerOffset = Math.abs(leftOffset - screenCenter) + Math.abs(rightOffset - screenCenter);

            if (minCenterOffset > centerOffset) {
                minCenterOffset = centerOffset;
                middleItemIndex = index + firstVisible;
            }
        }

        if(middleItemIndex!=-1) {
            centeredElement = (Object) adapter.getItemAt(middleItemIndex);
            String lbl = String.valueOf(centeredElement) + labelAppendix;
            label.setText(lbl);
            Log.d("quiero", "centrado a " + middleItemIndex);
        }
    }

    /**
     * Returns the value of the element in the center of the picker
     *
     * @return
     */
    public Object getCenteredValue() {
        return centeredElement;
    }

    /**
     * Sets an array of elements as the ones to be shown in the picker
     *
     * @param elements
     */
    public void setElements(ArrayList<Object> elements) {
        this.list = elements;
        this.adapter = new HorizontalRulerAdapter(this.list, this.context);
        this.ruler.setAdapter(this.adapter);
        setInitValue(elements.get(elements.size()/2));
    }

    /**
     * Tints the label background with a desired color
     *
     * @param rscIdLabelBackColor Resource Id
     */
    public void setLabelBackgroundColor(int rscIdLabelBackColor) {
        this.label.setBackgroundResource(rscIdLabelBackColor);
        this.rulerPick.setColorFilter(ContextCompat.getColor(context, rscIdLabelBackColor), PorterDuff.Mode.SRC_IN);
    }

    /**
     * Tints the label background with a desired color
     *
     * @param labelTextColor
     */
    public void setLabelTextColor(int labelTextColor) {
        this.label.setTextColor(labelTextColor);
    }

    /**
     * Tints the divisor lines with a desired color
     *
     * @param rscIdDivisorColor Resource Id
     */
    public void setDivisorColor(int rscIdDivisorColor) {
        this.adapter.setDivisorColor(rscIdDivisorColor);
        adapter.notifyDataSetChanged();
    }

    /**
     * Tints the value labels with a desired color
     *
     * @param valueColor
     */
    public void setValueColor(int valueColor) {
        this.adapter.setValueColor(valueColor);
        adapter.notifyDataSetChanged();
    }


    /**
     * Sets the width of the horizontal fading edge
     *
     * @param pxFadingEdge
     */
    public void setFadingEdge(int pxFadingEdge) {
        this.ruler.setFadingEdgeLength(pxFadingEdge);
    }


    /**
     * Sets the value that the picker will be centered at the very beginning
     * By default it will be the middle value
     * @param initValue
     */
    public void setInitValue(Object initValue){
        this.ruler.smoothScrollToPosition(adapter.indexOf(initValue));
        updateCenteredElement();
    }

    /**
     * Sets the value that the picker will be centered at the very beginning
     * By default it will be the middle value
     * @param targetValue
     */
    public void moveTo(Object targetValue){
        this.ruler.smoothScrollToPosition(adapter.indexOf(targetValue));
        updateCenteredElement();
    }


    /**
     * Sets a message to be displayed after the data in the label
     *
     * @param labelAppendix
     */
    public void appendLabel(String labelAppendix) {
        this.labelAppendix = labelAppendix;
    }

    /**
     * Adds a new rule of divisor.
     * A certain height is applied to the divisor every certain interval
     *
     * @param divisor
     */
    public void addNewDivisor(HorizontalRulerDivisor divisor) {
        this.adapter.addDivisor(divisor);
        this.adapter.notifyDataSetChanged();
    }

























    /**
     * Checks if {@link LinearLayoutManager} has loaded all the positions
     * and waits 500 ms in that case before setting the value on the picker
     *
     * @param initValue
     */
    public void dragTo(final Object initValue) {
        if (layoutManager.findFirstVisibleItemPosition() == RecyclerView.NO_POSITION
                || layoutManager.findLastVisibleItemPosition() == RecyclerView.NO_POSITION) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    dragFinallyTo(initValue);
                }
            }, 500);
        } else {
            dragFinallyTo(initValue);
        }
    }
    /**
     * This is the method that really sets the desired value on the picker
     *
     * @param initValue
     */
    private void dragFinallyTo(Object initValue) {
        int target = adapter.indexOf(initValue);
        int current = adapter.indexOf(centeredElement);
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        Log.d("quiero","target "+target);
        Log.d("quiero","current "+current);
        Log.d("quiero","first "+first);
        Log.d("quiero","last "+last);
        if(target>0 && target<this.list.size()) {
            if (target < current) {
                int offset = current - target;
                int shift = first - offset;
                if (shift > -1) ruler.smoothScrollToPosition(first - offset);
            } else if (target > current) {
                int offset = target - current;
                int shift = last + offset;
                if (shift > -1) ruler.smoothScrollToPosition(last + offset);
            }
        }
        else {
            Toast.makeText(context, "El valor seleccionado está fuera de rango.", Toast.LENGTH_SHORT).show();
        }
    }
}
