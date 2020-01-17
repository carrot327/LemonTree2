package com.cocotree.android.utils.text;

import android.os.Parcel;

/**
 * @author evanyu
 * @date 17/12/13
 */
public class TextStyleSpan extends android.text.style.StyleSpan implements ITextSpan, Cloneable {

    public TextStyleSpan(int style) {
        super(style);
    }

    public TextStyleSpan(Parcel src) {
        super(src);
    }

    @Override
    public TextStyleSpan clone() throws CloneNotSupportedException {
        return (TextStyleSpan) super.clone();
    }
}
