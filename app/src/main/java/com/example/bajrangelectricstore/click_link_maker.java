package com.example.bajrangelectricstore;



import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class click_link_maker {

    public static void makeClickLink(Context context, TextView textView) {
        String fullText = "If you are new user then? Click Here";
        SpannableString spannableString = new SpannableString(fullText);

        String clickText = "Click Here";
        int startIndex = fullText.indexOf(clickText);
        int endIndex = startIndex + clickText.length();

        // Clickable Span
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                context.startActivity(new Intent(context, SignIn_Activity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(context, R.color.your_custom_blue));
                ds.setUnderlineText(false); // Remove underline
                ds.setTypeface(Typeface.DEFAULT_BOLD); // Force bold
            }
        };

        // Apply spans
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        // Apply to TextView
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

}


