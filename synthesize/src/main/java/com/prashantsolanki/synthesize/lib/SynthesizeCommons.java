package com.prashantsolanki.synthesize.lib;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringDef;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Package com.prashantsolanki.synthesize.lib
 * <p>
 * Created by Prashant on 4/3/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */

public class SynthesizeCommons extends Synthesize {

    @LayoutRes
    int layoutRes;
    WebView webView;


    public SynthesizeCommons(Context context, @Commons String preDefLayouts){
        super(context);
        switch (preDefLayouts){
            case Commons.WEB_VIEW:
                layoutRes = R.layout.commons_web_view;
                setLayout(layoutRes);
                initWebView();
                break;
        }
    }

    private void initWebView(){
        webView=(WebView)getLayout().findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    public void setUrl(String url){
        if(webView==null)
            throw new RuntimeException("You must use Commons.WEB_VIEW to use webview");

        webView.loadUrl(url);
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({Commons.WEB_VIEW})
    public @interface Commons {
        String WEB_VIEW ="WebView";
    }
}
