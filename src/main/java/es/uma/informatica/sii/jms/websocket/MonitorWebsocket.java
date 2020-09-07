package es.uma.informatica.sii.jms.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Named;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import es.uma.informatica.sii.jms.mdb.Alta;

@ServerEndpoint("/monitor")
public class MonitorWebsocket {
	private static final Logger LOGGER = Logger.getLogger(MonitorWebsocket.class.getCanonicalName());
	private static Set<Session> sesiones = Collections.synchronizedSet(new HashSet<>());
	
	@OnOpen
	public void open(Session session,
	                 EndpointConfig conf) { 
		sesiones.add(session);
		LOGGER.info("Se abre conexión de websocket");
	}

	@OnClose
	public void close(Session session, CloseReason reason) { 
		LOGGER.info("Se acierra conexión de websocket");
		sesiones.remove(session);
	}
	
	@OnError
	public void error(Session session, Throwable error) {
		LOGGER.severe(error.getMessage());
	}
	
	@OnMessage
	public void onMessage(Session session, String msg) {
		LOGGER.info("Llega mensaje del cliente: "+msg);
	}
	
	public static void notificar(@Observes @Alta String cuenta) {
		LOGGER.info("Notifican al Websocket");
		try {
			synchronized (sesiones) {
				for (Session sesion: sesiones) {
					LOGGER.info("Enviado mensaje al otro lado del WebSocket sobre "+cuenta);
					sesion.getBasicRemote().sendText(cuenta);
				}
			}
			LOGGER.info("Notificada el alta de "+cuenta);
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}

}
