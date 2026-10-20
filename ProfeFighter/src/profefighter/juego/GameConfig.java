package profefighter.juego;

import java.awt.event.KeyEvent;

/**
 * Constantes de configuración del juego (dimensiones de la arena, velocidad,
 * rango de ataque, teclas de control, etc.). Centralizarlas aquí evita
 * "números mágicos" repartidos por las clases de la interfaz gráfica.
 */
public final class GameConfig {

    private GameConfig() {
    }

    // Arena
    public static final int ANCHO_ARENA = 900;
    public static final int ALTO_ARENA = 520;
    public static final int SUELO_Y = 420;

    // Personaje (tamaño aproximado para dibujo y colisiones)
    public static final int ANCHO_PERSONAJE = 56;
    public static final int ALTO_PERSONAJE = 96;

    // Movimiento y combate
    public static final int VELOCIDAD_MOVIMIENTO = 6;
    public static final int LIMITE_IZQUIERDO = 40;
    public static final int LIMITE_DERECHO = ANCHO_ARENA - 40 - ANCHO_PERSONAJE;
    public static final int DISTANCIA_MINIMA = 30;
    public static final int RANGO_ATAQUE = 95;

    public static final long COOLDOWN_ATAQUE_BASICO_MS = 450;
    public static final long DURACION_ANIMACION_ATAQUE_MS = 220;
    public static final long DURACION_FLASH_GOLPE_MS = 180;

    public static final int ENERGIA_MAXIMA = 100;
    public static final int ENERGIA_POR_GOLPE_BASICO = 18;

    // Controles Jugador 1
    public static final int P1_IZQUIERDA = KeyEvent.VK_A;
    public static final int P1_DERECHA = KeyEvent.VK_D;
    public static final int P1_ATAQUE_BASICO = KeyEvent.VK_F;
    public static final int P1_ATAQUE_ESPECIAL = KeyEvent.VK_G;

    // Controles Jugador 2
    public static final int P2_IZQUIERDA = KeyEvent.VK_LEFT;
    public static final int P2_DERECHA = KeyEvent.VK_RIGHT;
    public static final int P2_ATAQUE_BASICO = KeyEvent.VK_K;
    public static final int P2_ATAQUE_ESPECIAL = KeyEvent.VK_L;

    // Potenciadores (patrón Decorator): una activación de cada uno por combate
    public static final int P1_FURIA = KeyEvent.VK_R;
    public static final int P1_ESCUDO = KeyEvent.VK_T;
    public static final int P2_FURIA = KeyEvent.VK_I;
    public static final int P2_ESCUDO = KeyEvent.VK_O;

    public static final int USOS_POTENCIADOR = 3;
    public static final int FURIA_PORCENTAJE_EXTRA = 50;
    public static final int ESCUDO_PORCENTAJE_REDUCCION = 40;

    public static final int FPS = 60;
}
