package com.example.carrental.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void sendAsJson(HttpServletResponse response, Object obj){
        try {
            response.setContentType("application/json");
            String json = mapper.writeValueAsString(obj);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write(e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}