# Ejecutar la PARTE 1 sin Maven (solo con JDK)

Si no tienes Maven a mano pero sí un JDK instalado, puedes ejecutar el generador
y reproducir la evidencia con estos comandos desde la carpeta `data-generator`:

```bash
# 1. Compilar el nucleo (modelo, patrones, generador, servicio, persistencia)
mkdir -p target/classes
find src/main/java/com/greensqa/datagen/model \
     src/main/java/com/greensqa/datagen/patterns \
     src/main/java/com/greensqa/datagen/generator \
     src/main/java/com/greensqa/datagen/util \
     src/main/java/com/greensqa/datagen/persistence \
     src/main/java/com/greensqa/datagen/service -name "*.java" > sources.txt
javac -d target/classes @sources.txt

# 2. Compilar y ejecutar el CLI de demostracion
javac -cp target/classes -d target/classes DemoCLI.java Evidencia.java

# 3. Generar datos (serial)
java -cp target/classes DemoCLI 100

# 4. Generar datos en paralelo
java -cp target/classes DemoCLI 500 --paralelo 4

# 5. Validar todas las reglas de negocio
java -cp target/classes Evidencia
```

El CSV se genera en `output/datos_evidencia.csv`.

NOTA: Esta via usa solo el JDK (almacenamiento en memoria + CSV con Java estandar)
y ejecuta la MISMA logica de negocio. La version completa con Maven agrega
persistencia en SQLite, CSV con OpenCSV y envio por correo; se ejecuta con:
`mvn clean package && java -jar target/data-generator.jar --cantidad 100`
