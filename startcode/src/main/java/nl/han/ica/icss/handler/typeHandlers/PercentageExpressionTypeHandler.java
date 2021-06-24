package nl.han.ica.icss.handler.typeHandlers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

public class PercentageExpressionTypeHandler extends ExpressionTypehandler {
    @Override
    public boolean isSpecificInstance(ASTNode type) {
        return type instanceof PercentageLiteral;
    }

    @Override
    protected ExpressionType getExpressionType() {
        return ExpressionType.PERCENTAGE;
    }
}
