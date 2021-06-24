package nl.han.ica.icss.utils;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.ColorLiteral;

public class ColorComparator implements ICompare {

    @Override
    public boolean compare(ASTNode propertyValue) {
        return propertyValue instanceof ColorLiteral || propertyValue instanceof VariableReference;
    }

}
