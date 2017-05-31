package listeners;

import android.os.Handler;

public interface IVoiceRecognitionHandling {
	Runnable getRecognizeSpeechThread();

	Handler getHandler();

	void startRecognizeSpeech();
	
}
