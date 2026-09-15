package profefighter;

import java.awt.Color;

/**
 * Representa a un personaje dentro de la arena: su posición, si mira a la
 * derecha o a la izquierda, su energía especial y el estado de animación de
 * ataque/golpe. Envuelve un Personaje (dominio) sin modificarlo.
 */
public class Luchador {

    private final Personaje personaje;
    private final Color colorEtiqueta;
    private final String inicial;
    private final String nombreJugador;

    private double x;
    private boolean mirandoDerecha;

    private int energiaEspecial = 0;
    private long ultimoAtaqueBasicoMs = 0;
    private long finAnimacionAtaqueMs = 0;
    private boolean ultimoAtaqueFueEspecial = false;
    private long finFlashGolpeMs = 0;

    public Luchador(Personaje personaje, double xInicial, boolean mirandoDerecha,
                     Color colorEtiqueta, String inicial, String nombreJugador) {
        this.personaje = personaje;
        this.x = xInicial;
        this.mirandoDerecha = mirandoDerecha;
        this.colorEtiqueta = colorEtiqueta;
        this.inicial = inicial;
        this.nombreJugador = nombreJugador;
    }

    public void mover(int delta, double limiteIzq, double limiteDer) {
        x += delta;
        if (x < limiteIzq) x = limiteIzq;
        if (x > limiteDer) x = limiteDer;
    }

    public boolean intentarAtaqueBasico(Luchador objetivo, long ahoraMs) {
        if (ahoraMs - ultimoAtaqueBasicoMs < GameConfig.COOLDOWN_ATAQUE_BASICO_MS) {
            return false;
        }
        ultimoAtaqueBasicoMs = ahoraMs;
        ultimoAtaqueFueEspecial = false;
        finAnimacionAtaqueMs = ahoraMs + GameConfig.DURACION_ANIMACION_ATAQUE_MS;
        if (enRango(objetivo)) {
            personaje.atacar(objetivo.personaje, false);
            objetivo.recibirFlashGolpe(ahoraMs);
            ganarEnergia(GameConfig.ENERGIA_POR_GOLPE_BASICO);
            return true;
        }
        return false;
    }

    public boolean intentarAtaqueEspecial(Luchador objetivo, long ahoraMs) {
        if (energiaEspecial < GameConfig.ENERGIA_MAXIMA) {
            return false;
        }
        energiaEspecial = 0;
        ultimoAtaqueFueEspecial = true;
        finAnimacionAtaqueMs = ahoraMs + GameConfig.DURACION_ANIMACION_ATAQUE_MS + 120;
        if (enRango(objetivo)) {
            personaje.atacar(objetivo.personaje, true);
            objetivo.recibirFlashGolpe(ahoraMs);
            return true;
        }
        return false;
    }

    private void ganarEnergia(int cantidad) {
        energiaEspecial = Math.min(GameConfig.ENERGIA_MAXIMA, energiaEspecial + cantidad);
    }

    private void recibirFlashGolpe(long ahoraMs) {
        finFlashGolpeMs = ahoraMs + GameConfig.DURACION_FLASH_GOLPE_MS;
    }

    private boolean enRango(Luchador objetivo) {
        return Math.abs(this.x - objetivo.x) <= GameConfig.RANGO_ATAQUE;
    }

    public void actualizarOrientacion(Luchador oponente) {
        mirandoDerecha = oponente.x >= this.x;
    }

    public boolean estaAtacando(long ahoraMs) {
        return ahoraMs < finAnimacionAtaqueMs;
    }

    public boolean ultimoAtaqueEspecial() {
        return ultimoAtaqueFueEspecial;
    }

    public boolean estaEnFlashGolpe(long ahoraMs) {
        return ahoraMs < finFlashGolpeMs;
    }

    public Personaje getPersonaje() {
        return personaje;
    }

    public double getX() {
        return x;
    }

    public boolean isMirandoDerecha() {
        return mirandoDerecha;
    }

    public int getEnergiaEspecial() {
        return energiaEspecial;
    }

    public Color getColorEtiqueta() {
        return colorEtiqueta;
    }

    public String getInicial() {
        return inicial;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }
}
