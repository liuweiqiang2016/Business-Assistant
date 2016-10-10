package myapp.alex.com.businessassistant.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MaqueeTextView extends TextView{

	public MaqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MaqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MaqueeTextView(Context context) {
		super(context);
	}
	
	public boolean isFocused() {
		return true;
	}

}
