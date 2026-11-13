import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

interface ObservadorCombate {
    void actualizar(String evento);
}

class RegistroConsola implements ObservadorCombate {
    public void actualizar(String evento) {
        System.out.println(evento);
    }
}

class ContadorAtaques implements ObservadorCombate {
    private int ataques = 0;

    public void actualizar(String evento) {
        if (evento.contains("ataca a")) {
            ataques++;
        }
    }

    public int getAtaques() {
        return ataques;
    }
}

class AlertaVidaBaja implements ObservadorCombate {
    public void actualizar(String evento) {
        if (evento.contains("0 puntos de vida")) {
            System.out.println("¡Un personaje ha sido derrotado!");
        }
    }
}

class RegistroCombate {
    private static RegistroCombate instancia;
    private final List<ObservadorCombate> observadores = new ArrayList<>();
    private final List<String> eventos = new ArrayList<>();

    private RegistroCombate() {}

    public static RegistroCombate getInstancia() {
        if (instancia == null) {
            instancia = new RegistroCombate();
        }
        return instancia;
    }

    public void agregarObservador(ObservadorCombate observador) {
        observadores.add(observador);
    }

    public void registrar(String evento) {
        eventos.add(evento);

        for (ObservadorCombate observador : observadores) {
            observador.actualizar(evento);
        }
    }

    public List<String> getEventos() {
        return new ArrayList<>(eventos);
    }
}

interface Atacable {
    String getNombre();
    int danoExtra();
}

class Espada implements Atacable {
    public String getNombre() {
        return "Espada de acero";
    }

    public int danoExtra() {
        return 5;
    }
}

class BaculoMagico implements Atacable {
    public String getNombre() {
        return "Báculo mágico";
    }

    public int danoExtra() {
        return 8;
    }
}

class ArmaLegacy {
    public String descripcion() {
        return "Hacha ancestral";
    }

    public int golpear() {
        return 6;
    }
}

class ArmaLegacyAdapter implements Atacable {
    private final ArmaLegacy armaLegacy;

    public ArmaLegacyAdapter(ArmaLegacy armaLegacy) {
        this.armaLegacy = armaLegacy;
    }

    public String getNombre() {
        return armaLegacy.descripcion();
    }

    public int danoExtra() {
        return armaLegacy.golpear();
    }
}

abstract class Personaje {
    protected String nombre;
    protected int puntosDeVida;
    protected int danoMinimo = 10;
    protected int danoMaximo = 30;
    protected Atacable arma;

    protected static final Random ALEATORIO = new Random();

    public Personaje(String nombre, int puntosDeVida) {
        this.nombre = nombre;
        this.puntosDeVida = puntosDeVida;
    }

    public void atacar(Personaje oponente) {
        int dano = ALEATORIO.nextInt(danoMaximo - danoMinimo + 1) + danoMinimo;

        if (arma != null) {
            dano += arma.danoExtra();
            RegistroCombate.getInstancia().registrar(
                nombre + " usa " + arma.getNombre() + "."
            );
        }

        oponente.recibirDano(dano);

        RegistroCombate.getInstancia().registrar(
            nombre + " ataca a " + oponente.getNombre() +
            " y causa " + dano + " puntos de daño."
        );
    }

    public void recibirDano(int dano) {
        puntosDeVida = Math.max(0, puntosDeVida - dano);
    }

    public boolean estaVivo() {
        return puntosDeVida > 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosDeVida() {
        return puntosDeVida;
    }

    public void setArma(Atacable arma) {
        this.arma = arma;
    }

    public abstract String getTipo();
}

class Guerrero extends Personaje {
    public Guerrero(String nombre) {
        super(nombre, 120);
        this.danoMinimo = 12;
        this.danoMaximo = 30;
    }

    public String getTipo() {
        return "Guerrero";
    }
}

class Mago extends Personaje {
    public Mago(String nombre) {
        super(nombre, 90);
        this.danoMinimo = 15;
        this.danoMaximo = 35;
    }

    public String getTipo() {
        return "Mago";
    }
}

class Arquero extends Personaje {
    public Arquero(String nombre) {
        super(nombre, 100);
        this.danoMinimo = 10;
        this.danoMaximo = 32;
    }

    public String getTipo() {
        return "Arquero";
    }
}

abstract class PersonajeFactory {
    public abstract Personaje crearPersonaje(String nombre);

    public Personaje crearConNombre(String nombre) {
        Personaje personaje = crearPersonaje(nombre);
        RegistroCombate.getInstancia().registrar(
            "Se creó un " + personaje.getTipo() + " llamado " + nombre + "."
        );
        return personaje;
    }
}

class GuerreroFactory extends PersonajeFactory {
    public Personaje crearPersonaje(String nombre) {
        return new Guerrero(nombre);
    }
}

class MagoFactory extends PersonajeFactory {
    public Personaje crearPersonaje(String nombre) {
        return new Mago(nombre);
    }
}

class ArqueroFactory extends PersonajeFactory {
    public Personaje crearPersonaje(String nombre) {
        return new Arquero(nombre);
    }
}

class PersonajeBuilder {
    private String nombre;
    private String tipo = "Guerrero";
    private Atacable arma;

