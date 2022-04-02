package ipfs.gomobile.example;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
//import java.util.Arrays;

import ipfs.gomobile.android.IPFS;

final class StartIPFS extends AsyncTask<Void, Void, String> {
    private static final String TAG = "StartIPFS";

    private final WeakReference<MainActivity> activityRef;
    private boolean backgroundError;

    StartIPFS(MainActivity activity) {
        activityRef = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
//        MainActivity activity = activityRef.get();
//        try {
//            IPFS ipfs = new IPFS(activity.getApplicationContext());
//            ipfs.newRequest("bootstrap").withArgument("rm").withOption("--all", true).send();
//        }
//        catch (Exception e){
//            Log.d("inside",e.toString());
//        }
    }

    @Override
    protected String doInBackground(Void... v) {
        MainActivity activity = activityRef.get();
        if (activity == null || activity.isFinishing()) {
            cancel(true);
            return null;
        }

        try {
            IPFS ipfs = new IPFS(activity.getApplicationContext());

            ipfs.start();

            ipfs.newRequest("bootstrap/rm/all").send();


            ipfs.newRequest("bootstrap/add").withArgument("/ip4/192.168.43.238/tcp/4001/p2p/12D3KooWEmQpqnZWMdx7xozKxko1va7k9kxzuXaCTGwue1nYQkRd").send();

            ArrayList<JSONObject> k = ipfs.newRequest("config/show").sendToJSONList();
            Log.d("CONFIG", k.get(0).getString("Addresses"));
            ArrayList<JSONObject> jsonList = ipfs.newRequest("id").sendToJSONList();

            activity.setIpfs(ipfs);

            return jsonList.get(0).getString("ID");
        } catch (Exception err) {
            backgroundError = true;
            return MainActivity.exceptionToString(err);
        }
    }

    protected void onPostExecute(String result) {
        MainActivity activity = activityRef.get();
        if (activity == null || activity.isFinishing()) return;

        if (backgroundError) {
            activity.displayPeerIDError(result);
            Log.e(TAG, "IPFS start error: " + result);
        } else {
            activity.displayPeerIDResult(result);
            Log.i(TAG, "Your PeerID is: " + result);
        }
    }
}
