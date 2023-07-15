package com.example.lmsbackend.client.google;

import com.example.lmsbackend.config.google.GcpConfig;
import com.example.lmsbackend.config.google.GoogleDnsApiConfig;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dns.Dns;
import com.google.api.services.dns.model.Change;
import com.google.api.services.dns.model.ResourceRecordSet;
import com.google.auth.http.HttpCredentialsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleDNSClient {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static Dns dns;

    private final GcpConfig gcpConfig;

    @Autowired
    private GoogleDnsApiConfig config;

    @PostConstruct
    public void getUpStreamDnsService() {
        // Build a new authorized API client service.
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(config.getGoogleCredential());
            // * Set up the calendar service
            dns = new Dns.Builder(httpTransport, JSON_FACTORY, requestInitializer).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createNewSubdomain(String subdomain) {
        ResourceRecordSet resourceRecordSet = new ResourceRecordSet();
        resourceRecordSet.setName(subdomain + "." + gcpConfig.getDns().getName());
        resourceRecordSet.setType("CNAME");
        resourceRecordSet.setRrdatas(List.of(gcpConfig.getDns().getName()));
        resourceRecordSet.setTtl(30);

        Change change = new Change();
        change.setAdditions(List.of(resourceRecordSet));
        try {
            Dns.Changes.Create request = dns.changes().create(gcpConfig.getProjectId(), gcpConfig.getDns().getManagedZone(), change);
            request.execute();
            return true;
        } catch (IOException e) {
            log.error("Can not create domain", e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkExisted(String domain) {
        try {
            dns.resourceRecordSets().get(gcpConfig.getProjectId(), gcpConfig.getDns().getManagedZone(), domain + gcpConfig.getDns().getName(), "CNAME").execute();
            return true;
        } catch (IOException e) {
            log.info("Domain not existed");
            return false;
        }
    }
}
