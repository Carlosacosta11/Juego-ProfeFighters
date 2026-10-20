package profefighter.ui;

import profefighter.composite.ElementoGrafico;
import profefighter.decorador.Combatiente;
import profefighter.juego.GameConfig;
import profefighter.juego.Luchador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Arena de combate en tiempo real: mueve a los dos luchadores según las
 * teclas presionadas, resuelve los ataques básicos/especiales, aplica los
 * potenciadores y muestra barras de vida y energía.
 *
 * El escenario (mundo destruido) ya no se dibuja aquí: es un árbol del patrón
 * Composite (ElementoGrafico / GrupoGrafico) que arma EscenarioMundoDestruido y
 * que esta clase pinta con una sola llamada, escenario.dibujar(g). Esta clase
 * es el cliente del Composite: solo conoce la interfaz ElementoGrafico.
 *
 * NOTA (SRP, ver sección 6.2 de la documentación): sigue mezclando dos
 * responsabilidades -leer el teclado y aplicar las reglas de combate-, y
 * además dibuja luchadores, popups y HUD. Se deja así a propósito para poder
 * señalarlo como incumplimiento real en la sustentación.
 */
public class ArenaPanel extends JPanel {

    private final Luchador jugador1;
    private final Luchador jugador2;
    private final Consumer<Luchador> onCombateTerminado;
    private final Set<Integer> teclasPresionadas = new HashSet<>();
    private final Timer timer;
    private final List<PopupDano> popups = new ArrayList<>();
    private final ElementoGrafico escenario;

    private long inicioMs;
    private boolean combateResuelto = false;
    private String mensajeGanador = null;

    public ArenaPanel(Luchador jugador1, Luchador jugador2, Consumer<Luchador> onCombateTerminado) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.onCombateTerminado = onCombateTerminado;

