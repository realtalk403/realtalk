package realtalk.util;

import android.test.AndroidTestCase;

/**
 * @author Cory Shiraishi
 *
 * Tests for Emoticonifier
 */
public class EmoticonifierTest extends AndroidTestCase {

	/**
	 * Tests if it throws an exception with smilies
	 */
	public void testStringWithSmilies() {
		Emoticonifier.getSmiledText(getContext(), ":) :D :) har har har");
		//fails if it throws an exception
	}
	
	/** 
	 * Tests if it throws an exception with no smilies
	 */
	public void testStringWithoutSmilies() {
		Emoticonifier.getSmiledText(getContext(), "hahahaha lol dude wow roflcoptersauce");
	}
	
}
