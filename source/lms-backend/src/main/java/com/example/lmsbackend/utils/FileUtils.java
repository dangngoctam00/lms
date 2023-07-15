package com.example.lmsbackend.utils;

import com.example.lmsbackend.config.firebase.FirebaseConfiguration;
import com.example.lmsbackend.exceptions.UnsupportedContentTypeException;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUtils {

    private final FirebaseConfiguration firebaseConfiguration;

    private static final String FILE_NAME_PATTERN = "(.*?)(?=\\?)";
    private static final String URL_PATTERN = "([^\\/]*)\\/*$";

    public String getFileExtensionFromUrl(String url) {
        var decodedUrl = decode(url);
        var pattern = Pattern.compile(URL_PATTERN);
        var lastPathItemMatcher = pattern.matcher(decodedUrl);
        if (lastPathItemMatcher.find()) {
            var lastPathItem = lastPathItemMatcher.group(0);
            var fileNameMatcher = Pattern.compile(FILE_NAME_PATTERN).matcher(lastPathItem);
            if (fileNameMatcher.find()) {
                var fileName = fileNameMatcher.group(0);
                return getFileExtensionFromFileName(fileName);
            }
        }
        return StringUtils.EMPTY;
    }

    public String getFileExtensionFromFileName(String fileName) {
        var parts = StringUtils.split(fileName, ".");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return StringUtils.EMPTY;
    }

    public String getFileName(String url) {
        var decodedUrl = decode(url);
        var pattern = Pattern.compile(URL_PATTERN);
        var lastPathItemMatcher = pattern.matcher(decodedUrl);
        if (lastPathItemMatcher.find()) {
            var lastPathItem = lastPathItemMatcher.group(0);
            var fileNameMatcher = Pattern.compile(FILE_NAME_PATTERN).matcher(lastPathItem);
            if (fileNameMatcher.find()) {
                return fileNameMatcher.group(0);
            }
        }
        return StringUtils.EMPTY;
    }

    public FileDto getFileFromFirebase(String url) {
        var fileName = getFileName(url);
        var extension = getFileExtensionFromFileName(fileName);
        var decodedUrl = decode(url);
        var fileNameIndex = StringUtils.lastIndexOf(decodedUrl, fileName);
        var path = StringUtils.substring(decodedUrl, StringUtils.indexOf(decodedUrl, "/o/") + "/o/".length(), fileNameIndex + fileName.length());
        var storage = firebaseConfiguration.getStorageOptions().getService();
        var blob = storage.get(BlobId.of(firebaseConfiguration.getBucketName(), path));
        ReadChannel reader = blob.reader();
        InputStream inputStream = Channels.newInputStream(reader);
        byte[] byteContent;
        try {
            byteContent = IOUtils.toByteArray(inputStream);
            final ByteArrayResource byteArrayResource = new ByteArrayResource(byteContent);
            return new FileDto(fileName, extension, byteArrayResource);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Cannot download file {} from firebase", url);
            return null;
        }
    }

    public String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.warn("Cannot decode attachment: {}", value);
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    public String getContentType(String extension) {
        switch (extension) {
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "csv":
                return "text/csv";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xls":
                return "application/vnd.ms-excel";
            default:
                throw new UnsupportedContentTypeException(extension);
        }
    }
}
