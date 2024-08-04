package com.wakeonlan.app.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final CustomAdapterSaves adapter;
    private final Paint mPaint;
    private final int backgroundColor;
    private Context context;

    public SwipeToDeleteCallback(CustomAdapterSaves adapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT); // enabling swipe left and right
        this.adapter = adapter;
        this.context = context;

        // paint object
        mPaint = new Paint();
        // background color for swipe
        backgroundColor = ContextCompat.getColor(context, android.R.color.holo_red_light);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // no move operation is needed so return false
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // deleting the item after a swipe
        int position = viewHolder.getAdapterPosition();
        adapter.removeItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            float itemHeight = (float) itemView.getBottom() - itemView.getTop();

            // drawing the background
            if (dX < 0 || dX > 0) { // swiping left and right
                mPaint.setColor(backgroundColor);
                RectF backgroundLeft = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + dX, itemView.getBottom());
                RectF backgroundRight = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                c.drawRect(backgroundLeft, mPaint);
                c.drawRect(backgroundRight, mPaint);

            }
        }
    }

}