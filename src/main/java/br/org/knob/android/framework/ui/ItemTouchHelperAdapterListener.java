package br.org.knob.android.framework.ui;

import android.content.Context;
import android.view.View;

public interface ItemTouchHelperAdapterListener {
    void onClickItem(Context context, View view, int position, Long id);
    void onLongClickItem(Context context, View view, int position,  Long id);
    void onSwipeItem(Context context, View view, int position, Long id);
    void onDismissItem(Context context, View view, int position,  Long id);
    boolean onMoveItem(Context context, View view, int fromPosition, int toPosition,  Long id);

}
