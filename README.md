# Mapeo de entidades

## Mapeo de una entidad simple: Service

### Ejercicio 12: ​¿Cuál es el conjunto mínimo de anotaciones que debe tener una clase para ser persistente con JPA?

El conjunto mínimo de anotaciones que debe tener una clase para ser persistente con JPA se compone de dos anotaciones:
- `@Entity`: Se coloca a nivel de la declaración de la clase par aindicarle al framework que dicha clase es uina entidad (POJO persistente) y que debe ser mapeada a la base de datos
- `@Id`: Se coloca sobre el atributo que servirá como identificador único o clave primaria, cumpliendo con la regla obligatoria de los POJOs persistentes de proveer un identificador

>[!NOTE]
> No es obligatorio agregar anotaciones adicionales para los demás atributos de la clase. Gracias al principio de "Convención sobre Configuración", JPA asume por defecto que todos los atributos declarados en la clase deben persistirse en la base de datos, a menos que se indique explícitamente lo contrario (por ejemplo, utilizando la anotación `@Transient` para que un campo sea ignorado)

### Ejercicio 13: ¿Qué significa que JPA use persistencia por alcance (persistence by reachability)? ¿Qué consecuencia tiene si un objeto referenciado no está todavía persistido?

La persistencia por alcance (o persistencia transitiva) significa que todo objeto al cual se pueda llegar navegando a partir de un objeto que ya es persistente, debe ser necesariamente persistente a su vez. Esto permite que, para almacenar un objeto en la base de datos, el desarrollador solamente necesite vincularlo orgánicamente (por ejemplo, agregándolo a una colección o asignándolo a un atributo) con algún otro objeto que ya exista en el repositorio, reduciendo de esta forma las operaciones explícitas de guardado y respetando el principio de independencia
- La consecuencia de que un objeto referenciado no esté todavía persistido (es decir,  que se encuentre en estado volátil o transitorio) depende de la configuración de cascada:
  - Si la persistencia por alcance está activa (por ejemplo, configurada con `CascadeType.PERSIST` o `ALL`): Al momento de realizar el commit de la transacción, el framework recorrerá el grafo de objetos modificados y, al encontrar este nuevo objeto volátil referenciado, lo insertará y persistirá automáticamente en la base de datos sin requerir ninguna acción adicional por parte del desarrollador
  - Si la persistencia por alcance NO está activa (el comportamiento por defecto de JPA): La operación de guardado no se propagará hacia la entidad asociada. Como consecuencia, el ORM intentará guardar en la base de datos una relación hacia un registro que físicamente aún no existe, lo cual desencadenará una excepción o falla de integridad referencial.

### Ejercicio 14: ¿Qué diferencia hay entre las estrategias IDENTITY, SEQUENCE y TABLE para la generación de IDs? ¿Cuál tiene mejor rendimiento en inserciones masivas y por qué?

En JPA la anotación `@GeneratedValue` permite definir cómo se crerán las claves primarias automáticamente, utilizando tres estrategias principales:

- `IDENTITY`:
  - **Delega la generación del identificador enteramente a la base de datos** utilizando columnas de tipo "**autoincremental**" (como `AUTO_INCREMENT` en MySQL o `IDENTITY` en PostgreSQL).
  - En Hibernate no conoce el ID del objeto hasta que la sentencia `INSERT` se ejecuta realmente en la base de datos.
- `SEQUENCE`:
  - Utiliza un objeto nativo de la base de datos llamado "Secuencia" (muy común en Oracle o PostgreSQL).
  - En Hibernate realiza una consulta a la base de datos para pedirle el próximo valor de la secuencia antes de ejecutar el `INSERT`. Esto le permite a Hibernate conocer el ID del objeto y asignárselo en memoria antes de guardar el registro.
- `TABLE`:
  - Es la estrategia más genérica. En lugar de usar características nativas del motor (como columnas autoincrementales o secuencias), crea una tabla auxiliar en la base de datos dedicada exclusivamente a llevar la cuenta de los identificadores.
  - En Hibernate para generar un nuevo ID, Hibernate debe hacer un `SELECT` en esta tabla auxiliar, bloquear la fila (lock), hacer un `UPDATE` para incrementar el valor, y liberar la fila.

