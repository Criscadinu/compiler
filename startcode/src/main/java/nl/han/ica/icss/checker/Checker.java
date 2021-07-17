package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.operations.*;
import nl.han.ica.icss.ast.properties.*;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.OperationHandler;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers.AddOrSubtractOperationHandler;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers.VariablesHandler;
import nl.han.ica.icss.utils.comparator.ColorComparator;
import nl.han.ica.icss.utils.comparator.*;
import nl.han.ica.icss.utils.comparator.calculator.Calculator;
import java.util.*;

public class Checker {
    private final VariablesHandler variablesHandler;
    private final OperationHandler operationHandler;
    private final Calculator calculator;

    public Checker() {
        variablesHandler = new VariablesHandler();
        operationHandler = new OperationHandler();
        calculator = new Calculator();
    }

    public void check(AST ast) {
        validateNodes(ast.root.getChildren());
    }

    private void validateNodes(ArrayList<ASTNode> nodes) {
        for(ASTNode node : nodes) {
            variablesHandler.checkForUndefinedVariables(node);
            checkForUnApprovedOperations(node, variablesHandler);
//            calculateSumOfOperation(node);
            checkForValidTypes(node);

            validateNodes(node.getChildren());
        }
    }

    private void checkForUnApprovedOperations(ASTNode node, VariablesHandler variablesHandler) {
        if (node instanceof AddOperation || node instanceof SubtractOperation) {
            operationHandler.execute(node, new AddOrSubtractOperationHandler(this.variablesHandler));
        }
        else if (node instanceof MultiplyOperation){
//            operationHandler.execute(node, new MultiplyOperationHandler(variableTypes));
        }
    }

    private void calculateSumOfOperation(ASTNode node) {
        if (node instanceof Operation) {
            this.calculator.setTokens(node, variablesHandler);
        }
        int sum = calculator.calculate();
    }

    private void checkForValidTypes(ASTNode node) {
        if (node instanceof Declaration) {
            compareDeclarations(node);
        }
    }

    private void compareDeclarations(ASTNode node) {
        ASTNode propertyName = node.getChildren().get(0);
        ASTNode propertyType = node.getChildren().get(1);
        if (propertyName instanceof ColorProperty || propertyName instanceof BackgroundProperty) {
            ICompare comparator = new ColorComparator();
            if (!comparator.compare(propertyType)) {
                node.setError("Should be color literal");
            }
        } else if (propertyName instanceof WidthProperty || propertyName instanceof HeightProperty) {
            ICompare comparator = new WidthAndHeightComparator();
            if (!comparator.compare(propertyType)) {
                node.setError("Should be pixel or percentage literal");
            }
        }
    }
}
