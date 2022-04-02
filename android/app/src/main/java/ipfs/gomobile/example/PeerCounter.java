package ipfs.gomobile.example;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import ipfs.gomobile.android.IPFS;

final class PeerCounter {
    private static final String TAG = "PeerCounter";
    private final WeakReference<MainActivity> activityRef;
    private final int interval;
    private Thread runner;

    PeerCounter(MainActivity activity, int interval) {
        activityRef = new WeakReference<>(activity);
        this.interval = interval;
    }

    void start() {
        runner = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    updatePeerCount();
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
        runner.start();
    }

    private void updatePeerCount() {
        final MainActivity activity = activityRef.get();
        if (activity == null || activity.isFinishing()) return;

        try {
            IPFS ipfs = activity.getIpfs();

            ArrayList<JSONObject> jsonList = ipfs.newRequest("swarm/peers")
                .withOption("verbose", false)
                .withOption("streams", false)
                .withOption("latency", false)
                .withOption("direction", false)
                .sendToJSONList();
            ArrayList<JSONObject> k = ipfs.newRequest("config/show").sendToJSONList();
            Log.d("CONFIG", k.get(0).getString("Addresses"));
            //System.out.println(jsonList);

/*
            try {
                byte[] filess = ipfs.newRequest("crazyTest").send();
                String array = new String(filess);
                System.out.println(array);
            Log.d("id", array);
            }
            catch( Exception err){
                System.out.println("err:");
                System.out.println(err);
            }
*/

            JSONArray peerList = jsonList.get(0).getJSONArray("Peers");
            final int count = peerList.length();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.updatePeerCount(count);
                }
            });
        } catch (JSONException err) {
            // Should only occurs if the peer list is empty, don't log an error
        } catch (Exception err) {
            Log.e(TAG, err.toString());
        }
    }

    void stop() {
        if (runner != null) {
            if (!runner.isInterrupted()) runner.interrupt();
            runner = null;
        }
    }
}
