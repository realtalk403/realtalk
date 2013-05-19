package realtalk.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import android.test.AndroidTestCase;
/**
 * Message Info Test
 * 
 * White Box Test Case.
 * 
 * @author Colin Kho
 *
 */
public class MessageInfoTest extends AndroidTestCase {
    private MessageInfo msginfo;
    private List<MessageInfo> rgmi;
    private String body = "Default Message Body";
    private String sender = "Default Sender Body";
    private Timestamp timestamp = new Timestamp(100000);
    private static final int TIMEOUT = 10000;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        msginfo = new MessageInfo(body, sender, new Timestamp(timestamp.getTime()));
        rgmi = new ArrayList<MessageInfo>();
    }
    
    @Test(timeout = TIMEOUT)
    public void testConstructor() {
        assertTrue(body.equals(msginfo.stBody()));
        assertTrue(sender.equals(msginfo.stSender()));
        assertTrue(timestamp.equals(msginfo.timestampGet()));
    }
    
    @Test(timeout = TIMEOUT)
    public void testBodyGetter() {
        assertTrue(body.equals(msginfo.stBody()));
    }
    
    @Test(timeout = TIMEOUT)
    public void testSenderGetter() {
        assertTrue(sender.equals(msginfo.stSender()));
    }
    
    @Test(timeout = TIMEOUT)
    public void testTimestampGetter() {
        assertTrue(timestamp.equals(msginfo.timestampGet()));
    }
    
    @Test(timeout = TIMEOUT)
    public void testEquals() {
        MessageInfo msginfo2 = new MessageInfo(body, sender, new Timestamp(timestamp.getTime()));
        assertTrue(msginfo.equals(msginfo2));
    }

    @Test(timeout = TIMEOUT)
    public void testCompareTo() {
        MessageInfo msginfo2 = new MessageInfo(body, sender, new Timestamp(1000000));
        MessageInfo msginfo3 = new MessageInfo(body, sender, new Timestamp(10000));
        MessageInfo msginfo4 = new MessageInfo(body, sender, new Timestamp(1000));
        MessageInfo msginfo5 = new MessageInfo(body, sender, new Timestamp(100));
        MessageInfo msginfo6 = new MessageInfo(body, sender, new Timestamp(10));
        rgmi.add(msginfo);
        rgmi.add(msginfo2);
        rgmi.add(msginfo3);
        rgmi.add(msginfo4);
        rgmi.add(msginfo5);
        rgmi.add(msginfo6);
        Collections.sort(rgmi);
        long check = 10;
        for (MessageInfo mi : rgmi) {
            assertTrue(mi.timestampGet().getTime() == check);
            check *= 10;
        }
    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
