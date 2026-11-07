# HotelReservas — Sistema de reservas de un hotel (Java 21 / NetBeans 22)

Aplicación de escritorio (Java Swing, sin dependencias externas) del caso de estudio de la
investigación *Comparación entre programación estructurada y diseño orientado a objetos*.

## Abrir y ejecutar en NetBeans 22
1. Archivo > Abrir proyecto... > seleccionar la carpeta `HotelReservas` (proyecto Maven).
2. Verificar que el JDK sea 21 (Propiedades del proyecto > Build > Compile).
3. Ejecutar (F6). Clase principal: `com.ues.hotel.Main`.
4. Pruebas: clic derecho en `src/test/java/.../PruebasHotel.java` > Run File.
5. Alternativa sin NetBeans: `java -jar dist/HotelReservas.jar`.

## Estructura
- `modelo`: Persona, Cliente, Habitacion (+ Sencilla, Doble, Suite), Reserva, enums.
- `servicio`: Hotel (reglas del negocio) y HotelException.
- `ui`: ventana principal y paneles Swing.
- `estructurado`: la misma funcionalidad con enfoque estructurado (arreglos + procedimientos).
- `persistencia`: RepositorioArchivo (guarda/carga en `datos/hotel.txt`); interfaz RepositorioHotel en `servicio`.
- `comparacion`: escenario equivalente con objetos para la pestaña de comparación.

## Funcionalidades (caso de estudio)
Registrar clientes y habitaciones, consultar disponibilidad, reservar, cancelar,
consultar reservas y calcular el costo (tarifa x noches + IVA 13 %).
## Persistencia de datos
Cada cambio (registrar, reservar, cancelar) se guarda automáticamente en `datos/hotel.txt`, un archivo de texto
legible que se crea junto a donde se ejecuta la aplicación (en NetBeans, la carpeta del proyecto).
Al abrir, se cargan los datos guardados; en la primera ejecución se crean datos de ejemplo.
Si el archivo está dañado, se conserva como `hotel.txt.danado` y se inicia con datos de ejemplo.
Para empezar de cero basta con borrar la carpeta `datos`.
