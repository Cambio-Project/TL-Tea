package cambio.tltea.parser.core.temporal;

public interface ITemporalExpressionValueHolder {

    default void setTemporalExpressionValue(String temporalValueExpression){
        setTemporalExpressionValue(TemporalPropositionParser.parse(temporalValueExpression));
    }

    void setTemporalExpressionValue(ITemporalValue temporalValueExpression);

    ITemporalValue getTemporalValue();

}
