package nl.han.ica.icss.handler.typeHandlers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

public class ColorExpressionTypeHandler extends ExpressionTypehandler {
    @Override
    public boolean isSpecificInstance(ASTNode type) {
        return type instanceof ColorLiteral;
    }

    @Override
    protected ExpressionType getExpressionType() {
        return ExpressionType.COLOR;
    }
}
