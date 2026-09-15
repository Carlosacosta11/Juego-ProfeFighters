package profefighter;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Pantalla de selección de personajes. Se usa dos veces (Jugador 1 y luego
 * Jugador 2): el jugador elige explícitamente su profesor haciendo clic en
 * una tarjeta, ya no se asigna al azar.
 */
public class PantallaSeleccion extends JPanel {

    public interface OnSeleccionCompleta {
        void completado(String idPersonaje1, String idPersonaje2);
    }

    private final BattleController controller;
    private final OnSeleccionCompleta callback;
    private final JLabel titulo;
    private String idJugador1 = null;

    public PantallaSeleccion(BattleController controller, OnSeleccionCompleta callback) {
        this.controller = controller;
        this.callback = callback;

        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 24));

        JLabel encabezado = new JLabel("P R O F E F I G H T E R", SwingConstants.CENTER);
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 34));
        encabezado.setForeground(new Color(240, 200, 60));
        encabezado.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(encabezado, BorderLayout.NORTH);

        titulo = new JLabel("", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(encabezado, BorderLayout.NORTH);
        norte.add(titulo, BorderLayout.SOUTH);
        add(norte, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 24, 24));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(10, 60, 30, 60));

        for (Map.Entry<String, PersonajeFactory> entrada : controller.getFabricasDisponibles().entrySet()) {
            grid.add(crearTarjeta(entrada.getKey(), entrada.getValue()));
        }

        add(grid, BorderLayout.CENTER);
        mostrarTurno(1);
    }

    private void mostrarTurno(int numeroJugador) {
        titulo.setText("Selección de personaje — Jugador " + numeroJugador);
    }

    private JPanel crearTarjeta(String id, PersonajeFactory fabrica) {
        Personaje muestra = fabrica.crearPersonaje();
        PersonajeMeta.Info meta = PersonajeMeta.of(id);

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(32, 32, 42));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(meta.color, 2),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel badge = new JLabel(meta.inicial, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(meta.color);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("SansSerif", Font.BOLD, 22));
        badge.setPreferredSize(new Dimension(48, 48));
        badge.setMaximumSize(new Dimension(48, 48));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombre = new JLabel(muestra.getNombre(), SwingConstants.CENTER);
        nombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        nombre.setForeground(Color.WHITE);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        nombre.setBorder(BorderFactory.createEmptyBorder(8, 0, 2, 0));

        JLabel materia = new JLabel(meta.materia, SwingConstants.CENTER);
        materia.setFont(new Font("SansSerif", Font.ITALIC, 13));
        materia.setForeground(new Color(180, 180, 190));
        materia.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel stats = new JLabel("<html><div style='text-align:center;'>"
                + "Vida: " + muestra.getVidaMaxima() + "<br>"
                + muestra.getNombreAtaqueBasico() + " (-" + muestra.getDanoBasico() + ")<br>"
                + muestra.getNombreAtaqueEspecial() + " (-" + muestra.getDanoEspecial() + ")"
                + "</div></html>", SwingConstants.CENTER);
        stats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        stats.setForeground(new Color(210, 210, 220));
        stats.setAlignmentX(Component.CENTER_ALIGNMENT);
        stats.setBorder(BorderFactory.createEmptyBorder(8, 0, 10, 0));

        JButton elegir = new JButton("Elegir");
        elegir.setAlignmentX(Component.CENTER_ALIGNMENT);
        elegir.setFocusPainted(false);
        elegir.setBackground(meta.color);
        elegir.setForeground(Color.WHITE);
        elegir.addActionListener(e -> onElegir(id));

        tarjeta.add(badge);
        tarjeta.add(nombre);
        tarjeta.add(materia);
        tarjeta.add(stats);
        tarjeta.add(elegir);

        return tarjeta;
    }

    private void onElegir(String id) {
        if (idJugador1 == null) {
            idJugador1 = id;
            mostrarTurno(2);
        } else {
            String idJugador2 = id;
            callback.completado(idJugador1, idJugador2);
        }
    }
}
