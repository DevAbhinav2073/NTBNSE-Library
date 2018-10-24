package com.susankya.ntbnse_library;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response response = chain.proceed(request);

        if (response.body()==null) {
            Request newRequest = request.newBuilder().build();
            // retry the request
            return chain.proceed(newRequest);
        }

        // otherwise just pass the original response on
        return response;
    }

}