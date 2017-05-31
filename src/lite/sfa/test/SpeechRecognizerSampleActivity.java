package lite.sfa.test;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SpeechRecognizerSampleActivity extends Activity {
	private static final String TAG = "SpeechRecognizerSampleActivity";
	private SpeechRecognizer recog;
	private Runnable readyRecognizeSpeech;
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_2);

		recog = SpeechRecognizer.createSpeechRecognizer(this);
		recog.setRecognitionListener(new RecogListener(this));

		readyRecognizeSpeech = new Runnable() {
			@Override
			public void run() {
				startRecognizeSpeech();
			}
		};

		Button b = (Button) findViewById(R.id.start_recognize);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startRecognizeSpeech();
			}
		});
		// startRecognizeSpeech();
	}

	private void startRecognizeSpeech() {
		handler.removeCallbacks(readyRecognizeSpeech);

		

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, new Locale("ro"));
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

		recog.startListening(intent);

		((TextView) findViewById(R.id.status)).setText("");
		((TextView) findViewById(R.id.sub_status)).setText("");
		findViewById(R.id.start_recognize).setEnabled(false);
	}

	private static class RecogListener implements RecognitionListener {
		private SpeechRecognizerSampleActivity caller;
		private TextView status;
		private TextView subStatus;

		RecogListener(SpeechRecognizerSampleActivity a) {
			caller = a;
			status = (TextView) a.findViewById(R.id.status);
			subStatus = (TextView) a.findViewById(R.id.sub_status);
		}

		@Override
		public void onReadyForSpeech(Bundle params) {
			status.setText("ready for speech");
			Log.v(TAG, "ready for speech");
		}

		@Override
		public void onBeginningOfSpeech() {
			status.setText("beginning of speech");
			Log.v(TAG, "beginning of speech");
		}

		@Override
		public void onBufferReceived(byte[] buffer) {
			// status.setText("onBufferReceived");
			// Log.v(TAG,"onBufferReceived");
		}

		@Override
		public void onRmsChanged(float rmsdB) {
			String s = String.format("recieve : % 2.2f[dB]", rmsdB);
			subStatus.setText(s);
			// Log.v(TAG,"recieve : " + rmsdB + "dB");
		}

		@Override
		public void onEndOfSpeech() {
			status.setText("end of speech");
			Log.v(TAG, "end of speech");
			caller.handler.postDelayed(caller.readyRecognizeSpeech, 500);
		}

		@Override
		public void onError(int error) {
			status.setText("on error");
			Log.v(TAG, "on error");
			switch (error) {
			case SpeechRecognizer.ERROR_AUDIO:

				subStatus.setText("ERROR_AUDIO");
				break;
			case SpeechRecognizer.ERROR_CLIENT:

				subStatus.setText("ERROR_CLIENT");
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:

				subStatus.setText("ERROR_INSUFFICIENT_PERMISSIONS");
				break;
			case SpeechRecognizer.ERROR_NETWORK:

				subStatus.setText("ERROR_NETWORK");
				break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:

				subStatus.setText("ERROR_NETWORK_TIMEOUT");
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:

				subStatus.setText("ERROR_NO_MATCH");
				caller.handler.postDelayed(caller.readyRecognizeSpeech, 1000);
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:

				subStatus.setText("ERROR_RECOGNIZER_BUSY");
				caller.handler.postDelayed(caller.readyRecognizeSpeech, 1000);
				break;
			case SpeechRecognizer.ERROR_SERVER:

				subStatus.setText("ERROR_SERVER");
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:

				subStatus.setText("ERROR_SPEECH_TIMEOUT");
				caller.handler.postDelayed(caller.readyRecognizeSpeech, 1000);
				break;
			default:
			}
		}

		@Override
		public void onEvent(int eventType, Bundle params) {
			status.setText("on event");
			Log.v(TAG, "on event");
		}

		@Override
		public void onPartialResults(Bundle partialResults) {
			status.setText("on partial results");
			Log.v(TAG, "on results");
		}

		@Override
		public void onResults(Bundle data) {
			status.setText("on results");
			Log.v(TAG, "on results");

			ArrayList<String> results = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

			TextView t = (TextView) caller.findViewById(R.id.result);
			t.setText(results.get(0));

			boolean end = false;
			for (String s : results) {
				if (s.equals("gata"))
					end = true;
				if (s.equals("2"))
					end = true;
				if (s.equals("3"))
					end = true;
			}
			if (end)
				caller.findViewById(R.id.start_recognize).setEnabled(true);
			else
				caller.startRecognizeSpeech();
		}
	}
}
