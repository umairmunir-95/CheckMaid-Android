package com.mindclarity.checkmaid.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mindclarity.checkmaid.R;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassword extends AsyncTask<Void,Void,Void> {

    private Context context;
    private Session session;

    private String email;
    private String subject;
    private String message;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private EditText etPassword,etConfirmPasswrd;
    private Button btnChangePassword,btnSubmit;

    public ForgotPassword(Context context, ProgressBar progressBar, String email, String subject, String message, EditText etPassword, EditText etConfirmPassword, Button btnChangePassword,Button btnSubmit){
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.progressBar=progressBar;
        this.etPassword=etPassword;
        this.etConfirmPasswrd=etConfirmPassword;
        this.btnChangePassword=btnChangePassword;
        this.btnSubmit=btnSubmit;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,context.getResources().getString(R.string.verifying_email),context.getResources().getString(R.string.please_wait),false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        DialogManager.showForgetPasswordVerificationPinPopuop(context,progressBar,etPassword,etConfirmPasswrd,btnChangePassword,btnSubmit);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.privateemail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(CredentialsManager.EMAIL, CredentialsManager.PASSWORD);
                    }
                });

        try
        {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(CredentialsManager.EMAIL));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);
            mm.setText(message);
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

