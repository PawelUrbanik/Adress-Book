package com.example.pawel.adressbook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDivider extends RecyclerView.ItemDecoration {

    private final Drawable divider;

    public ItemDivider(Context context) {
        int [] attrs = {android.R.attr.listDivider};
        divider = context.obtainStyledAttributes(attrs).getDrawable(0);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        /* Wyliczenie współrzędnych początka i końca linii */
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        /* Rysowanie linii pod każdym elementem poza ostatnim */
        for (int i= 0; i < parent.getChildCount() -1; i++)
        {
         /* Odczytanie itego elementu z listy */
            View item = parent.getChildAt(i);

            /* Obliczenie wsporzędnych na osi y */
            int top = item.getBottom() + ((RecyclerView.LayoutParams) item.getLayoutParams()).bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();


            /* Narysowanie linii */
            divider.setBounds(left,top,right,bottom);
            divider.draw(c);
        }
    }
}
