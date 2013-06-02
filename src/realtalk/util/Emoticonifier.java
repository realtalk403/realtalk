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
public class Emoticonifier {
	/*
	 * Hungarian:
	 * sbb = SpannableStringBuilder
	 * 
	 */
	
    private static final HashMap<String, Integer> mpstiEmoticons = new HashMap<String, Integer>();
    
    static {
    	mpstiEmoticons.put("(<", R.drawable.realtalksmile);
    	mpstiEmoticons.put(":)", R.drawable.smile);
    	mpstiEmoticons.put(":]", R.drawable.smile);
    	mpstiEmoticons.put(":D", R.drawable.biggrin);
    	mpstiEmoticons.put("XP", R.drawable.bleh);
    	mpstiEmoticons.put("Xp", R.drawable.bleh);
    	mpstiEmoticons.put("xP", R.drawable.bleh);
    	mpstiEmoticons.put("xp", R.drawable.bleh);
    	mpstiEmoticons.put("</3", R.drawable.brokenheart);
    	mpstiEmoticons.put("<\\3", R.drawable.brokenheart);
    	mpstiEmoticons.put("8)", R.drawable.cool);
    	mpstiEmoticons.put(":\\", R.drawable.dry);
    	mpstiEmoticons.put(":/", R.drawable.dry);
    	mpstiEmoticons.put("<3", R.drawable.heart);
    	mpstiEmoticons.put("o_O", R.drawable.huh);
    	mpstiEmoticons.put("O_o", R.drawable.huh);
    	mpstiEmoticons.put("O.o", R.drawable.huh);
    	mpstiEmoticons.put("o.O", R.drawable.huh);
    	mpstiEmoticons.put("*lol*", R.drawable.lol);
    	mpstiEmoticons.put("*haha*", R.drawable.lol);
    	mpstiEmoticons.put(">:(", R.drawable.mad);
    	mpstiEmoticons.put(":|", R.drawable.mellow);
    	mpstiEmoticons.put(":l", R.drawable.mellow);
    	mpstiEmoticons.put(":I", R.drawable.mellow);
    	mpstiEmoticons.put(":O", R.drawable.ohmy);
    	mpstiEmoticons.put(":o", R.drawable.ohmy);
    	mpstiEmoticons.put(":(", R.drawable.sad);
    	mpstiEmoticons.put(":P", R.drawable.tongue);
    	mpstiEmoticons.put(":p", R.drawable.tongue);
    	mpstiEmoticons.put(";)", R.drawable.wink);
    	mpstiEmoticons.put(":'(", R.drawable.tear);
    	
    }
    
    public static Spannable getSmiledText(Context context, String text) {
    	SpannableStringBuilder sbb = new SpannableStringBuilder(text);
    	for (int i = 0; i < sbb.length(); i++) {

    		for (Entry<String, Integer> entry : mpstiEmoticons.entrySet()) {
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
