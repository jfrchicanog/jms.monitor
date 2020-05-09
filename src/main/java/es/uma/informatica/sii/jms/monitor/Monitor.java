package es.uma.informatica.sii.jms.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.WebsocketEvent;
import javax.faces.event.WebsocketEvent.Closed;
import javax.faces.event.WebsocketEvent.Opened;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

import es.uma.informatica.sii.jms.mdb.Alta;

@Named
@ApplicationScoped
public class Monitor implements Serializable {
	
	private final static Logger LOGGER = Logger.getLogger(Monitor.class.getCanonicalName());
	
	private List<String> registrados =  new ArrayList<>();
	
	@Inject @Push (channel = "notificacion")
	private PushContext notificacionAltas;
	
	public void notificar(@Observes @Alta String cuenta) {
		registrados.add(cuenta);
		notificacionAltas.send("actualizar");
		LOGGER.info("Notificada el alta de "+cuenta);
	}

	public List<String> getRegistrados() {
		return registrados;
	}
	
	public void pulsar() {
		notificar("prueba");
	}
	
	
	public void onOpen(@Observes @Opened WebsocketEvent event) { String channel = event.getChannel();
	LOGGER.info("Socket abierto: "+channel);
	// Returns <f:websocket channel>. Long userId = event.getUser();
	// Returns <f:websocket user>, if any.
	// ...

	}

	public void onClose(@Observes @Closed WebsocketEvent event) { String channel = event.getChannel();
	LOGGER.info("Socket cerrado: "+channel);
	// Returns <f:websocket channel>. Long userId = event.getUser();
	// Returns <f:websocket user>, if any. CloseCode code = event.getCloseCode();
	// Returns close reason code.
	// ...
	}

}