### Ejercicio 15: Implementar el mapeo completo de la entidad Service según el diagrama. La implementación debe incluir:

#### a) Clave primaria con estrategia de generación automática. Elegir entre IDENTITY, SEQUENCE o TABLE y justificar la elección.

- Agergamos la anotación `@Entity` a la clase para marcarla como una entidad persistente
- Luego, definimos el atributo `id` como clave primaria utilizando la anotación `@Id` y configuramos la estrategia de generación automática con `@GeneratedValue`. En este caso, utilizaremos `IDENTITY`
  - La elección de `IDENTITY` se justifica porque, como estamos usando MySQL, esta estrategia es compatible con las columnas autoincrementales, lo que simplifica la generación de IDs sin necesidad de realizar consultas adicionales a la base de datos para obtener el próximo valor, como ocurre con `SEQUENCE` o `TABLE`. Además, `IDENTITY` suele ofrecer un mejor rendimiento en inserciones masivas debido a su simplicidad y menor sobrecarga.

#### b) Atributos: name (no nulo, max. 100 caracteres), description (opcional), price (no nulo).

- Ahora sobre el atributo `name` agregamos las anotaciones `@Column(nullable = false, length = 100)` para indicar que no puede ser nulo y que su longitud máxima es de 100 caracteres
- Sobre el atributo `description` agregamos la anotación `@Column(nullable = true)` para indicar que es opcional (aunque esta anotación es redundante, ya que por defecto los campos son nullable)
- Sobre el atributo `price` agregamos la anotación `@Column(nullable = false)`

#### c) Al menos una restricción de unicidad a nivel de columna.

Para agregar una restricción de unicidad a nivel de columna, podemos utilizar la anotación `@Column(unique = true)` sobre el atributo `name`, lo que garantizará que no haya dos servicios con el mismo nombre en la base de datos.

## Relaciones entre entidades

### Ejercicio 16: Para la relación Purchase -> ItemService (composición uno-a-muchos):

#### a)​ ¿Qué anotaciones se necesitan en cada lado?

- En el lado de `Purchase` (lado "uno"), se necesita la anotación `@OneToMany` sobre el atributo que representa la colección de `ItemService`. Además, se debe especificar el atributo `mappedBy` para indicar que esta es la parte inversa de la relación y que el propietario es `ItemService`. Además, agergamos `cascade = CascadeType.REMOVE` y `cascade = CascadeType.PERSIST` para que se propaguen las operaciones de eliminacion y persistencia, y por último `orphanRemoval = true` para eliminar objetos "sueltos" (sin referencias).  
- En el lado de `ItemService` (lado "muchos"), se necesita la anotación `@ManyToOne` sobre el atributo que representa la referencia a `Purchase`. Este lado es el propietario de la relación, por lo que no se utiliza `mappedBy` aquí. Además, se puede agregar la anotación `@JoinColumn` para especificar el nombre de la columna que actuará como clave foránea en la base de datos. En este caso, la utilizaremos `@JoinColumn(name = "purchase_id")` para indicar que la columna `purchase_id` en la tabla de `ItemService` será la clave foránea que referencia a `Purchase`. 

#### b)​ ¿Qué columna o tabla aparece en la base de datos para representar esta relación?

- Aparece una columna llamada `purchase_id` en la tabla correspondiente a `ItemService`, que actúa como clave foránea y referencia a la tabla de `Purchase`. Esta columna se utiliza para establecer la relación entre cada registro de `ItemService` y su correspondiente registro de `Purchase`.

>[!NOTE]
> Esta columna se genera automáticamente por JPA debido a la anotación `@JoinColumn` en el lado de `ItemService`, y su nombre puede ser personalizado según las necesidades del desarrollador. En este caso, hemos elegido `purchase_id` para mantener una convención clara y descriptiva.

#### c)​ ¿Qué es mappedBy y en qué lado de la relación va? ¿Qué ocurre si se omite en ambos lados?

