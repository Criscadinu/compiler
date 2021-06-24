package nl.han.ica.icss.handler.typeHandlers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

public class PixelExpressionTypeHandler extends ExpressionTypehandler {


    @Override
    public boolean isSpecificInstance(ASTNode type) {
        return type instanceof PixelLiteral;
    }

    @Override
    protected ExpressionType getExpressionType() {
        return ExpressionType.PIXEL;
    }
}
