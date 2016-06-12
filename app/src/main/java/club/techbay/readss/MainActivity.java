package club.techbay.readss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener;
import com.mopub.mobileads.MoPubErrorCode;

import android.view.View;
import android.widget.Button;

import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyVideoAd;

public class MainActivity extends AppCompatActivity implements AdColonyAdAvailabilityListener, AdColonyAdListener, InterstitialAdListener {
    final private String APP_ID  = "app17ac68314d3d4ec79a";
    final private String ZONE_ID = "vz4a146b857ab94aa699";
    private AdColonyVideoAd ad;

    private WebView mWebView;
    private MoPubView moPubView;
    MoPubInterstitial mInterstitial;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView)findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("http://videos.techbay.club");
        //如果页面中链接,如果希望点击链接继续在当前browser中响应,而不是新开Android的系统browser中响应该链接,必须覆盖 WebView的WebViewClient对象
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        initBannerAd();
        initVideoAd();

        mInterstitial = new MoPubInterstitial(this, "6c88e3c8c4da43da9cab76caf5d9e3b5");
        mInterstitial.setInterstitialAdListener(this);
        mInterstitial.load();
    }


    private void initBannerAd() {
        final String adUnitId = "497c211508fb480386cb5e2a500c0f01";
        moPubView = (MoPubView) findViewById(R.id.adview);
        moPubView.setAdUnitId(adUnitId);
        moPubView.setAutorefreshEnabled(true);
        moPubView.loadAd();
    }

    private void initVideoAd() {
        AdColony.configure( this, "version:1.0,store:google", APP_ID, ZONE_ID );
        AdColony.addAdAvailabilityListener( this );

        button = (Button)findViewById( R.id.button );
        button.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AdColonyVideoAd ad = new AdColonyVideoAd( ZONE_ID ).withListener( MainActivity.this );
                ad.show();
            }
        } );

        //If ads are already ready for zone (i.e. if onCreate is happening for a second time) we want
        //to make sure our button is enabled.
        if (AdColony.statusForZone( ZONE_ID ).equals( "active" ))
        {
            button.setEnabled( true );
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        mInterstitial.destroy();
        super.onDestroy();
        if (moPubView != null) {
            moPubView.destroy();
            moPubView = null;
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AdColony.pause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        AdColony.resume( this );
    }

    @Override
    public void onAdColonyAdAvailabilityChange( final boolean available, String zone_id )
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                if (available)
                {
                    button.setEnabled( true );
                }
                else
                {
                    button.setEnabled( false );
                }
            }
        } );
    }

    @Override
    public void onAdColonyAdAttemptFinished( AdColonyAd ad )
    {
        //Can use the ad object to determine information about the ad attempt:
        //ad.shown();
        //ad.notShown();
        //ad.canceled();
        //ad.noFill();
        //ad.skipped();
    }

    @Override
    public void onAdColonyAdStarted( AdColonyAd ad )
    {
        //Called when the ad has started playing
    }

    public void backHome(View view){
        mWebView.loadUrl("http://videos.techbay.club");
    }



    // InterstitialAdListener methods
    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
        //logToast(getActivity(), "Interstitial clicked.");
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        if (interstitial.isReady()) {
            mInterstitial.show();
        } else {
            // Other code
        }
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {}

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {}

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {}

}
