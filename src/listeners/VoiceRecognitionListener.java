package listeners;

public interface VoiceRecognitionListener {

	void onRmsChanged(float voiceLevel);	
	void onResults(String results);
}