        setPreferredSize(new Dimension(GameConfig.ANCHO_ARENA, GameConfig.ALTO_ARENA));
        setFocusable(true);
        escenario = EscenarioMundoDestruido.crear();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                teclasPresionadas.add(e.getKeyCode());
                manejarAtaques(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                teclasPresionadas.remove(e.getKeyCode());
            }
        });

        inicioMs = System.currentTimeMillis();
        timer = new Timer(1000 / GameConfig.FPS, e -> tick());
        timer.start();
    }

    private void manejarAtaques(int codigo) {
        if (combateResuelto) return;
        long ahora = System.currentTimeMillis();
        if (ahora - inicioMs <= 1500) return; // mientras corre la cuenta regresiva no hay acciones

        if (codigo == GameConfig.P1_ATAQUE_BASICO) {
            atacar(jugador1, jugador2, false, ahora);
        } else if (codigo == GameConfig.P1_ATAQUE_ESPECIAL) {
            atacar(jugador1, jugador2, true, ahora);
        } else if (codigo == GameConfig.P2_ATAQUE_BASICO) {
            atacar(jugador2, jugador1, false, ahora);
        } else if (codigo == GameConfig.P2_ATAQUE_ESPECIAL) {
            atacar(jugador2, jugador1, true, ahora);
        } else if (codigo == GameConfig.P1_FURIA) {
            jugador1.activarFuria();
        } else if (codigo == GameConfig.P1_ESCUDO) {
            jugador1.activarEscudo();
        } else if (codigo == GameConfig.P2_FURIA) {
            jugador2.activarFuria();
        } else if (codigo == GameConfig.P2_ESCUDO) {
            jugador2.activarEscudo();
        }
        comprobarFinDeCombate();
    }

    /**
     * Ejecuta un ataque y, si conectó, muestra el daño realmente recibido por la
     * víctima (se mide por la diferencia de vida: así incluye lo que sumen o
     * resten los potenciadores).
     */
    private void atacar(Luchador atacante, Luchador victima, boolean especial, long ahora) {
        int vidaAntes = victima.getPersonaje().getVidaActual();
        boolean conecto = especial
                ? atacante.intentarAtaqueEspecial(victima, ahora)
                : atacante.intentarAtaqueBasico(victima, ahora);
        if (conecto) {
            agregarPopup(victima, vidaAntes - victima.getPersonaje().getVidaActual(), especial);
        }
    }

    private void agregarPopup(Luchador victima, int dano, boolean especial) {
        popups.add(new PopupDano(victima.getX() + GameConfig.ANCHO_PERSONAJE / 2.0,
                GameConfig.SUELO_Y - GameConfig.ALTO_PERSONAJE, "-" + dano, especial));
    }

    private void tick() {
        long ahora = System.currentTimeMillis();
        boolean enCombate = ahora - inicioMs > 1500; // pequeña cuenta regresiva inicial

        if (enCombate && !combateResuelto) {
            if (teclasPresionadas.contains(GameConfig.P1_IZQUIERDA)) {
                jugador1.mover(-GameConfig.VELOCIDAD_MOVIMIENTO, GameConfig.LIMITE_IZQUIERDO, GameConfig.LIMITE_DERECHO);
            }
            if (teclasPresionadas.contains(GameConfig.P1_DERECHA)) {
                jugador1.mover(GameConfig.VELOCIDAD_MOVIMIENTO, GameConfig.LIMITE_IZQUIERDO, GameConfig.LIMITE_DERECHO);
            }
            if (teclasPresionadas.contains(GameConfig.P2_IZQUIERDA)) {
                jugador2.mover(-GameConfig.VELOCIDAD_MOVIMIENTO, GameConfig.LIMITE_IZQUIERDO, GameConfig.LIMITE_DERECHO);
            }
            if (teclasPresionadas.contains(GameConfig.P2_DERECHA)) {
                jugador2.mover(GameConfig.VELOCIDAD_MOVIMIENTO, GameConfig.LIMITE_IZQUIERDO, GameConfig.LIMITE_DERECHO);
            }
            evitarSuperposicion();
            jugador1.actualizarOrientacion(jugador2);
            jugador2.actualizarOrientacion(jugador1);
        }

        for (int i = popups.size() - 1; i >= 0; i--) {
            PopupDano p = popups.get(i);
            p.avanzar();
            if (p.terminado()) popups.remove(i);
        }

        repaint();
    }

    private void evitarSuperposicion() {
        double dx = jugador2.getX() - jugador1.getX();
        double distanciaMinima = GameConfig.DISTANCIA_MINIMA;
        if (Math.abs(dx) < distanciaMinima) {
            double centro = (jugador1.getX() + jugador2.getX()) / 2.0;
            if (jugador1.getX() <= jugador2.getX()) {
                jugador1.mover((int) (centro - distanciaMinima / 2 - jugador1.getX()), GameConfig.LIMITE_IZQUIERDO, GameConfig.LIMITE_DERECHO);
                jugador2.mover((int) (centro + distanciaMinima / 2 - jugador2.getX()), GameConfig.LIMITE_IZQUIERDO, GameConfig.LIMITE_DERECHO);
            } else {
                jugador2.mover((int) (centro - distanciaMinima / 2 - jugador2.getX()), GameConfig.LIMITE_IZQUIERDO, GameConfig.LIMITE_DERECHO);
                jugador1.mover((int) (centro + distanciaMinima / 2 - jugador1.getX()), GameConfig.LIMITE_IZQUIERDO, GameConfig.LIMITE_DERECHO);
            }
        }
    }

    private void comprobarFinDeCombate() {
        if (combateResuelto) return;
        if (!jugador1.getPersonaje().estaVivo() || !jugador2.getPersonaje().estaVivo()) {
            combateResuelto = true;
            Luchador ganador = jugador1.getPersonaje().estaVivo() ? jugador1 : jugador2;
            mensajeGanador = "¡" + ganador.getPersonaje().getNombre() + " (" + ganador.getNombreJugador() + ") gana el combate!";
            Timer fin = new Timer(1400, e -> onCombateTerminado.accept(ganador));
            fin.setRepeats(false);
            fin.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        escenario.dibujar(g);
        dibujarLuchador(g, jugador1, jugador2);
        dibujarLuchador(g, jugador2, jugador1);
        dibujarPopups(g);
        dibujarHUD(g);

        long ahora = System.currentTimeMillis();
        if (ahora - inicioMs <= 1500) {
            dibujarCuentaRegresiva(g, ahora);
        }
        if (combateResuelto && mensajeGanador != null) {
            dibujarGanador(g);
        }
    }

    private void dibujarLuchador(Graphics2D g, Luchador l, Luchador oponente) {
        long ahora = System.currentTimeMillis();
        int px = (int) l.getX();
        int pyBase = GameConfig.SUELO_Y;
        int ancho = GameConfig.ANCHO_PERSONAJE;
        int alto = GameConfig.ALTO_PERSONAJE;

        // sombra
        g.setColor(new Color(0, 0, 0, 110));
        g.fillOval(px, pyBase - 8, ancho, 16);

        boolean flash = l.estaEnFlashGolpe(ahora);
        Color colorCuerpo = flash ? Color.WHITE : l.getColorEtiqueta();

        int torsoX = px + ancho / 4;
        int torsoY = pyBase - alto + 26;
        int torsoW = ancho / 2;
        int torsoH = alto - 46;

        // piernas
        g.setColor(colorCuerpo.darker());
        g.fillRoundRect(torsoX, torsoY + torsoH - 6, torsoW / 2 - 3, 30, 6, 6);
        g.fillRoundRect(torsoX + torsoW / 2 + 3, torsoY + torsoH - 6, torsoW / 2 - 3, 30, 6, 6);

        // torso
        g.setColor(colorCuerpo);
        g.fillRoundRect(torsoX, torsoY, torsoW, torsoH, 14, 14);

        // brazos (se extienden al atacar)
        int dir = l.isMirandoDerecha() ? 1 : -1;
        boolean atacando = l.estaAtacando(ahora);
        int extension = atacando ? 34 : 8;
        boolean especial = atacando && l.ultimoAtaqueEspecial();
        g.setStroke(new BasicStroke(especial ? 8f : 6f));
        g.setColor(especial ? new Color(255, 230, 90) : colorCuerpo.darker());
        int brazoY = torsoY + 14;
        g.drawLine(torsoX + torsoW / 2, brazoY, torsoX + torsoW / 2 + dir * extension, brazoY);

        if (especial) {
            g.setColor(new Color(255, 230, 90, 160));
            int cx = torsoX + torsoW / 2 + dir * (extension + 14);
            g.fillOval(cx - 16, brazoY - 16, 32, 32);
        }

        // cabeza
        int headSize = 30;
        int headX = torsoX + torsoW / 2 - headSize / 2;
        int headY = torsoY - headSize + 6;
        g.setColor(new Color(230, 200, 170));
        g.fill(new Ellipse2D.Double(headX, headY, headSize, headSize));

        // insignia con inicial
        int badge = 22;
        g.setColor(l.getColorEtiqueta());
        g.fillOval(headX + headSize / 2 - badge / 2, headY - badge - 4, badge, badge);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        FontMetrics fm = g.getFontMetrics();
        String txt = l.getInicial();
        g.drawString(txt, headX + headSize / 2 - fm.stringWidth(txt) / 2, headY - badge / 2 - 4 + fm.getAscent() / 2 - 2);

        // nombre encima
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        g.setColor(Color.WHITE);
        String nombre = l.getPersonaje().getNombre();
        FontMetrics fm2 = g.getFontMetrics();
        g.drawString(nombre, px + ancho / 2 - fm2.stringWidth(nombre) / 2, pyBase - alto - 8);
    }

    private void dibujarPopups(Graphics2D g) {
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        for (PopupDano p : popups) {
            g.setColor(new Color(p.especial ? 255 : 255, p.especial ? 210 : 90, p.especial ? 60 : 90, p.alpha()));
            g.drawString(p.texto, (int) p.x, (int) p.y());
        }
    }

    private void dibujarHUD(Graphics2D g) {
        dibujarBarraVida(g, jugador1, 30, 20, false);
        dibujarBarraVida(g, jugador2, GameConfig.ANCHO_ARENA - 330, 20, true);

        dibujarBarraEnergia(g, jugador1, 30, 48, false);
        dibujarBarraEnergia(g, jugador2, GameConfig.ANCHO_ARENA - 330, 48, true);

        dibujarEfectos(g, jugador1, 30, 74, false);
        dibujarEfectos(g, jugador2, GameConfig.ANCHO_ARENA - 330, 74, true);

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(new Color(255, 255, 255, 190));
        g.drawString("J1: A/D mover · F básico · G especial · R furia · T escudo", 20, GameConfig.ALTO_ARENA - 12);
        String p2txt = "J2: ←/→ mover · K básico · L especial · I furia · O escudo";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(p2txt, GameConfig.ANCHO_ARENA - fm.stringWidth(p2txt) - 20, GameConfig.ALTO_ARENA - 12);
    }

    private void dibujarBarraVida(Graphics2D g, Luchador l, int x, int y, boolean invertido) {
        int ancho = 300, alto = 20;
        Combatiente p = l.getPersonaje();
        double proporcion = Math.max(0, (double) p.getVidaActual() / p.getVidaMaxima());

        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(x - 3, y - 3, ancho + 6, alto + 6, 10, 10);
        g.setColor(new Color(60, 20, 20));
        g.fillRoundRect(x, y, ancho, alto, 8, 8);

        Color vidaColor = proporcion > 0.5 ? new Color(60, 200, 90) : (proporcion > 0.2 ? new Color(230, 190, 40) : new Color(220, 60, 50));
        g.setColor(vidaColor);
        int w = (int) (ancho * proporcion);
        if (invertido) {
            g.fillRoundRect(x + ancho - w, y, w, alto, 8, 8);
        } else {
            g.fillRoundRect(x, y, w, alto, 8, 8);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        String txt = l.getNombreJugador() + " · " + p.getNombre() + "  " + p.getVidaActual() + "/" + p.getVidaMaxima();
        if (invertido) {
            FontMetrics fm = g.getFontMetrics();
            g.drawString(txt, x + ancho - fm.stringWidth(txt), y - 6);
        } else {
            g.drawString(txt, x, y - 6);
        }
    }

    private void dibujarBarraEnergia(Graphics2D g, Luchador l, int x, int y, boolean invertido) {
        int ancho = 300, alto = 8;
        double proporcion = l.getEnergiaEspecial() / (double) GameConfig.ENERGIA_MAXIMA;

        g.setColor(new Color(0, 0, 0, 140));
        g.fillRoundRect(x, y, ancho, alto, 6, 6);
        g.setColor(new Color(90, 170, 240));
        int w = (int) (ancho * proporcion);
        if (invertido) {
            g.fillRoundRect(x + ancho - w, y, w, alto, 6, 6);
        } else {
            g.fillRoundRect(x, y, w, alto, 6, 6);
        }
    }

    /** Muestra bajo la barra de energía los potenciadores (decoradores) activos del luchador. */
    private void dibujarEfectos(Graphics2D g, Luchador l, int x, int y, boolean invertido) {
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        int cursor = invertido ? x + 300 : x;
        for (String efecto : l.getEfectos()) {
            g.setColor(efecto.startsWith("FURIA") ? new Color(255, 120, 60) : new Color(90, 190, 255));
            int w = fm.stringWidth(efecto);
            if (invertido) {
                cursor -= w;
                g.drawString(efecto, cursor, y);
                cursor -= 12;
            } else {
                g.drawString(efecto, cursor, y);
                cursor += w + 12;
            }
        }
    }

    private void dibujarCuentaRegresiva(Graphics2D g, long ahora) {
        long restante = 1500 - (ahora - inicioMs);
        String texto;
        if (restante > 1000) texto = "3";
        else if (restante > 500) texto = "2";
        else if (restante > 0) texto = "1";
        else texto = "¡PELEA!";

        g.setFont(new Font("SansSerif", Font.BOLD, 60));
        g.setColor(new Color(255, 220, 80));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(texto, GameConfig.ANCHO_ARENA / 2 - fm.stringWidth(texto) / 2, GameConfig.ALTO_ARENA / 2);
    }

    private void dibujarGanador(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, GameConfig.ANCHO_ARENA, GameConfig.ALTO_ARENA);
        g.setFont(new Font("SansSerif", Font.BOLD, 30));
        g.setColor(new Color(255, 220, 80));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(mensajeGanador, GameConfig.ANCHO_ARENA / 2 - fm.stringWidth(mensajeGanador) / 2, GameConfig.ALTO_ARENA / 2);
    }

    public void detener() {
        timer.stop();
    }

    /** Texto de daño flotante que sube y se desvanece. */
    private static class PopupDano {
        final double x;
        final double yInicial;
        final String texto;
        final boolean especial;
        private int frame = 0;
        private static final int DURACION_FRAMES = 40;

        PopupDano(double x, double yInicial, String texto, boolean especial) {
            this.x = x;
            this.yInicial = yInicial;
            this.texto = texto;
            this.especial = especial;
        }

        void avanzar() {
            frame++;
        }

        boolean terminado() {
            return frame >= DURACION_FRAMES;
        }

        double y() {
            return yInicial - frame * 1.2;
        }

        int alpha() {
            return Math.max(0, 255 - (int) (255.0 * frame / DURACION_FRAMES));
        }
    }
}
