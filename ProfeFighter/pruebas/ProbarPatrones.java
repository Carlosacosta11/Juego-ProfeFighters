import profefighter.decorador.*;
import profefighter.composite.*;
import profefighter.fachada.*;
import profefighter.juego.*;
import profefighter.personajes.*;
import java.util.*;

public class ProbarPatrones {
    static int fallos = 0;
    static void check(String nombre, boolean ok) { System.out.println((ok ? "OK    " : "FALLO ") + nombre); if (!ok) fallos++; }

    static int contar(ElementoGrafico e) {
        if (!(e instanceof GrupoGrafico)) return 1;
        int n = 0; for (ElementoGrafico h : ((GrupoGrafico) e).getChildren()) n += contar(h); return n;
    }

    public static void main(String[] args) {
        // ---------- FACADE ----------
        PartidaFacade f = new PartidaFacade();
        Partida p = f.iniciarPartida("1", "1");   // mismo profesor dos veces
        Luchador j1 = p.getJugador1(), j2 = p.getJugador2();
        check("Facade: devuelve dos luchadores", j1 != null && j2 != null);
        check("Facade: mismo profesor -> instancias independientes", j1.getPersonaje() != j2.getPersonaje());
        j1.getPersonaje().recibirDano(30);
        check("Facade: dañar a uno no afecta al otro", j2.getPersonaje().getVidaActual() == 100 && j1.getPersonaje().getVidaActual() == 70);
        check("Facade: j1 a la izquierda mirando a la derecha", j1.getX() == 160 && j1.isMirandoDerecha());
        check("Facade: j2 a la derecha mirando a la izquierda", j2.getX() == GameConfig.ANCHO_ARENA - 220 && !j2.isMirandoDerecha());
        boolean lanzo = false; try { f.iniciarPartida("9", "1"); } catch (IllegalArgumentException e) { lanzo = true; }
        check("Facade: id desconocido -> IllegalArgumentException", lanzo);
        List<TarjetaProfesor> tarjetas = f.getCatalogo().listarProfesores();
        check("Facade adicional: 4 tarjetas", tarjetas.size() == 4);
        check("Facade adicional: datos de tarjeta coherentes",
              tarjetas.get(0).getNombre().equals("Marta Compiler") && tarjetas.get(0).getMateria().equals("Construcción de Software")
              && tarjetas.get(0).getResumen().contains("Vida: 100") && tarjetas.get(3).getInicial().equals("JP"));

        // ---------- DECORATOR ----------
        BattleController bc = new BattleController();
        Combatiente marta = bc.seleccionarPersonaje("1");   // básico 10, especial 25
        Combatiente edith = bc.seleccionarPersonaje("2");   // vida 90
        marta.atacar(edith, false);
        check("Decorator: sin decorar, básico = 10", edith.getVidaActual() == 80);
        check("Decorator: sin decorar, sin efectos", marta.getEfectos().isEmpty());

        Combatiente m = bc.seleccionarPersonaje("1"), e = bc.seleccionarPersonaje("2");
        Combatiente conFuria = new FuriaDecorator(m, 3, 50);
        conFuria.atacar(e, false);             // 10 + 5
        check("Furia: básico = 10 + 50% = 15", e.getVidaActual() == 75);
        conFuria.atacar(e, true);              // 25 + 12
        check("Furia: especial = 25 + 12 = 37", e.getVidaActual() == 75 - 37);
        check("Furia: queda 1 uso", conFuria.getEfectos().equals(Arrays.asList("FURIA x1")));
        conFuria.atacar(e, false);             // último uso
        check("Furia: tercer uso aplica extra", e.getVidaActual() == 38 - 15);
        int antes = e.getVidaActual();
        conFuria.atacar(e, false);             // sin usos -> normal
        check("Furia: agotada, vuelve a 10", e.getVidaActual() == antes - 10);
        check("Furia: agotada, sin efectos", conFuria.getEfectos().isEmpty());
        check("Furia: no altera al objeto envuelto (nombre/stats delegados)",
              conFuria.getNombre().equals("Marta Compiler") && conFuria.getDanoBasico() == 10);

        Combatiente v = bc.seleccionarPersonaje("2");
        Combatiente conEscudo = new EscudoDecorator(v, 3, 40);
        bc.seleccionarPersonaje("1").atacar(conEscudo, false);   // 10 -> 6
        check("Escudo: 10 -> 6 (-40%)", conEscudo.getVidaActual() == 84);
        bc.seleccionarPersonaje("1").atacar(conEscudo, true);    // 25 -> 15
        check("Escudo: 25 -> 15", conEscudo.getVidaActual() == 69);
        bc.seleccionarPersonaje("1").atacar(conEscudo, false);   // 10 -> 6
        check("Escudo: tercer golpe reducido", conEscudo.getVidaActual() == 63);
        check("Escudo: agotado, sin efectos", conEscudo.getEfectos().isEmpty());
        bc.seleccionarPersonaje("1").atacar(conEscudo, false);   // 10 completo
        check("Escudo: agotado, daño completo", conEscudo.getVidaActual() == 53);

        // capas apiladas: Furia (atacante) vs Escudo (objetivo), con capa doble en el atacante
        Combatiente atac = new EscudoDecorator(new FuriaDecorator(bc.seleccionarPersonaje("1"), 3, 50), 3, 40);
        Combatiente obj = new EscudoDecorator(bc.seleccionarPersonaje("3"), 3, 40);  // Dayner vida 110
        atac.atacar(obj, true);   // especial 25 -> 15 ; extra 12 -> 7
        check("Capas: Furia atacante + Escudo objetivo = 15 + 7 = 22", obj.getVidaActual() == 110 - 22);
        check("Capas: efectos apilados en orden", atac.getEfectos().equals(Arrays.asList("FURIA x2", "ESCUDO x3")));

        // Luchador: activar potenciadores solo una vez, y se aplican en combate real
        Partida q = f.iniciarPartida("1", "2");
        Luchador a = q.getJugador1(), b = q.getJugador2();
        check("Luchador: activarFuria 1ª vez true", a.activarFuria());
        check("Luchador: activarFuria 2ª vez false (una por combate)", !a.activarFuria());
        check("Luchador: activarEscudo en b", b.activarEscudo());
        check("Luchador: efectos visibles", a.getEfectos().equals(Arrays.asList("FURIA x3")) && b.getEfectos().equals(Arrays.asList("ESCUDO x3")));
        // acercar a b a rango y atacar: 10 + 5 = 15, reducido por escudo: 10*0.6=6 + 5*0.6=3 = 9
        b.mover(-(int) (b.getX() - a.getX() - 50), 0, 900);
        int v0 = b.getPersonaje().getVidaActual();
        boolean golpe = a.intentarAtaqueBasico(b, System.currentTimeMillis());
        check("Luchador: ataque conecta en rango", golpe);
        check("Luchador: Furia(+5) y Escudo(-40%) combinados = 9", v0 - b.getPersonaje().getVidaActual() == 9);

        // ---------- COMPOSITE ----------
        GrupoGrafico g = new GrupoGrafico("g"); Edificio ed = new Edificio(new java.awt.Polygon());
        g.add(ed); g.add(new GrupoGrafico("h"));
        check("Composite: add", g.getChildren().size() == 2);
        g.remove(ed);
        check("Composite: remove", g.getChildren().size() == 1);
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(10, 10, 2);
        g.dibujar(img.createGraphics());
        check("Composite: dibujar() sobre grupo vacío anidado no falla", true);

        System.out.println(fallos == 0 ? "\nTODAS LAS PRUEBAS PASARON" : "\nFALLOS: " + fallos);
        System.exit(fallos == 0 ? 0 : 1);
    }
}
