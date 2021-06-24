package nl.han.ica.icss.utils;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

public class WidthAndHeightComparator implements ICompare {

    @Override
    public boolean compare(ASTNode propertyValue) {
        return propertyValue instanceof PixelLiteral || propertyValue instanceof PercentageLiteral || propertyValue instanceof Operation || propertyValue instanceof VariableReference;
    }
}
