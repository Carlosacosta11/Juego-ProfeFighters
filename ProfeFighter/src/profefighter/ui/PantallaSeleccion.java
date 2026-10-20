package profefighter.ui;

import profefighter.fachada.TarjetaProfesor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Pantalla de selección de personajes. Se usa dos veces (Jugador 1 y luego
 * Jugador 2): el jugador elige explícitamente su profesor haciendo clic en
 * una tarjeta, ya no se asigna al azar.
 *
 * Clase de interfaz: no participa en el patrón Facade ni conoce ninguna fachada.
 * Main (el Client) le entrega ya la lista de TarjetaProfesor obtenida de PartidaFacade.
 */
public class PantallaSeleccion extends JPanel {

    public interface OnSeleccionCompleta {
        void completado(String idPersonaje1, String idPersonaje2);
    }

    private final OnSeleccionCompleta callback;
    private final JLabel titulo;
    private String idJugador1 = null;

    public PantallaSeleccion(List<TarjetaProfesor> tarjetas, OnSeleccionCompleta callback) {
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

        for (TarjetaProfesor tarjeta : tarjetas) {
            grid.add(crearTarjeta(tarjeta));
        }

        add(grid, BorderLayout.CENTER);
        mostrarTurno(1);
    }

    private void mostrarTurno(int numeroJugador) {
        titulo.setText("Selección de personaje — Jugador " + numeroJugador);
    }

    private JPanel crearTarjeta(TarjetaProfesor datos) {
        String id = datos.getId();

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(32, 32, 42));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(datos.getColor(), 2),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel badge = new JLabel(datos.getInicial(), SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(datos.getColor());
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("SansSerif", Font.BOLD, 22));
        badge.setPreferredSize(new Dimension(48, 48));
        badge.setMaximumSize(new Dimension(48, 48));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombre = new JLabel(datos.getNombre(), SwingConstants.CENTER);
        nombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        nombre.setForeground(Color.WHITE);
        nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        nombre.setBorder(BorderFactory.createEmptyBorder(8, 0, 2, 0));

        JLabel materia = new JLabel(datos.getMateria(), SwingConstants.CENTER);
        materia.setFont(new Font("SansSerif", Font.ITALIC, 13));
        materia.setForeground(new Color(180, 180, 190));
        materia.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel stats = new JLabel("<html><div style='text-align:center;'>"
                + datos.getResumen().replace("\n", "<br>")
                + "</div></html>", SwingConstants.CENTER);
        stats.setFont(new Font("SansSerif", Font.PLAIN, 13));
        stats.setForeground(new Color(210, 210, 220));
        stats.setAlignmentX(Component.CENTER_ALIGNMENT);
        stats.setBorder(BorderFactory.createEmptyBorder(8, 0, 10, 0));

        JButton elegir = new JButton("Elegir");
        elegir.setAlignmentX(Component.CENTER_ALIGNMENT);
        elegir.setFocusPainted(false);
        elegir.setBackground(datos.getColor());
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