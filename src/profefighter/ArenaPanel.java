package profefighter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Arena de combate en tiempo real: dibuja el escenario (mundo destruido),
 * mueve a los dos luchadores según las teclas presionadas, resuelve los
 * ataques básicos/especiales y muestra barras de vida y energía.
 *
 * NOTA (viola SRP a propósito, ver análisis SOLID actualizado en la
 * documentación): esta clase mezcla tres responsabilidades: dibujar la
 * escena, leer el teclado y aplicar las reglas de combate. Se deja así para
 * poder señalarlo como ejemplo real de incumplimiento de SRP en la
 * sustentación (se corregiría separando Renderer, InputHandler y
 * CombatEngine).
 */
public class ArenaPanel extends JPanel {

    private final Luchador jugador1;
    private final Luchador jugador2;
    private final Consumer<Luchador> onCombateTerminado;
    private final Set<Integer> teclasPresionadas = new HashSet<>();
    private final Timer timer;
    private final List<PopupDano> popups = new ArrayList<>();
    private final List<Rectangle> escombros = new ArrayList<>();
    private final List<Polygon> edificios = new ArrayList<>();

    private long inicioMs;
    private boolean combateResuelto = false;
    private String mensajeGanador = null;

    public ArenaPanel(Luchador jugador1, Luchador jugador2, Consumer<Luchador> onCombateTerminado) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.onCombateTerminado = onCombateTerminado;

        setPreferredSize(new Dimension(GameConfig.ANCHO_ARENA, GameConfig.ALTO_ARENA));
        setFocusable(true);
        generarEscenario();

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

    private void generarEscenario() {
        Random rnd = new Random(42);
        for (int i = 0; i < 6; i++) {
            int ancho = 60 + rnd.nextInt(90);
            int alto = 90 + rnd.nextInt(160);
            int x = i * (GameConfig.ANCHO_ARENA / 6) + rnd.nextInt(30);
            Polygon edificio = new Polygon();
            int baseY = GameConfig.SUELO_Y - 10;
            edificio.addPoint(x, baseY);
            edificio.addPoint(x, baseY - alto + rnd.nextInt(30));
            edificio.addPoint(x + ancho / 3, baseY - alto);
            edificio.addPoint(x + (2 * ancho) / 3, baseY - alto + rnd.nextInt(25));
            edificio.addPoint(x + ancho, baseY - alto + rnd.nextInt(20));
            edificio.addPoint(x + ancho, baseY);
            edificios.add(edificio);
        }
        for (int i = 0; i < 14; i++) {
            int w = 14 + rnd.nextInt(26);
            int h = 8 + rnd.nextInt(14);
            int x = rnd.nextInt(GameConfig.ANCHO_ARENA - w);
            int y = GameConfig.SUELO_Y + rnd.nextInt(GameConfig.ALTO_ARENA - GameConfig.SUELO_Y - h - 5);
            escombros.add(new Rectangle(x, y, w, h));
        }
    }

    private void manejarAtaques(int codigo) {
        if (combateResuelto) return;
        long ahora = System.currentTimeMillis();
        if (codigo == GameConfig.P1_ATAQUE_BASICO) {
            if (jugador1.intentarAtaqueBasico(jugador2, ahora)) {
                agregarPopup(jugador2, jugador1.getPersonaje().getDanoBasico(), false);
            }
        } else if (codigo == GameConfig.P1_ATAQUE_ESPECIAL) {
            if (jugador1.intentarAtaqueEspecial(jugador2, ahora)) {
                agregarPopup(jugador2, jugador1.getPersonaje().getDanoEspecial(), true);
            }
        } else if (codigo == GameConfig.P2_ATAQUE_BASICO) {
            if (jugador2.intentarAtaqueBasico(jugador1, ahora)) {
                agregarPopup(jugador1, jugador2.getPersonaje().getDanoBasico(), false);
            }
        } else if (codigo == GameConfig.P2_ATAQUE_ESPECIAL) {
            if (jugador2.intentarAtaqueEspecial(jugador1, ahora)) {
                agregarPopup(jugador1, jugador2.getPersonaje().getDanoEspecial(), true);
            }
        }
        comprobarFinDeCombate();
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

        dibujarFondo(g);
        dibujarSuelo(g);
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

    private void dibujarFondo(Graphics2D g) {
        GradientPaint cielo = new GradientPaint(0, 0, new Color(70, 30, 30), 0, GameConfig.SUELO_Y, new Color(35, 25, 35));
        g.setPaint(cielo);
        g.fillRect(0, 0, GameConfig.ANCHO_ARENA, GameConfig.SUELO_Y);

        g.setPaint(new Color(200, 90, 50, 160));
        g.fillOval(GameConfig.ANCHO_ARENA - 160, 40, 90, 90);
        g.setPaint(new Color(200, 90, 50, 70));
        g.fillOval(GameConfig.ANCHO_ARENA - 190, 10, 150, 150);

        g.setColor(new Color(20, 15, 20, 200));
        for (Polygon edificio : edificios) {
            g.fillPolygon(edificio);
        }
    }

    private void dibujarSuelo(Graphics2D g) {
        g.setColor(new Color(46, 40, 42));
        g.fillRect(0, GameConfig.SUELO_Y, GameConfig.ANCHO_ARENA, GameConfig.ALTO_ARENA - GameConfig.SUELO_Y);

        g.setColor(new Color(30, 26, 28));
        for (Rectangle r : escombros) {
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        g.setColor(new Color(20, 16, 18));
        g.setStroke(new BasicStroke(2f));
        Path2D grieta = new Path2D.Double();
        grieta.moveTo(120, GameConfig.SUELO_Y + 6);
        grieta.lineTo(200, GameConfig.SUELO_Y + 30);
        grieta.lineTo(260, GameConfig.SUELO_Y + 14);
        grieta.lineTo(340, GameConfig.SUELO_Y + 40);
        g.draw(grieta);
        Path2D grieta2 = new Path2D.Double();
        grieta2.moveTo(560, GameConfig.SUELO_Y + 10);
        grieta2.lineTo(620, GameConfig.SUELO_Y + 34);
        grieta2.lineTo(700, GameConfig.SUELO_Y + 18);
        g.draw(grieta2);
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

        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(new Color(255, 255, 255, 190));
        g.drawString("J1: A/D mover · F básico · G especial", 20, GameConfig.ALTO_ARENA - 12);
        String p2txt = "J2: ←/→ mover · K básico · L especial";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(p2txt, GameConfig.ANCHO_ARENA - fm.stringWidth(p2txt) - 20, GameConfig.ALTO_ARENA - 12);
    }

    private void dibujarBarraVida(Graphics2D g, Luchador l, int x, int y, boolean invertido) {
        int ancho = 300, alto = 20;
        Personaje p = l.getPersonaje();
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
