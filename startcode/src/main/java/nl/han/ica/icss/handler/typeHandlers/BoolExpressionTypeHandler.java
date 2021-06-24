package nl.han.ica.icss.handler.typeHandlers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

public class BoolExpressionTypeHandler extends ExpressionTypehandler {
    @Override
    public boolean isSpecificInstance(ASTNode type) {
        return type instanceof BoolLiteral;
    }

    @Override
    protected ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }
}
