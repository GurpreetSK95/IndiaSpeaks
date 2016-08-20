package gurpreetsk.me.indiaspeaks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = (WebView) findViewById(R.id.webview);
        String ministerName = "SukhbirSinghDalal";

        ministerName = getIntent().getStringExtra("MinisterName");
        ministerName = ministerName.replace(" ", "");

        webView.loadUrl("http://delhiassembly.nic.in/aspfile/whos_who/VIthAssembly/WhosWho/"+ministerName+".htm");

    }
}