Proyecto 'cafeteria-samanez-estrada-delivery' v2 listo para abrir y ejecutar.

- Requisitos: JDK 17, IntelliJ o IDE con soporte Maven, JavaFX.
- Importar: Open -> seleccionar carpeta del proyecto -> Import as Maven project.
- Ejecutar: Run la clase pe.edu.cafeteria.CafeteriaApplication o usar: mvn clean javafx:run

Cambios principales respecto a la versión anterior:
 - Botón 'SOLICITAR DELIVERY' ahora siempre pide la dirección sin signos de interrogación.
 - Si hay un cliente seleccionado, la dirección ingresada se asigna a ese cliente y se actualiza la tabla.
 - Si no hay selección, debes ingresar Nombre y DNI primero; al ingresar la dirección se crea un nuevo cliente con esa dirección.
 - Después de ingresar la dirección se muestra una alerta de confirmación: 'Delivery confirmado'.
