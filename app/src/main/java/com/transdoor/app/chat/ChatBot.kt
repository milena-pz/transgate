package com.transdoor.app.chat


object ChatBot {

    // Genera una respuesta basada en la pregunta del usuario
    fun responder(pregunta: String): Mensaje {
        val textoRespuesta = when {
            // Acceder / login
            pregunta.contains("acceder", ignoreCase = true) ||
                    pregunta.contains("iniciar", ignoreCase = true) ||
                    pregunta.contains("conectar", ignoreCase = true) ||
                    pregunta.contains("login", ignoreCase = true) ->

                "Solo un administrador del sistema puede tener acceso en la app. Para más información, contacte con Transnaba Orihuela."

            // Buscar en lista
            pregunta.contains("buscar", ignoreCase = true) ||
                    pregunta.contains("encontrar", ignoreCase = true) ||
                    pregunta.contains("filtrar", ignoreCase = true) ->

                "Para filtrar la lista y encontrar el usuario que desea, escriba en la celda del buscador el nombre si lo conoce. Si sabe cómo empieza el número de teléfono, puede usar la celda correspondiente. Los filtros se aplican en tiempo real. ¡Suerte!"

            // Insertar / Alta
            pregunta.contains("insertar", ignoreCase = true) ||
                    pregunta.contains("alta", ignoreCase = true) ->

                "Para insertar un nuevo usuario, pulsa el botón ➕. Se abrirá una nueva ventana donde debe rellenar los datos del usuario."

            // Editar / Modificar
            pregunta.contains("editar", ignoreCase = true) ||
                    pregunta.contains("modificar", ignoreCase = true) ||
                    pregunta.contains("cambiar", ignoreCase = true) ->

                "Con una pulsación sobre el usuario elegido se muestran todos los datos en otra ventana, y se pueden editar como en un formulario."

            // Dar de baja
            pregunta.contains("baja", ignoreCase = true) ->

                "Para dar de baja un usuario, entra en el modo de editar. Si no cambias ningún otro dato, simplemente confirma la baja. Después serás redirigido a la pantalla para enviar el SMS de baja."

            // Enviar SMS
            pregunta.contains("sms", ignoreCase = true) ||
                    pregunta.contains("mensaje", ignoreCase = true) ->

                "Se pueden enviar SMS desde la ventana de insertar usuario y desde la opción de dar de baja un usuario."

            // Salir de la app
            pregunta.contains("salir", ignoreCase = true) ->

                "Hay un botón en la parte inferior derecha que permite salir de la aplicación. Si solo quieres cerrar la sesión, pulsa el botón 'Logout' en el centro."

            // Ayuda general
            pregunta.contains("ayuda", ignoreCase = true) ||
                    pregunta.contains("qué puedes hacer", ignoreCase = true) ->

                "Puedo ayudarte con temas como: acceder, buscar, insertar, editar, dar de baja, enviar SMS o salir de la aplicación. Solo escribe tu duda."

            // Saludo inicial (detecta que no hay texto aún)
            pregunta.isBlank() -> "Hola 👋 soy MIA. ¿Con qué puedo ayudarte?"

            // Mensaje por defecto
            else -> "Lo siento, no entiendo bien tu pregunta. Intenta con palabras como: acceder, buscar, insertar, editar, baja, sms, salir o ayuda."
        }

        return Mensaje(texto = textoRespuesta, esUsuario = false)
    }
}


