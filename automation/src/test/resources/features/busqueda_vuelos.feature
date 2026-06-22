# language: es
@busqueda_vuelos
Característica: Búsqueda de vuelos en LATAM
  Como pasajero que visita el sitio de LATAM
  Quiero buscar vuelos según mis necesidades
  Para poder comparar opciones y planificar mi viaje

  Antecedentes:
    Dado que el pasajero ingresa a la página de inicio de LATAM

  @caso1 @ida_y_vuelta @smoke
  Escenario: CP01 - Búsqueda de vuelo ida y vuelta para un pasajero adulto
    Cuando selecciona el tipo de viaje "Ida y vuelta"
    Y completa el origen "Bogotá" y el destino "Santiago"
    Y selecciona una fecha de ida a 20 días y una fecha de regreso a 27 días
    Y configura 1 pasajero adulto
    Y ejecuta la búsqueda de vuelos
    Entonces se muestran resultados de vuelos disponibles para la ruta seleccionada

  @caso2 @solo_ida @datos_csv
  Escenario: CP02 - Búsqueda solo ida usando datos de prueba generados
    Cuando toma un pasajero desde los datos de prueba generados
    Y selecciona el tipo de viaje "Solo ida"
    Y completa el origen "Medellín" y el destino "Lima"
    Y selecciona una fecha de ida a 15 días
    Y configura los pasajeros según la edad del dato de prueba
    Y ejecuta la búsqueda de vuelos
    Entonces se muestran resultados de vuelos disponibles para la ruta seleccionada

  @caso3 @validacion_negativa
  Escenario: CP03 - Validación de origen y destino iguales
    Cuando selecciona el tipo de viaje "Solo ida"
    Y intenta completar el origen "Bogotá" y el destino "Bogotá"
    Entonces el sistema impide buscar vuelos con el mismo origen y destino
