package in.sportscafe.nostragamus.module.common;

/**
 * Created by deepanshi on 3/5/18.
 */

import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.util.Log;

public class NostraTagHandler implements TagHandler {

    boolean first = true;
    String parent = null;
    int index = 1;

    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {

        if (tag.equals("ul")) parent = "ul";
        else if (tag.equals("ol")) parent = "ol";
        if (tag.equals("li")) {
            if (parent.equals("ul")) {
                if (first) {
                    output.append("\n•\t");
                    first = false;
                } else {
                    first = true;
                }
            } else {
                if (first) {
                    output.append("\n\t" + index + ". ");
                    first = false;
                    index++;
                } else {
                    first = true;
                }
            }
        }
    }
}
