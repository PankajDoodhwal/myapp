package com.example.myapp.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedOutputStream = new ByteArrayOutputStream();
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

    public CachedBodyHttpServletResponse(HttpServletResponse response) throws IOException {
        super(response);

        outputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {}

            @Override
            public void write(int b) {
                cachedOutputStream.write(b);
            }
        };

        writer = new PrintWriter(cachedOutputStream, true, response.getCharacterEncoding() != null ?
                java.nio.charset.Charset.forName(response.getCharacterEncoding()) :
                java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public byte[] getBody() {
        return cachedOutputStream.toByteArray();
    }

    public String getBodyAsString() {
        return new String(getBody(), java.nio.charset.StandardCharsets.UTF_8);
    }

    public void copyBodyToResponse() throws IOException {
        ServletOutputStream out = super.getOutputStream();
        out.write(getBody());
        out.flush();
    }

    public byte[] getCaptureAsBytes() throws IOException {
        writer.flush();
        return cachedOutputStream.toByteArray();
    }
}
