package fi.guagua.pixrayandroid.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Most content Copied from http://sapandiwakar.in/recycler-view-item-click-handler/
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private static final String TAG = "RecyclerClickListener";
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
            //@Override
            //public void onLongPress(MotionEvent e) {
            //    Log.d(TAG, "onLongPress " + e);
            //}
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildLayoutPosition(childView));
        }
        return false; // let the event be handled in children
    }

    // Not going to be called because onInterceptTouchEvent() returns false; instead the children's
    // onTouchEvent() are going to be called.
    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }
}
