package nl.han.ica.icss.handler.typeHandlers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

public class ScalarExpressionTypeHandler extends ExpressionTypehandler {
    @Override
    public boolean isSpecificInstance(ASTNode type) {
        return type instanceof ScalarLiteral;
    }

    @Override
    protected ExpressionType getExpressionType() {
        return ExpressionType.SCALAR;
    }
}
