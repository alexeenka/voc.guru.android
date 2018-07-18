package guru.voc.vocguru;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK;
import static android.webkit.WebSettings.LOAD_NO_CACHE;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private AppTextToSpeech appTTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.mainView);
        webView.setWebViewClient(new AppWebViewClient());

        WebChromeClient client = new WebChromeClient();
        webView.setWebChromeClient(client);

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setCacheMode(LOAD_NO_CACHE);


        appTTS = new AppTextToSpeech(this);
        webView.addJavascriptInterface(appTTS, "speechSynthesis");
        webView.loadUrl("https://voc.guru");
        //webView.loadUrl("https://voc.guru:9080");
        //webView.loadUrl("http://10.0.2.2:8082/");
    }

    private class AppChromeClient extends WebChromeClient {


    }

    @Override
    protected void onDestroy() {
        appTTS.shutdown();
        super.onDestroy();
    }

    private class AppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();
            if (host.equals("voc.guru")) {
                return false;
            }


            if (host.contains("youtube.com")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

//            if (host.equals("voc.guru")
//                    || host.equals("m.facebook.com")
//                    || url.equals("https://www.facebook.com/dialog/oauth?client_id=193614377664302&scope=public_profile,user_friends&redirect_uri=https://voc.guru/fb-auth")
//                    || host.equals("vk.com")) {
//                // This is my web site, so do not override; let my WebView load the page
//                return false;
//            }
//
//            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
//            return true;

            return false;
        }
    }
}
