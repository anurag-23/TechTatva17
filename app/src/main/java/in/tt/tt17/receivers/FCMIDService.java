package in.tt.tt17.receivers;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by anurag on 3/10/17.
 */
public class FCMIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMInstanceIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", refreshedToken);
    }
}
