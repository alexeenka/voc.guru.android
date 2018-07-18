package guru.voc.vocguru;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private AppTextToSpeech appTTS;


    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.mainView);
        webView.setWebViewClient(new AppWebViewClient());

        WebChromeClient client = new AppChromeClient(this);
        webView.setWebChromeClient(client);

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        //webSettings.setCacheMode(LOAD_NO_CACHE);


        appTTS = new AppTextToSpeech(this);
        webView.addJavascriptInterface(appTTS, "speechSynthesis");
        webView.loadUrl("https://voc.guru");
        //webView.loadUrl("https://voc.guru:9080");
        //webView.loadUrl("http://10.0.2.2:8082/");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else {
            Toast.makeText(this.getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
        }
    }

    private class AppChromeClient extends WebChromeClient {

        private final MainActivity mainActivity;

        public AppChromeClient(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
        {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
        {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try
            {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e)
            {
                uploadMessage = null;
                Toast.makeText(mainActivity.getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
        {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg)
        {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

    }


    @Override
    protected void onDestroy() {
        appTTS.shutdown();
        super.onDestroy();
    }

    private static final String[] URLS = {
            "http://www.collinsdictionary.com/dictionary/english/",
            "http://www.google.com/search?q=define:",
            "http://www.merriam-webster.com/dictionary/",
            "http://www.yourdictionary.com/",
            "http://www.urbandictionary.com/define.php?term=",
            "http://idioms.thefreedictionary.com/",
            "http://www.google.com/search?q=define:",
            "http://www.multitran.ru/c/m.exe?CL=1&l1=1&s=",
            "http://www.lingvo-online.ru/ru/Translate/en-ru/",
            "http://www.brainyquote.com/search_results.html?q=",
            "http://www.merriam-webster.com/dictionary/",
            "http://www.collinsdictionary.com/dictionary/english/",
            "http://www.google.com/search?q=define:",
            "http://sentence.yourdictionary.com/",
            "http://www.google.com/search?q=",
            "http://images.google.com/search?tbm=isch&q=",
            "https://translate.yandex.by/?lang=en-ru&text=",
            "https://translate.google.com/#en/ru/",
            "https://yandex.by/images/search?text="
    };

    private class AppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();
            if (host.equals("voc.guru")) {
                return false;
            }


            if (host.contains("youtube.com")) {
                return newIntent(url);
            }

            for (String iUrl : URLS) {
                if (url.startsWith(iUrl)) {
                    return newIntent(url);
                }
            }

            return false;
        }

        /**
         * Launch another Activity that handles URLs
         */
        private boolean newIntent(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}
