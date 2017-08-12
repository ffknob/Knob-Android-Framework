package br.org.knob.android.framework.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;
    private Context context;
    private RecyclerView.ViewHolder viewHolder;
    private View view;
    private ItemTouchHelperAdapterListener itemTouchHelperAdapterListener;

    public OnSwipeTouchListener(Context context, ItemTouchHelperAdapterListener itemTouchHelperAdapterListener) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.context = context;
        this.itemTouchHelperAdapterListener = itemTouchHelperAdapterListener;
    }

    public OnSwipeTouchListener(Context context, RecyclerView.ViewHolder viewHolder, ItemTouchHelperAdapterListener itemTouchHelperAdapterListener) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.context = context;
        this.viewHolder = viewHolder;
        if(viewHolder != null) this.view = viewHolder.itemView;
        this.itemTouchHelperAdapterListener = itemTouchHelperAdapterListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public RecyclerView.ViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(RecyclerView.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ItemTouchHelperAdapterListener getItemTouchHelperAdapterListener() {
        return itemTouchHelperAdapterListener;
    }

    public void setItemTouchHelperAdapterListener(ItemTouchHelperAdapterListener itemTouchHelperAdapterListener) {
        this.itemTouchHelperAdapterListener = itemTouchHelperAdapterListener;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        // Determines the fling velocity and then fires the appropriate swipe event accordingly
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeUp() {
    }

    public void onSwipeDown() {
    }

    public void onClick() {
    }

    public void onDoubleClick() {
    }

    public void onLongClick() {
    }
}