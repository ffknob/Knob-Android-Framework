package br.org.knob.android.framework.ui;

import android.content.Context;
import android.view.View;

public interface ItemTouchHelperAdapterListener {
    void onClickItem(Context context, View view, int position);
    void onLongClickItem(Context context, View view, int position);
    void onSwipeItem(Context context, View view, int position);
    void onDismissItem(Context context, View view, int position);
    boolean onMoveItem(Context context, View view, int fromPosition, int toPosition);

}
