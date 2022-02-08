package cambio.tltea.interpreter.nodes.consequence;

import cambio.tltea.interpreter.nodes.StateChangeEvent;
import cambio.tltea.interpreter.nodes.StateChangedPublisher;

import java.util.LinkedList;

/**
 * @author Lion Wagner
 */
public abstract class ConsequenceNode {

    public abstract LinkedList<EventActivationDescription> activate();

}
