package com.schwarzsword.pip.coursework.security.Loggers;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.security.AbstractAuthorizationAuditListener;
import org.springframework.security.access.event.AbstractAuthorizationEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExposeAttemptedPathAuthorizationAuditListener
        extends AbstractAuthorizationAuditListener {

    private static final String AUTHORIZATION_FAILURE
            = "AUTHORIZATION_FAILURE";

    @Override
    public void onApplicationEvent(AbstractAuthorizationEvent event) {
        if (event instanceof AuthorizationFailureEvent) {
            onAuthorizationFailureEvent((AuthorizationFailureEvent) event);
        }
    }

    private void onAuthorizationFailureEvent(
            AuthorizationFailureEvent event) {
        Map<String, Object> data = new HashMap<>();
        data.put(
                "type", event.getAccessDeniedException().getClass().getName());
        data.put("message", event.getAccessDeniedException().getMessage());
        data.put("attrs", event.getConfigAttributes().toString());
        data.put("roles", event.getAuthentication().getAuthorities().toString());
        data.put("cred", event.getAuthentication().getCredentials());

        if (event.getAuthentication().getDetails() != null) {
            data.put("details",
                    event.getAuthentication().getDetails());
        }
        publish(new AuditEvent(event.getAuthentication().getName(),
                AUTHORIZATION_FAILURE, data));
    }
}