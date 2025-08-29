/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labfilee;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Comandos {
    private File carpeta;

    public Comandos(File inicio) {
        carpeta = inicio;
    }

    public String getRuta() {
        return carpeta.getAbsolutePath();
    }

    public String ejecutar(String linea) {
        String[] partes = separar(linea);
        String cmd = partes[0];
        String arg = partes[1];

        if (igual(cmd, "mkdir")) return mkdir(arg);
        if (igual(cmd, "mfile")) return mfile(arg);
        if (igual(cmd, "rm")) return rm(arg);
        if (igual(cmd, "cd")) return cd(arg);
        if (cmd.equals("...")) return atras();
        if (igual(cmd, "dir")) return dir();
        if (igual(cmd, "date")) return fecha();
        if (igual(cmd, "time")) return hora();
        if (igual(cmd, "rd")) return leer(arg);
        if (igual(cmd, "wr")) return "Uso: wr archivo.ext";
        return "Comando no reconocido.";
    }

    private String[] separar(String linea) {
        String cmd = linea.trim();
        String arg = "";
        int i = cmd.indexOf(' ');
        if (i != -1) {
            arg = cmd.substring(i + 1).trim();
            cmd = cmd.substring(0, i).trim();
        }
        return new String[]{cmd.toLowerCase(), arg};
    }

    private boolean igual(String a, String b) {
        return a != null && a.equalsIgnoreCase(b);
    }

    public String mkdir(String nombre) {
        if (nombre.isEmpty()) return "Uso: mkdir nombre";
        File f = new File(carpeta, nombre);
        if (f.exists()) return "Ya existe.";
        boolean ok = f.mkdirs();
        return ok ? "Carpeta creada." : "No se pudo crear.";
    }

    public String mfile(String nombre) {
        if (nombre.isEmpty()) return "Uso: mfile nombre.ext";
        File f = new File(carpeta, nombre);
        try {
            if (f.exists()) return "Ya existe.";
            boolean ok = f.createNewFile();
            return ok ? "Archivo creado." : "No se pudo crear.";
        } catch (Exception e) {
            return "Error al crear.";
        }
    }

    public String rm(String nombre) {
        if (nombre.isEmpty()) return "Uso: rm nombre";
        File f = new File(carpeta, nombre);
        if (!f.exists()) return "No existe.";
        borrarRec(f);
        return "Eliminado.";
    }

    private void borrarRec(File f) {
        if (f.isDirectory()) {
            File[] hijos = f.listFiles();
            if (hijos != null) {
                for (File h : hijos) borrarRec(h);
            }
        }
        f.delete();
    }

    public String cd(String nombre) {
        if (nombre.isEmpty()) return "Uso: cd carpeta";
        if (nombre.equals("..")) return atras();
        File destino = new File(carpeta, nombre);
        if (!destino.exists() || !destino.isDirectory()) return "Carpeta no encontrada.";
        carpeta = destino;
        return "";
    }

    public String atras() {
        File padre = carpeta.getParentFile();
        if (padre == null) return "No hay carpeta superior.";
        carpeta = padre;
        return "";
    }

    public String dir() {
        File[] arr = carpeta.listFiles();
        if (arr == null || arr.length == 0) return "Vacio.";
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (File f : arr) {
            if (f.isDirectory()) {
                sb.append("<DIR> ").append(f.getName()).append("\n");
            } else {
                sb.append(tamano(f.length())).append(" ").append(f.getName()).append("\n");
            }
        }
        return sb.toString().trim();
    }

    private String tamano(long n) {
        return String.valueOf(n);
    }

    public String fecha() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now().format(fmt);
    }

    public String hora() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.now().format(fmt);
    }

    public String escribir(String nombre, String texto) {
        if (nombre.isEmpty()) return "Uso: wr archivo.ext";
        File f = new File(carpeta, nombre);
        if (!f.exists() || f.isDirectory()) return "Archivo no encontrado.";
        try {
            FileWriter w = new FileWriter(f, true);
            w.write(texto + System.lineSeparator());
            w.close();
            return "Texto guardado.";
        } catch (Exception e) {
            return "Error al escribir.";
        }
    }

    public String leer(String nombre) {
        if (nombre.isEmpty()) return "Uso: rd archivo.ext";
        File f = new File(carpeta, nombre);
        if (!f.exists() || f.isDirectory()) return "Archivo no encontrado.";
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            String s = sb.toString().trim();
            if (s.isEmpty()) return "(vacio)";
            return s;
        } catch (Exception e) {
            return "Error al leer.";
        }
    }
}

