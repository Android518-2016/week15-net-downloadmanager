package ca.campbell.downloadmanager;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * DownloadManagerExample
 * 
 * This app uses DownloadManager to download a sends a URL uses HttpURLConnection class + GET
 * to download an image.
 * original example
 * http://blog.vogella.com/2011/06/14/android-downloadmanager-example/
 *
 *
 *
 */
public class DownloadManagerExample extends Activity {
	private static final int NETIOBUFFER = 1024;

	private static final String TAG = "DOWN";
	private EditText urlText;
	private TextView tv;
    private long enqueue;
	private DownloadManager dm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);

		urlText = (EditText) findViewById(R.id.myURL);
		tv = (TextView) findViewById(R.id.tv);
		BroadcastReceiver receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
					long downloadId = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, 0);
					DownloadManager.Query query = new DownloadManager.Query();
					query.setFilterById(enqueue);
					Cursor c = dm.query(query);
					if (c.moveToFirst()) {
						int columnIndex = c
								.getColumnIndex(DownloadManager.COLUMN_STATUS);
						if (DownloadManager.STATUS_SUCCESSFUL == c
								.getInt(columnIndex)) {
							tv.setText("Download complete...");
						}
					}
				}
			}
		};
		registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}  // onCreate()
	/*
	 * When user clicks button, executes an AsyncTask to do the download. Before
	 * attempting to fetch the URL, makes sure that there is a network
	 * connection.
	 */
	public void doDownload(View view) {
		String urlStr;
		if (urlText.getText().toString().equals(""))
			urlStr = (String) getResources().getText(R.string.tuxImgURL);
		else
			urlStr = "http://"+urlText.getText().toString();

		dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		Log.d(TAG, "url "+urlStr);
		DownloadManager.Request request =
				new DownloadManager.Request(
				Uri.parse(urlStr));
		enqueue = dm.enqueue(request);
		tv.setText("Download started...");
	} // doDownload
	public void showDownload(View view) {
		Intent i = new Intent();
		i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
		startActivity(i);
	}
} // Activity class