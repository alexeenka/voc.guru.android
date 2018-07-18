package guru.voc.vocguru;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AppTextToSpeech {

    public static final List<AppVoice> VOICES = Arrays.asList(new AppVoice("en-US"), new AppVoice("ru-RU"));
    public static final Gson GSON = new Gson();
    public static final String JSON_VOICES = GSON.toJson(VOICES);
    public static final Locale RU_LOCALE = new Locale("ru", "RU");

    private TextToSpeech tts;


    public AppTextToSpeech(Context context) {
        this.tts = new TextToSpeech(context, i -> {
            tts.setLanguage(RU_LOCALE);
            tts.speak("ВОК ГУРУ!", TextToSpeech.QUEUE_ADD, null);
            tts.setLanguage(Locale.US);
            tts.speak("Let's get started!", TextToSpeech.QUEUE_ADD, null);
        });
    }

    public void shutdown() {
        //Close the Text to Speech Library
        if(tts != null) {
            tts.stop();
            tts.shutdown();
            Log.d("LOG", "TTS Destroyed.");
        }
    }

    @JavascriptInterface
    public void speak(String message) {
        SpeechSynthesisUtterance utterance = GSON.fromJson(message, SpeechSynthesisUtterance.class);

        if ("en-US".equals(utterance.getLang())) {
            tts.setLanguage(Locale.US);
        } else if ("ru-RU".equals(utterance.getLang())) {
            tts.setLanguage(RU_LOCALE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.setLanguage(Locale.forLanguageTag(utterance.getLang()));
            } else {
                tts.setLanguage(Locale.US);
            }
        }

        tts.speak(utterance.getText(), TextToSpeech.QUEUE_ADD, null);

        Log.d("LOG", "Message from the webpage " + message);
    }

    @JavascriptInterface
    public void cancel() {
        if (tts.isSpeaking()) {
            tts.stop();
        }
    }

    @JavascriptInterface
    public String getVoices() {
        return JSON_VOICES;
    }

    @JavascriptInterface
    public void isVocAndroidNative() {
    }

    @JavascriptInterface
    public void onvoiceschanged() {
    }

}
