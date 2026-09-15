package profefighter;

import javax.swing.*;
import java.awt.*;

/** Pantalla final: anuncia al ganador y permite reiniciar o salir. */
public class PantallaResultado extends JPanel {

    public PantallaResultado(Luchador ganador, Runnable onJugarDeNuevo, Runnable onSalir) {
        setLayout(new GridBagLayout());
        setBackground(new Color(18, 18, 24));

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JLabel trofeo = new JLabel("\uD83C\uDFC6", SwingConstants.CENTER);
        trofeo.setFont(new Font("SansSerif", Font.PLAIN, 60));
        trofeo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("¡" + ganador.getNombreJugador() + " gana el combate!", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(new Color(255, 220, 80));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));

        JLabel subtitulo = new JLabel("Personaje: " + ganador.getPersonaje().getNombre(), SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JButton reiniciar = new JButton("Jugar de nuevo");
        reiniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        reiniciar.addActionListener(e -> onJugarDeNuevo.run());

        JButton salir = new JButton("Salir");
        salir.setAlignmentX(Component.CENTER_ALIGNMENT);
        salir.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        salir.addActionListener(e -> onSalir.run());

        contenido.add(trofeo);
        contenido.add(titulo);
        contenido.add(subtitulo);
        contenido.add(reiniciar);
        contenido.add(Box.createVerticalStrut(8));
        contenido.add(salir);

        add(contenido);
    }
}
