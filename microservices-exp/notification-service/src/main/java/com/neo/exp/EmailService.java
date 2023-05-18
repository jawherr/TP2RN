package com.neo.exp;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Collections;

@Path("/email")
public class EmailService {
    @Inject
    Mailer mailer;

    @Inject
    ReactiveMailer reactiveMailer;

    @GET
    public void sendEmail(String toEmail,
                          String subject,
                          String body) {
        Mail mail = new Mail()
                .setFrom("jawa.kalel@gmail.com")
                .setTo(Collections.singletonList(toEmail))
                .setSubject(subject)
                .setText(body);
        mailer.send(mail);
    }

    @GET
    public Uni<Void> sendEmailReactive(String toEmail,
                                       String subject,
                                       String body) {
        Mail mail = new Mail()
                .setFrom("jawa.kalel@gmail.com")
                .setTo(Collections.singletonList(toEmail))
                .setCc(Collections.singletonList("kallel.jawher@gmail.com"))
                .setSubject(subject)
                .setText(body);
        return reactiveMailer.send(mail);
    }
}
