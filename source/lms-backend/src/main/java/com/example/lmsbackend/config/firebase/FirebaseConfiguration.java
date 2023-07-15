package com.example.lmsbackend.config.firebase;

import com.example.lmsbackend.config.google.GoogleApiConfig;
import com.google.cloud.storage.StorageOptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
@Getter
@RequiredArgsConstructor
@PropertySource("classpath:firebase-storage.properties")
public class FirebaseConfiguration {

    private final Environment environment;
    private final GoogleApiConfig googleApiConfig;

    private StorageOptions storageOptions;
    private String bucketName;
    private String projectId;

    @PostConstruct
    private void initializeFirebase() {
        bucketName = environment.getRequiredProperty("FIREBASE_STORAGE_BUCKET");
        projectId = environment.getRequiredProperty("FIREBASE_PROJECT_ID");

        this.storageOptions = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(googleApiConfig.getFirebaseCredential()).build();

    }
}
