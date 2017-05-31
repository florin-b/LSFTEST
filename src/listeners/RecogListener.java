package listeners;

import java.util.ArrayList;

import lite.sfa.test.SpeechRecognizerSampleActivity;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

public class RecogListener implements RecognitionListener {
	private SpeechRecognizerSampleActivity caller;

	private IVoiceRecognitionHandling callerClass;
	private VoiceRecognitionListener voiceListener;

	public RecogListener(IVoiceRecognitionHandling callerClass) {
		this.callerClass = callerClass;

	}

	public void setVoiceRecognitionListener(VoiceRecognitionListener voiceListener) {
		this.voiceListener = voiceListener;
	}

	@Override
	public void onReadyForSpeech(Bundle params) {

	}

	@Override
	public void onBeginningOfSpeech() {

	}

	@Override
	public void onBufferReceived(byte[] buffer) {

	}

	@Override
	public void onRmsChanged(float rmsdB) {
		String s = String.format("recieve : % 2.2f[dB]", rmsdB);
		if (voiceListener != null)
			voiceListener.onRmsChanged(rmsdB);

	}

	@Override
	public void onEndOfSpeech() {

		callerClass.getHandler().postDelayed(callerClass.getRecognizeSpeechThread(), 500);
	}

	@Override
	public void onError(int error) {

		switch (error) {
		case SpeechRecognizer.ERROR_AUDIO:

			break;
		case SpeechRecognizer.ERROR_CLIENT:

			break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:

			break;
		case SpeechRecognizer.ERROR_NETWORK:

			break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:

			break;
		case SpeechRecognizer.ERROR_NO_MATCH:

			callerClass.getHandler().postDelayed(callerClass.getRecognizeSpeechThread(), 1000);
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:

			callerClass.getHandler().postDelayed(callerClass.getRecognizeSpeechThread(), 1000);
			break;
		case SpeechRecognizer.ERROR_SERVER:

			break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:

			callerClass.getHandler().postDelayed(callerClass.getRecognizeSpeechThread(), 1000);
			break;
		default:
		}
	}

	@Override
	public void onEvent(int eventType, Bundle params) {

	}

	@Override
	public void onPartialResults(Bundle partialResults) {

	}

	@Override
	public void onResults(Bundle data) {

		ArrayList<String> results = data.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

		if (voiceListener != null && !results.isEmpty())
			voiceListener.onResults(results.get(0));

		callerClass.startRecognizeSpeech();
	}
}
