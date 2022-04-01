# AREP-Parcial02

## Enunciado.

Diseñe un prototipo de calculadora de **microservicios** que tenga un servicios de matemáticas con al menos dos funciones implementadas y desplegadas en al menos dos instancias virtuales de EC2.
Además debe implementar un service proxy que recibe las solicitudes de servicios y se las delega a las dos instancias usando un algoritmo de round-robin.
Asegúrese que se pueden configurar las direcciones y puertos de las instancias en el proxy usando variables de entorno del sistema operativo. 
Cada estudiante debe seleccionar para desarrollar dos funciones matemáticas de acuerdo a los dos últimos dígitos de su cédula como se especifica en la lista 
(Si sus dos últimos dígitos de su cédula son el mismo use el siguiente dígito que sea diferente). 
Todas las funciones reciben un solo parámetro de tipo "Double" y retornan un parámetro de tipo "Double".

Las elegidas para esta implementación en especifico fueron:

- ln (Logaritmo natural del número ingresado)
- asin (Arcoseno del número ingresado, debe estar entre -1 y 1)

Implemente los servicios para responder al método de solicitud HTTP GET. 
Deben usar el nombre de la función especificado en la lista y el parámetro debe ser pasado en la variable de query con nombre "value".

El proxy debe delegar el llamado a los servicios de backend. El proxy y los servicios se deben implementar en Java usando Spark.

Ejemplo de un llamado a la instancia proxy EC2:

https://amazonxxx.x.xxx.x.xxx:{port}/cos?value=3.141592

El formato de la respuesta de los miroservicios debe de ser un JSON con los siguientes campos:

```json
{

 "operation": "cos",

 "input":  3.141592,

 "output":  -0.999999

}

```

## Entregable

1. Proyecto actualizado en github (uno o dos repositorios, incluya referencias al repositorio alterno en el repositorio que entregará).
2. Descripción completa del proyecto en el README con pantallazos que muestren el funcionamiento.
3. Descripción de cómo correrlo en una instancia EC2.
4. Video de menos de un minuto del funcionamiento.

## Desarrollo

### MathServices (Funcionamiento local).

Generamos un proyecto maven, y dentro del ```pom.xml``` colocamos las dependencias necesarias para poder crear un servicio web sencillo con Spark.
En este caso lo llamaremos ```SparkWebApp```, a la par crearemos un paquete llamado ```services``` con una clase llamada ```MathServices```, esta será la encargada de proporcionar las respuestas a las operaciones asignadas.
```SparkWebApp``` constará de dos consultas get, que tomaran el valor del parametro value de la siguiente forma:

```localhost:PORT/operacion?value=Number```

Ejecutando el proyecto de manera local, debería poder recibir respuesta del microservicio dentro de un explorador o cliente web.
Tal como se muestra a continuación:

#### Logaritmo natural (local)

![LNREQUEST](/img/lnRequest.png)

#### Arcoseno (local)

![ASINREQUEST](/img/asinRequest.png)

### Creación del proxy.

Aprovechando la estructura de ```SparkWebApp```, la copiaremos dentro de la clase ```Proxy```, enviando las solicitudes get a los microservicios creados.
¿Cómo? Asignandoles un puerto específico a cada una dentro de la consola desde la que se vayan a ejecutar cada una. Además otorgaremos la posibilidad de poder leer estos datos de variables de entorno.

```java
static String server1(){
        if (System.getenv("SERVERN") != null) {
            return System.getenv("SERVERN").toString();
        }return "http://localhost:XXXX";
    }
```
De esta forma se permite una escalabilidad relativamente sencilla.
Para el correcto funcionamiento del proxy, es necesario que todas las instancias estén siendo ejecutadas, funcionen correctamente y tengan un puerto único establecido.

### Migración a EC2

Es necesario crear el número de instancias como tantos microservicios + 1 se deseen utilizar, la instancia adicional será usada para el proxy.
Generando un único par de claves funciona, utilizando la misma para todas las conexiones SSH que se necesiten utilizar. Dentro de cada una de ellas es necesario transferir el proyecto completo, o en su defecto, la carpeta target generada con los binarios (bien por medio de una consexión sftp, git o docker).
Además, es necesario instalar java en todas las instancias EC2. PAra esto lo hacemos con los siguientes comandos:

```
sudo yum install java-1.8.0

sudo yum install java-1.8.0-openjdk-devel
```
Una vez cumplamos con estos requisitos con cada una de las instancias EC2 que cumpliran la función de microservicios, ejecutaremos el servicio con:

```
java -cp "target/classes:target/dependency/*" SparkWebApp
```
Y para la instancia que ejercerá de proxy:

```
java -cp "target/classes:target/dependency/*" Proxy
```

Si se setearon correctamente las variables de entorno y puertos, únicamente debería ser necesario acceder a la instancia de proxy para poder obtener los resultados de las operaciones deseadas.

### Evidencia en AWS

#### Logaritmo natural (AWS)

![LNREQUEST](/img/lnAWS.png)

#### Arcoseno (AWS)

![ASINREQUEST](/img/asinAWS.png)

#### Video de funcionamiento

[Video evidencia](https://youtu.be/JnYLq5zerFg)

