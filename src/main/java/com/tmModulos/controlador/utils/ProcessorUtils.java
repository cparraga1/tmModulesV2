package com.tmModulos.controlador.utils;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ProcessorUtils {

    public static final String CALCULO_PROMEDIO = "Promedio";
    public static final String CALCULO_MODA = "Moda";
    public static final String CALCULO_MINIMO = "Minimo";
    public static final String CALCULO_MAXIMO = "Maximo";

    public void copyFile(String fileName, InputStream in, String destination) {
        try {

            destination= destination+fileName;
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
