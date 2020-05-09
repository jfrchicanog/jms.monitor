package es.uma.informatica.sii.jms.mdb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "java:/jms/topic/altausuario"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Topic")
		}, 
		mappedName = "java:/jms/topic/altausuario")
public class AltaUsuario implements MessageListener {
	
	private static final Logger LOGGER = Logger.getLogger(AltaUsuario.class.getCanonicalName());
	
	@Inject
	@Alta
	private Event<String> altaUsuario;
	
    public void onMessage(Message message) {
    	try {
    	if (message instanceof MapMessage) {
    		String cuenta = ((MapMessage)message).getString("cuenta");
    		altaUsuario.fire(cuenta);
    		LOGGER.info("Recibida el alta de "+cuenta+" y lanzado evento");
    	}
    	} catch (JMSException e) {
    		LOGGER.severe("Error al recibir el mensaje en monitor");
    	}
    }

}
