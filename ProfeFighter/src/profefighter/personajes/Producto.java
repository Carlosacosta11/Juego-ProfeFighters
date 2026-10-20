package profefighter.personajes;

import profefighter.decorador.Combatiente;

/**
 * PATRÓN FACTORY METHOD (interfaz "Product")
 * Declara el comportamiento común a todo lo que una fábrica de personajes
 * puede producir. El factory method del Creator (PersonajeFactory) devuelve
 * este tipo, de modo que el Creator y sus clientes no dependen de ninguna
 * clase concreta de personaje.
 *
 * Extiende Combatiente para que quien reciba un Producto pueda consultar su
 * nombre, vida y ataques sin conocer la clase concreta. atacar() se vuelve a
 * declarar aquí porque es el "doStuff()" del patrón Factory Method.
 */
public interface Producto extends Combatiente {

    @Override
    void atacar(Combatiente objetivo, boolean especial);
}