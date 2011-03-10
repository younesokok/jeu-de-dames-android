package android.dames.utils;

import android.os.Handler;

/**
 * Create a simple handler that we can use to cause animation to happen.  We
 * set ourselves as a target and we can use the sleep()
 * function to cause an update/invalidate to occur at a later date.
 */
public class RefreshHandler extends Handler {
    public void sleep(long attenteEnSecondes) {
    	this.removeMessages(0);
        sendMessageDelayed(obtainMessage(0), attenteEnSecondes*10000);
    }
}
