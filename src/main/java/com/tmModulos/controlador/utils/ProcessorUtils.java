package com.tmModulos.controlador.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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





    public static String convertLongToTime(long tiempo) {
        return  String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(tiempo),
                TimeUnit.MILLISECONDS.toMinutes(tiempo) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(tiempo)),
                TimeUnit.MILLISECONDS.toSeconds(tiempo) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tiempo)));


    }
}
