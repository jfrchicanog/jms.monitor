package es.uma.informatica.sii.jms.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
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
	private Session currentSession;
	
	@OnOpen
	public void open(Session session,
	                 EndpointConfig conf) { 
		currentSession = session;
		LOGGER.info("Se abre conexión de websocket");
	}

	@OnClose
	public void close(Session session, CloseReason reason) { 
		LOGGER.info("Se acierra conexión de websocket");
	}
	
	@OnError
	public void error(Session session, Throwable error) {
		LOGGER.severe(error.getMessage());
	}
	
	@OnMessage
	public void onMessage(Session session, String msg) {
		LOGGER.info("Llega mensaje del cliente: "+msg);
	}
	
	private void notificar(@Observes @Alta String cuenta) {
		try {
			currentSession.getBasicRemote().sendText(cuenta);
			LOGGER.info("Notificada el alta de "+cuenta);
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
	}

}
