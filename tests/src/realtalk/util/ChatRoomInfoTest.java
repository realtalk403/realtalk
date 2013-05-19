package realtalk.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import realtalk.util.ChatRoomInfo;
import android.test.AndroidTestCase;
import java.sql.Timestamp;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * 
 */

/**
 * This tests the ChatRoomInfo class.
 * 
 * White Box Test
 * 
 * @author Colin Kho
 *
 */
public class ChatRoomInfoTest extends AndroidTestCase {
    private static final int TIMEOUT = 10000;
    private static final int TEST_CONTENTS = 0;
    
    private static final String TEST_NAME = "RealTalkChatRoom";
    private static final String TEST_ID = "ASKLHDAKSJHFKASHF(*SA&F*A&SF*(&AS(*F&AS(&FASKHSKAJDHKJAHSDJHASKLDHASNDJSAHDA";
    private static final String TEST_DESC = "This is a test chat room.";
    private static final double TEST_LAT = 1.0;
    private static final double TEST_LONG = 10.0;
    private static final String TEST_CREATOR = "RealTalkAuthor";
    private static final int TEST_NUMUSER = 5;
    private static final Timestamp TEST_TIMESTAMP = new Timestamp(100000);
    private static final long TEST_TIMESTAMP_LONG = 100000;
    
    /* Private Fields */
    private String stName;
    private String stId;
    private String stDescription;
    private double latitude;
    private double longitude;
    private String stCreator;
    private int numUsers;
    private Timestamp timestampCreated; 
    
    private ChatRoomInfo cri;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.stName = TEST_NAME;
        this.stId = TEST_ID;
        this.stDescription = TEST_DESC;
        this.stCreator = TEST_CREATOR;
        this.latitude = TEST_LAT;
        this.longitude = TEST_LONG;
        this.timestampCreated = new Timestamp(TEST_TIMESTAMP.getTime());
        this.numUsers = TEST_NUMUSER;
        cri = new ChatRoomInfo(stName, stId, stDescription, latitude, longitude, stCreator, numUsers, timestampCreated);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        cri = null;
    }

    @Test(timeout = TIMEOUT)
    public void testConstructorWithTimestampObject() {
        checkConstructorWithoutTimestamp(this.cri);  
    }
    
    @Test(timeout = TIMEOUT)
    public void testConstructorWithTimestampAsLong() {
        ChatRoomInfo cri2 = new ChatRoomInfo(stName, stId, stDescription, latitude, longitude, stCreator, numUsers, TEST_TIMESTAMP_LONG);
        checkConstructorWithoutTimestamp(cri2);
        
    }
    
    private void checkConstructorWithoutTimestamp(ChatRoomInfo cri) {
        assertTrue(cri.stName().equals(TEST_NAME));
        assertTrue(cri.stId().equals(TEST_ID));
        assertTrue(cri.stCreator().equals(TEST_CREATOR));
        assertTrue(cri.stDescription().equals(TEST_DESC));
        assertTrue(Double.compare(cri.getLatitude(), TEST_LAT) == 0);
        assertTrue(Double.compare(cri.getLongitude(), TEST_LONG) == 0);
        assertTrue(cri.numUsersGet() == TEST_NUMUSER);
        assertTrue(cri.timestampCreated().getTime() == TEST_TIMESTAMP.getTime());
    }

    @Test(timeout = TIMEOUT)
    public void testGetName() {
        assertTrue(cri.stName().equals(TEST_NAME));
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetId() {
        assertTrue(cri.stId().equals(TEST_ID));
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetCreator() {
        assertTrue(cri.stCreator().equals(TEST_CREATOR));
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetDesc() {
        assertTrue(cri.stDescription().equals(TEST_DESC));
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetLat() {
        assertTrue(Double.compare(cri.getLatitude(), TEST_LAT) == 0);
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetLong() {
        assertTrue(Double.compare(cri.getLongitude(), TEST_LONG) == 0);
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetNumUser() {
        assertTrue(cri.numUsersGet() == TEST_NUMUSER);
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetTimestamp() {
        assertTrue(cri.timestampCreated().getTime() == TEST_TIMESTAMP.getTime());
    }
    
    /*
     * Test Parcelable Methods
     */
    
    @Test(timeout = TIMEOUT)
    public void testGetContents() {
        assertTrue(cri.describeContents() == TEST_CONTENTS);
    }
    
    @Test(timeout = TIMEOUT)
    public void testParcelable() {
        Parcel parcel = Parcel.obtain();
        cri.writeToParcel(parcel, 0);
        
        parcel.setDataPosition(0);
        
        ChatRoomInfo createdCri = ChatRoomInfo.CREATOR.createFromParcel(parcel);
        
        assertTrue(createdCri.stName().equals(TEST_NAME));
        assertTrue(createdCri.stId().equals(TEST_ID));
        assertTrue(createdCri.stCreator().equals(TEST_CREATOR));
        assertTrue(createdCri.stDescription().equals(TEST_DESC));
        assertTrue(Double.compare(createdCri.getLatitude(), TEST_LAT) == 0);
        assertTrue(Double.compare(createdCri.getLongitude(), TEST_LONG) == 0);
        assertTrue(createdCri.numUsersGet() == TEST_NUMUSER);
        assertTrue(createdCri.timestampCreated().getTime() == TEST_TIMESTAMP.getTime());
    }
    
    @Test(timeout = TIMEOUT)
    public void testNewArray() {
        ChatRoomInfo[] criArr = ChatRoomInfo.CREATOR.newArray(10);
        assertTrue(criArr.length == 10);
    }
}
