package com.redhat.cleanbase.security.flow;

public enum SecurityFlowType {
    MONOLITHIC_FORM_LOGIN,
    FRONTEND_BACKEND_SEPARATION_SESSION,
    FRONTEND_BACKEND_SEPARATION_JWT,
    OAUTH2_CLIENT,
    OAUTH2_RESOURCE_SERVER
}