# Juego de Lucha en Java

Proyecto académico que implementa un juego de lucha por turnos en Java, aplicando patrones de diseño creacionales, estructurales y de comportamiento.

## Descripción

El programa permite ingresar los nombres de dos personajes. Después, inicia una pelea automática por turnos. Cada personaje realiza ataques que causan daño aleatorio al oponente. El primer personaje que llega a 0 puntos de vida pierde.

## Patrones implementados

### Patrones creacionales

- **Factory Method:** `PersonajeFactory`, `GuerreroFactory`, `MagoFactory` y `ArqueroFactory` crean los diferentes tipos de personajes.
- **Builder:** `PersonajeBuilder` construye un personaje paso a paso con nombre, tipo y arma.
- **Singleton:** `RegistroCombate` garantiza una única instancia del registro de combate.

### Patrones estructurales

- **Adapter:** `ArmaLegacyAdapter` adapta la clase antigua `ArmaLegacy` a la interfaz moderna `Atacable`.
- **Decorator:** `EscudoDecorator` añade reducción de daño a un personaje sin modificar su clase original.
- **Facade:** `JuegoLuchaFacade` simplifica el acceso al sistema y controla la pelea.

### Patrón de comportamiento

- **Observer:** `RegistroCombate` notifica a los observadores cuando ocurre un evento del combate. Los observadores son `RegistroConsola`, `ContadorAtaques` y `AlertaVidaBaja`.

## Cómo ejecutar

Compilar:

```bash
javac JuegoLuchaApp.java
```

Ejecutar:

```bash
java JuegoLuchaApp
```

## Autora

Lina Maria
