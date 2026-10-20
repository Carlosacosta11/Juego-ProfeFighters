package profefighter.personajes;

/**
 * PATRÓN PROTOTYPE (interfaz "Prototype")
 * Declara el método de clonado que debe implementar cualquier objeto que
 * se pueda copiar a sí mismo. El parámetro de tipo T es el tipo del objeto
 * que devuelve la copia, así la interfaz no depende de ninguna clase concreta.
 * Personaje la implementa como Prototype<Personaje>; cada subclase concreta
 * (Marta, Edith, Dayner, JuanPablo) decide cómo se clona.
 */
public interface Prototype<T> {

    T clonar();
}