- `mappedBy` es un atributo de la anotación `@OneToMany` que se utiliza para indicar que esta parte de la relación es la inversa y que el propietario de la relación es el otro lado (en este caso, `ItemService`). Especifica el nombre del atributo en la clase del lado "muchos" que mantiene la referencia a la clase del lado "uno". En este caso, `mappedBy = "purchase"` indica que el atributo `purchase` en `ItemService` es el propietario de la relación.
- Si se omite `mappedBy` en ambos lados, JPA no podrá determinar cuál es el propietario de la relación, lo que resultará en la creación de una tabla intermedia adicional para gestionar la relación entre `Purchase` e `ItemService`, en lugar de utilizar una clave foránea directa en la tabla de `ItemService`. Esto puede llevar a una estructura de base de datos más compleja e ineficiente, ya que se requerirán operaciones adicionales para gestionar la relación entre las entidades.

#### d)​ ¿Es esta relación bidireccional o unidireccional según el diagrama? ¿Cómo se refleja en el código Java?

- Según el diagrama, esta relación es bidireccional, ya que tanto `Purchase` como `ItemService` mantienen referencias mutuas entre sí. En el código Java, esto se refleja mediante la presencia de un atributo en `Purchase` que es una colección de `ItemService` (con la anotación `@OneToMany`) y un atributo en `ItemService` que es una referencia a `Purchase` (con la anotación `@ManyToOne`). Esta configuración permite navegar desde una instancia de `Purchase` hacia sus `ItemService` asociados, así como desde una instancia de `ItemService` hacia su correspondiente `Purchase`.

### Ejercicio 17: ​Para las relaciones Route <-> DriverUser y Route <-> TourGuideUser (muchos-a-muchos):

#### a)​ ¿Qué anotaciones se usan?

- Las anotaciones que se utilizan son:
  - Del lado de `Route`, se utiliza la anotación `@ManyToMany` sobre los atributos que representan las colecciones de `DriverUser` y `TourGuideUser`. Además, utilizamos la anotación `@JoinTable` para definir explícitamente la tabla intermedia que gestionará la relación muchos-a-muchos, especificando los nombres de las columnas de clave foránea y declarando que no pueden ser nulos (`nullable = false`) ya que se especifica que la relación es obligatoria. 
  - Del lado de `DriverUser` y `TourGuideUser`, también se utiliza la anotación `@ManyToMany` sobre los atributos que representan las colecciones de `Route`. Además, utilizamos `mappedBy` para indicar que esta es la parte inversa de la relación y que el propietario es `Route`. En este lado, no utilizamos `@JoinTable` porque la tabla intermedia ya está definida en el lado de `Route`.

#### b)​ ¿Qué tabla adicional genera JPA? ¿Qué columnas tiene? Definirla explícitamente usando @JoinTable.

- JPA genera una tabla adicional para cada relación muchos-a-muchos. 
  - Para la relación entre `Route` y `DriverUser`, se generará una tabla llamada `route_driver_user` (o un nombre personalizado si se especifica en `@JoinTable`) con las siguientes columnas:
    - `route_id`: Clave foránea que referencia a la tabla de `Route`.
    - `driver_user_id`: Clave foránea que referencia a la tabla de `DriverUser`.
  - Para la relación entre `Route` y `TourGuideUser`, se generará otra tabla llamada `route_tour_guide_user` con las siguientes columnas:
    - `route_id`: Clave foránea que referencia a la tabla de `Route`.
    - `tour_guide_user_id`: Clave foránea que referencia a la tabla de `TourGuideUser`.

#### c)​ ¿Pueden ambas relaciones compartir la misma tabla join? ¿Por qué?

- No, ambas relaciones no pueden compartir la misma tabla join porque cada relación muchos-a-muchos representa una asociación distinta entre diferentes entidades. Cada tabla join debe ser específica para la relación que está gestionando, ya que contiene claves foráneas que hacen referencia a las entidades involucradas en esa relación particular. Compartir la misma tabla join para ambas relaciones podría generar confusión y problemas de integridad referencial, ya que no habría una forma clara de distinguir entre los registros que corresponden a la relación `Route`-`DriverUser` y los que corresponden a la relación `Route`-`TourGuideUser`. Por lo tanto, es necesario tener tablas join separadas para cada relación muchos-a-muchos.