    public PersonajeBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public PersonajeBuilder conTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public PersonajeBuilder conArma(Atacable arma) {
        this.arma = arma;
        return this;
    }

    public Personaje construir() {
        Personaje personaje;

        switch (tipo.toLowerCase()) {
            case "mago":
                personaje = new Mago(nombre);
                break;
            case "arquero":
                personaje = new Arquero(nombre);
                break;
            default:
                personaje = new Guerrero(nombre);
        }

        if (arma != null) {
            personaje.setArma(arma);
        }

        return personaje;
    }
}

abstract class PersonajeDecorator extends Personaje {
    protected final Personaje personajeDecorado;

    public PersonajeDecorator(Personaje personaje) {
        super(personaje.getNombre(), personaje.getPuntosDeVida());
        this.personajeDecorado = personaje;
    }

    public String getTipo() {
        return personajeDecorado.getTipo();
    }
}

class EscudoDecorator extends PersonajeDecorator {
    private final int reduccionDano;

    public EscudoDecorator(Personaje personaje, int reduccionDano) {
        super(personaje);
        this.reduccionDano = reduccionDano;
    }

    public void recibirDano(int dano) {
        int danoReducido = Math.max(1, dano - reduccionDano);
        RegistroCombate.getInstancia().registrar(
            nombre + " reduce el daño con su escudo: " +
            dano + " -> " + danoReducido + "."
        );
        personajeDecorado.recibirDano(danoReducido);
    }

    public int getPuntosDeVida() {
        return personajeDecorado.getPuntosDeVida();
    }

    public boolean estaVivo() {
        return personajeDecorado.estaVivo();
    }
}

class ControladorCombate {
    public void ejecutarTurno(Personaje atacante, Personaje defensor) {
        RegistroCombate.getInstancia().registrar(
            "Turno de " + atacante.getNombre() +
            ". HP de " + defensor.getNombre() + ": " +
            defensor.getPuntosDeVida()
        );

        atacante.atacar(defensor);

        RegistroCombate.getInstancia().registrar(
            defensor.getNombre() + " ahora tiene " +
            defensor.getPuntosDeVida() + " puntos de vida."
        );
    }
}

class JuegoLuchaFacade {
    private final Personaje jugador1;
    private final Personaje jugador2;
    private final ControladorCombate controlador;

    public JuegoLuchaFacade(Personaje jugador1, Personaje jugador2) {
        this.jugador1 = new EscudoDecorator(jugador1, 3);
        this.jugador2 = new EscudoDecorator(jugador2, 3);
        this.controlador = new ControladorCombate();
    }

    public void iniciarPelea() {
        RegistroCombate.getInstancia().registrar(
            "La pelea comienza entre " + jugador1.getNombre() +
            " y " + jugador2.getNombre() + "."
        );

        while (jugador1.estaVivo() && jugador2.estaVivo()) {
            controlador.ejecutarTurno(jugador1, jugador2);

            if (!jugador2.estaVivo()) {
                break;
            }

            controlador.ejecutarTurno(jugador2, jugador1);
        }

        String ganador = jugador1.estaVivo()
                ? jugador1.getNombre()
                : jugador2.getNombre();

        RegistroCombate.getInstancia().registrar(
            ganador + " ha ganado la pelea."
        );
    }
}

public class JuegoLuchaApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RegistroCombate registro = RegistroCombate.getInstancia();
        registro.agregarObservador(new RegistroConsola());
        registro.agregarObservador(new ContadorAtaques());
        registro.agregarObservador(new AlertaVidaBaja());

        System.out.print("Nombre del jugador 1: ");
        String nombre1 = scanner.nextLine();

        System.out.print("Nombre del jugador 2: ");
        String nombre2 = scanner.nextLine();

        PersonajeFactory factory1 = new GuerreroFactory();
        PersonajeFactory factory2 = new MagoFactory();

        Personaje p1 = factory1.crearConNombre(nombre1);
        Personaje p2 = factory2.crearConNombre(nombre2);

        ArmaLegacy hachaAntigua = new ArmaLegacy();
        Atacable armaAdaptada = new ArmaLegacyAdapter(hachaAntigua);

        Personaje p3 = new PersonajeBuilder()
                .conNombre("Legolas")
                .conTipo("Arquero")
                .conArma(armaAdaptada)
                .construir();

        JuegoLuchaFacade juego = new JuegoLuchaFacade(p1, p2);
        juego.iniciarPelea();

        System.out.println("\nEventos registrados: " +
                registro.getEventos().size());
    }
}