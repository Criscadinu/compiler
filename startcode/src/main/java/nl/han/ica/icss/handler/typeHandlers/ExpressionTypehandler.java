package nl.han.ica.icss.handler.typeHandlers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.types.ExpressionType;

public abstract class ExpressionTypehandler {

    public ExpressionType execute(ASTNode type) {
        if (!isSpecificInstance(type)) {
            return ExpressionType.UNDEFINED;
        }
        return getExpressionType();
    }

    public abstract boolean isSpecificInstance(ASTNode type);
    protected abstract ExpressionType getExpressionType();
}