### Ejercicio 18: ​La relación Purchase -> Review es opcional (0..1). Implementar el mapeo de ambos lados. ¿Cómo se representa la opcionalidad en JPA?

- Para mapear la relación opcional entre `Purchase` y `Review`, se utiliza la anotación `@OneToOne` en ambos lados de la relación. En el lado de `Purchase`, se puede agregar la anotación `@OneToOne(mappedBy = "purchase", cascade = CascadeType.REMOVE, optional = true, orphanRemoval = true)` para indicar que esta es la parte inversa de la relación y que el propietario es `Review`. En el lado de `Review`, se utiliza la anotación `@OneToOne` junto con `@JoinColumn(name = "purchase_id", nullable = false)` para establecer la clave foránea que referencia a `Purchase`.
- La opcionalidad se representa en JPA utilizando el atributo `optional = true` en la anotación de relación (`@OneToOne`, `@ManyToOne`, etc.) en el lado que es opcional.

### Ejercicio 19: ItemService referencia a Service (muchos-a-uno). Analizar el diagrama: ¿es navegable esta relación desde Service hacia ItemService? Justificar si conviene hacerla bidireccional o no.

- Es navegable, ya que `Service` tiene un atributo que es una colección de `ItemService`, por lo cual, se puede navegar desde una instancia de `Service` hacia sus `ItemService` asociados. 
- Es conveniente hacerla bidireccional porque permite una mayor flexibilidad al momento de acceder a los datos. Al tener la relación bidireccional, se puede navegar tanto desde `ItemService` hacia `Service` como desde `Service` hacia `ItemService`, lo que facilita la consulta y manipulación de los datos relacionados. En este caso no se nos especifican consultas que se deban realizar, por lo que la relacion de `Service` hacia `ItemService` no es estrictamente necesaria, pero puede ser útil para futuras funcionalidades o consultas que requieran acceder a los `ItemService` asociados a un `Service`. Además, al ser una relación muchos-a-uno, la cantidad de `ItemService` asociados a un `Service` puede ser significativa, por lo que tener la capacidad de navegar desde `Service` hacia `ItemService` puede mejorar la eficiencia y la claridad del código. También, deberíamos tener en cuenta que al ser bidireccional, se debe gestionar adecuadamente la sincronización de ambas partes de la relación para evitar inconsistencias en el modelo de datos, lo cual agrega cierta complejidad al código.

### Ejercicio 20.​Implementar el mapeo completo de las siguientes entidades con todas sus relaciones, siguiendo las anotaciones y decisiones discutidas: Supplier, Purchase, ItemService, Route, Stop y Review. Para cada relación bidireccional, incluir las anotaciones en ambos lados.

- Realizado en el repositorio 

### Ejercicio 42
 ¿Que es una transacción en el contexto de Hibernate? ¿Por qué es necesaria? ¿Qué 
ocurre si se realizan operaciones de escritura sin una transacción activa?

Se corresponde con una transacción de base de datos, es una unidad lógica de trabajo, que se completa toda o no se hace. Es necesaria porque si no se trabaja con ellas, puede haber corrupción o inconsistencia de datos resultante en la BD.

### Ejercicio 43
 ¿En qué capa de la aplicación debería gestionarse la transacción: en el repositorio o en la 
capa de servicio? Justificar la elección. ¿Qué ocurre si una misma operación necesita de 
varios accesos a la base de datos? 

En la capa de servicio, ya que 


tengo que hacer un refresh del user para que funcione el assert de los tests? (linea 233)
Estan bien hecho asi los adddrivertoroute y lo del itemservice? y bien distribuido entre las capas?
listas de rutas separadas en los users? pasa q sino te queda una sola lista de users en route, y no separado por driver y tourguide
siempre tengo que agregar la relación de los dos lados para que se persista?