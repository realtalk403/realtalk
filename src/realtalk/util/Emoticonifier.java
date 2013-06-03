package realtalk.util;

import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.realtalk.R;

/**
 * 
 * @author Cory
 *
 */
public final class Emoticonifier {
	/*
	 * Hungarian:
	 * sbb = SpannableStringBuilder
	 * 
	 */
	
    private static final HashMap<String, Integer> MPSTIEMOTICONS = new HashMap<String, Integer>();
    
    private Emoticonifier() { }
    
    static {
    	MPSTIEMOTICONS.put("(<", R.drawable.realtalksmile);
    	MPSTIEMOTICONS.put(":)", R.drawable.smile);
    	MPSTIEMOTICONS.put(":]", R.drawable.smile);
    	MPSTIEMOTICONS.put(":D", R.drawable.biggrin);
    	MPSTIEMOTICONS.put("XP", R.drawable.bleh);
    	MPSTIEMOTICONS.put("Xp", R.drawable.bleh);
    	MPSTIEMOTICONS.put("xP", R.drawable.bleh);
    	MPSTIEMOTICONS.put("xp", R.drawable.bleh);
    	MPSTIEMOTICONS.put("</3", R.drawable.brokenheart);
    	MPSTIEMOTICONS.put("<\\3", R.drawable.brokenheart);
    	MPSTIEMOTICONS.put("8)", R.drawable.cool);
    	MPSTIEMOTICONS.put(":\\", R.drawable.dry);
    	MPSTIEMOTICONS.put(":/", R.drawable.dry);
    	MPSTIEMOTICONS.put("<3", R.drawable.heart);
    	MPSTIEMOTICONS.put("o_O", R.drawable.huh);
    	MPSTIEMOTICONS.put("O_o", R.drawable.huh);
    	MPSTIEMOTICONS.put("O.o", R.drawable.huh);
    	MPSTIEMOTICONS.put("o.O", R.drawable.huh);
    	MPSTIEMOTICONS.put("*lol*", R.drawable.lol);
    	MPSTIEMOTICONS.put("*haha*", R.drawable.lol);
    	MPSTIEMOTICONS.put(">:(", R.drawable.mad);
    	MPSTIEMOTICONS.put(":|", R.drawable.mellow);
    	MPSTIEMOTICONS.put(":l", R.drawable.mellow);
    	MPSTIEMOTICONS.put(":I", R.drawable.mellow);
    	MPSTIEMOTICONS.put(":O", R.drawable.ohmy);
    	MPSTIEMOTICONS.put(":o", R.drawable.ohmy);
    	MPSTIEMOTICONS.put(":(", R.drawable.sad);
    	MPSTIEMOTICONS.put(":P", R.drawable.tongue);
    	MPSTIEMOTICONS.put(":p", R.drawable.tongue);
    	MPSTIEMOTICONS.put(";)", R.drawable.wink);
    	MPSTIEMOTICONS.put(":'(", R.drawable.tear);
    	
    }
    
    public static Spannable getSmiledText(Context context, String text) {
    	SpannableStringBuilder sbb = new SpannableStringBuilder(text);
    	for (int i = 0; i < sbb.length(); i++) {

    		for (Entry<String, Integer> entry : MPSTIEMOTICONS.entrySet()) {
    			int ilength = entry.getKey().length();
    			if (i + ilength > sbb.length()) {
    				continue;
    			}

    			if (sbb.subSequence(i, i + ilength).toString().equals(entry.getKey())) {
    				ImageSpan imagespan = new ImageSpan(context, entry.getValue(), ImageSpan.ALIGN_BASELINE);
    				sbb.setSpan(imagespan, i, i + ilength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    				i += ilength - 1;
    				break;
    			}
    		}
    	}
    	return sbb;
    }
}
