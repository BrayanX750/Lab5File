/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package labfilee;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/**
 *
 * @author Usuario
 */
public class LabFilee extends JFrame {

    private JTextArea consola;
    private int posicionPrompt;
    private File directorioActual;
    private Comandos comandos;

    public LabFilee() {
        setTitle("Simbolo del sistema");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        consola = new JTextArea();
        
        consola.setBackground(Color.BLACK);
        consola.setForeground(Color.WHITE);
        consola.setFont(new Font("Consolas", Font.PLAIN, 14));
        consola.setCaretColor(Color.WHITE);
        consola.setLineWrap(true);
        consola.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(consola);
        add(scroll, BorderLayout.CENTER);

        directorioActual = new File(System.getProperty("user.dir"));
        comandos = new Comandos(consola);

        consola.append("Microsoft Windows [Version 10.21.3123.12]\n");
        consola.append("(c) Mentiritas Corporation. Todos los derechos reservados.\n");
        consola.append("Ingrese el comando help para ver sintaxis y funcionalidades. \n\n");
        mostrarPrompt();

        consola.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && consola.getCaretPosition() <= posicionPrompt) {
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    String texto = consola.getText();
                    String comando = texto.substring(posicionPrompt).trim();
                    consola.append("\n");
                    ejecutar(comando);
                    mostrarPrompt();
                }
            }
        });
    }

    private void mostrarPrompt() {
        consola.append(directorioActual.getAbsolutePath() + "> ");
        posicionPrompt = consola.getDocument().getLength();
        consola.setCaretPosition(posicionPrompt);
    }

    private void ejecutar(String comando) {
        comando = comando.toLowerCase();
        if (comando.startsWith("mkdir ")) {
            comandos.crearCarpeta(directorioActual, comando.substring(6).trim());
        } else if (comando.startsWith("mfile ")) {
            comandos.crearArchivo(directorioActual, comando.substring(6).trim());
        } else if (comando.startsWith("rm ")) {
            comandos.eliminar(directorioActual, comando.substring(3).trim());
        } else if (comando.equals("dir")) {
            comandos.listar(directorioActual);
        } else if (comando.equals("cd ...")) {   
            File nuevo = comandos.irAtras(directorioActual);
            if (nuevo != null) {
                directorioActual = nuevo;
            }
        } else if (comando.startsWith("cd ")) {  
            File nuevo = comandos.cambiarDirectorio(directorioActual, comando.substring(3).trim());
            if (nuevo != null) {
                directorioActual = nuevo;
            }
        } else if (comando.equals("date")) {
            comandos.mostrarFecha();
        } else if (comando.equals("time")) {
            comandos.mostrarHora();
        } else if (comando.startsWith("wr ")) {
            String[] partes = comando.substring(3).trim().split(" ", 2);
            if (partes.length == 2) {
                String nombreArchivo = partes[0];
                String texto = partes[1];
                comandos.escribirArchivo(directorioActual, nombreArchivo, texto);
            } else {
                consola.append("Sintaxis: wr <archivo> <texto>\n");
            }
        } else if (comando.startsWith("rd ")) {
            comandos.leerArchivo(directorioActual, comando.substring(3).trim());
        } else if (comando.equals("help")) {
            comandos.mostrarAyuda();
        } else if (!comando.isEmpty()) {
            consola.append("Comando no reconocido: " + comando + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LabFilee().setVisible(true));
    }
}
