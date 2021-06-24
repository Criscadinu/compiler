package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.properties.*;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.handler.typeHandlers.*;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.AddOrSubtractOperationHandler;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.OperationHandler;
import nl.han.ica.icss.utils.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Checker {
    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;
    private HANLinkedList<HashMap<String, String>> variableValues;
    private final ArrayList<ExpressionTypehandler> expressionTypehandlers = new ArrayList<>();
    private final ArrayList<OperationHandler> operationHandlers = new ArrayList<>();

    public Checker() {
        setExpressionTypeHandlers();
    }

    private void setExpressionTypeHandlers() {
        expressionTypehandlers.add(new BoolExpressionTypeHandler());
        expressionTypehandlers.add(new ColorExpressionTypeHandler());
        expressionTypehandlers.add(new PercentageExpressionTypeHandler());
        expressionTypehandlers.add(new PixelExpressionTypeHandler());
        expressionTypehandlers.add(new ScalarExpressionTypeHandler());
    }

    private void setOperationHandlers() {
        operationHandlers.add(new AddOrSubtractOperationHandler(variableTypes));
    }

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        variableValues = new HANLinkedList<>();
        validateNodes(ast.root.getChildren());
    }

    private void validateNodes(ArrayList<ASTNode> nodes) {
        for(ASTNode node : nodes) {
            checkForUndefinedVariables(node);
            setOperationHandlers();
            checkForUnApprovedOperations(node);
            checkForValidTypes(node);

            validateNodes(node.getChildren());
        }
    }

    private void checkForUndefinedVariables(ASTNode node) {
        selectAvailableVariables(node);
        setUndefinedVariablesError(node);
    }

    private void selectAvailableVariables(ASTNode node) {
        if (node instanceof VariableAssignment) {
            int startOfVariableName = 20;
            String name = node.getNodeLabel().substring(startOfVariableName, node.getNodeLabel().length() - 1);
            ASTNode type = node.getChildren().get(1);
            int index = type.getNodeLabel().lastIndexOf("(") + 1;
            String value = type.getNodeLabel().substring(index, type.getNodeLabel().length() - 1);
            setAvailableVariablesList(name, value);
            setExpressionTypeForHashmap(name, type);
        }
    }

    private void setAvailableVariablesList(String name, String value) {
        HashMap<String, String> hashmapWithValues = new HashMap<>();
        hashmapWithValues.put(name, value);
        variableValues.addFirst(hashmapWithValues);
    }

    private void setExpressionTypeForHashmap(String name, ASTNode type) {
        for (ExpressionTypehandler expressionTypeHandler : expressionTypehandlers) {
            if (expressionTypeHandler.isSpecificInstance(type)) {
                HashMap<String, ExpressionType> hashmap = new HashMap<>();
                hashmap.put(name, expressionTypeHandler.execute(type));
                variableTypes.addFirst(hashmap);
            }
        }
    }

    private void setUndefinedVariablesError(ASTNode node) {
        if (node instanceof VariableReference) {
            if (!doesVariableExists(node)) {
                node.setError(node.getNodeLabel() + " is undefined");
            }
        }
    }

    private boolean doesVariableExists(ASTNode node) {
        int startOfVariableName = 20;
        boolean variableExists = false;
        for (int i = 0; i < variableTypes.getSize(); i++) {
            String variableKey = node.toString().substring(startOfVariableName, node.getNodeLabel().length());
            if (variableTypes.get(i).containsKey(variableKey)) {
                variableExists =  true;
            }
        }
        return variableExists;
    }

    private void checkForUnApprovedOperations(ASTNode node) {
        checkForUnApprovedAddOrSubtractOperation(node);
//        getUnApprovedMultiplyOperation(node);
    }

    private void checkForUnApprovedAddOrSubtractOperation(ASTNode node) {
        for (OperationHandler handler : operationHandlers) {
            if (handler.isAddOrSubtractOperation(node)) {
                handler.execute(node);
            }
        }
    }

    private boolean compareNonOperationalOperands(String leftOperand, String rightOperand) {
        return leftOperand.equals(rightOperand);
    }

    private void getUnApprovedMultiplyOperation(ASTNode node) {

    }

    private void checkIfAllChildrenAreScalar(ASTNode node) {
        ASTNode operandRight = node.getChildren().get(1);
        if (! ((operandRight.getChildren().get(0)) instanceof ScalarLiteral)) {
            node.setError("Left child not scalar");
        } else if (!(operandRight.getChildren().get(1) instanceof Operation) && !(operandRight.getChildren().get(1) instanceof ScalarLiteral)) {
            node.setError("Right child not scalar");
        } else if (node.getChildren().get(1).getChildren().get(1) instanceof Operation) {
            checkIfAllChildrenAreScalar(node.getChildren().get(1));
        }
    }

    private void isColorLiteral(ASTNode operandLeft, ASTNode operandRight) {
        for (int i = 0; i < variableTypes.getSize(); i++) {
            String variableKey1 = operandLeft.toString().substring(20, operandLeft.getNodeLabel().length());
//            String variableKey2 = operandRight.toString().substring(20, operandRight.getNodeLabel().length());
            if (variableTypes.get(i).containsKey(variableKey1)) {
                ExpressionType literal = variableTypes.get(i).get(variableKey1);
                if (literal.equals(ExpressionType.COLOR)) {
                    operandLeft.setError("Is van het type COLOR");
                }
            }
        }
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
