package cambio.tltea.interpreter;

import cambio.tltea.interpreter.nodes.cause.EventActivationListener;
import cambio.tltea.interpreter.nodes.cause.CauseNode;
import cambio.tltea.interpreter.nodes.TriggerNotifier;
import cambio.tltea.parser.core.ASTNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Lion Wagner
 */
public final class BehaviorInterpretationResult {
    private ASTNode modifiedAST;
    private CauseNode<?> interpretedAST;
    private List<EventActivationListener> listeners;
    private TriggerNotifier triggerNotifier;

    /**
     */
    public BehaviorInterpretationResult(ASTNode modifiedAST, CauseNode<?> interpretedAST,
                                        List<EventActivationListener> listeners,
                                        TriggerNotifier triggerNotifier) {
        this.modifiedAST = modifiedAST;
        this.interpretedAST = interpretedAST;
        this.listeners = listeners;
        this.triggerNotifier = triggerNotifier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BehaviorInterpretationResult) obj;
        return Objects.equals(this.modifiedAST, that.modifiedAST) &&
               Objects.equals(this.interpretedAST, that.interpretedAST) &&
               Objects.equals(this.listeners, that.listeners) &&
               Objects.equals(this.triggerNotifier, that.triggerNotifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifiedAST, interpretedAST, listeners, triggerNotifier);
    }

    @Override
    public String toString() {
        return "BehaviorInterpretationResult[" +
               "modifiedAST=" + modifiedAST + ", " +
               "interpretedAST=" + interpretedAST + ", " +
               "listener=" + listeners + ", " +
               "triggerNotifier=" + triggerNotifier + ']';
    }

    public ASTNode getModifiedAST() {
        return modifiedAST;
    }

    void setModifiedAST(ASTNode modifiedAST) {
        this.modifiedAST = modifiedAST;
    }

    public CauseNode<?> getInterpretedAST() {
        return interpretedAST;
    }

    void setInterpretedAST(CauseNode<?> interpretedAST) {
        this.interpretedAST = interpretedAST;
    }

    @Contract(pure = true)
    public @NotNull @UnmodifiableView List<EventActivationListener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    public TriggerNotifier getTriggerNotifier() {
        return triggerNotifier;
    }

    void addListener(EventActivationListener listener) {
        this.listeners.add(listener);
    }
}
