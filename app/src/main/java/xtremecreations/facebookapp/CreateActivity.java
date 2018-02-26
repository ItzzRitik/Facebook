package xtremecreations.facebookapp;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity
{
    WebView wb;
    int page=0;
    ProgressBar PB;
    RelativeLayout RL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpannableString title = new SpannableString("Find Your Account");
        title.setSpan(new TypefaceSpan("sans-serif"), 0, title.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        title.setSpan(new AbsoluteSizeSpan(18,true), 0, title.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        CreateActivity.this.getSupportActionBar().setTitle(title);
        CreateActivity.this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3B5998")));
        CreateActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CreateActivity.this.getSupportActionBar().setElevation(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){CreateActivity.this.getWindow().setStatusBarColor(ContextCompat.getColor(CreateActivity.this, R.color.status));}
        setContentView(R.layout.activity_forgot);
        PB=(ProgressBar)findViewById(R.id.progressBar2);
        RL=(RelativeLayout)findViewById(R.id.splash);
        PB.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        wb=(WebView)findViewById(R.id.webView);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebChromeClient(new WebChromeClient());
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(CreateActivity.this, "Error Occured", Toast.LENGTH_LONG).show();finish();}
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                RL.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView v, String url) {
                if (page == 0) {
                    if (!getIntent().getExtras().getString("id").equals("")) {
                        wb.loadUrl("javascript: (function() {document.getElementsByName(\"email\")[0].value = \"" + getIntent().getExtras().getString("id") + "\";}) ();");
                        wb.loadUrl("javascript:(function(){" + "l=document.getElementById('did_submit');" + "e=document.createEvent('HTMLEvents');" +
                                "e.initEvent('click',true,true);" + "l.dispatchEvent(e);" + "})()");
                        page = 1;
                    } else {
                        page = 1;
                        RL.setVisibility(View.GONE);
                    }
                } else if (page == 1) {
                    SpannableString title = new SpannableString("Confirm Your Account");
                    title.setSpan(new TypefaceSpan("sans-serif"), 0, title.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    title.setSpan(new AbsoluteSizeSpan(18, true), 0, title.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    CreateActivity.this.getSupportActionBar().setTitle(title);
                    if (wb.getUrl().equals("https://m.facebook.com/login/identify/?ctx=recover")) {
                        Toast.makeText(CreateActivity.this, "Invalid Email ID", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        RL.setVisibility(View.GONE);
                    }
                }
            }
        });
        wb.loadUrl("https://m.facebook.com/login/identify");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
