package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.jwt.JWTUtils;
import com.example.lmsbackend.dto.request.file.CreateShareFileTokenRequest;
import com.example.lmsbackend.dto.request.file.DecodeShareFileTokenRequest;
import com.example.lmsbackend.exceptions.client.ExpiredShareFileTokenException;
import com.example.lmsbackend.exceptions.client.InvalidShareFileTokenException;
import com.example.lmsbackend.exceptions.jwt.ExpiredJwtException;
import com.example.lmsbackend.exceptions.jwt.JwtInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final JWTUtils jwtUtils;

    public String createShareFileToken(CreateShareFileTokenRequest request) {
        return jwtUtils.generateShareFileToken(request);
    }

    public List<String> decodeShareFileToken(DecodeShareFileTokenRequest request){
        try {
            return  jwtUtils.getFilePaths(request.getToken());
        } catch (ExpiredJwtException exception){
            throw new ExpiredShareFileTokenException();
        } catch (JwtInvalidException exception){
            throw new InvalidShareFileTokenException();
        }
    }
}
