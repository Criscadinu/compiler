package nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers;


import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiplyOperationHandler implements Handler {
    boolean leftOperandIsVariable;
    boolean rightOperandIsVariable;
    ASTNode leftOperand;
    ASTNode rightOperand;
    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;
    private ArrayList<String> variableTypesInOperation;

    public MultiplyOperationHandler(HANLinkedList<HashMap<String, ExpressionType>> variableTypes) {
        variableTypesInOperation = new ArrayList<>();
        this.variableTypes = variableTypes;
    }

    @Override
    public void handle(ASTNode leftOperand, ASTNode rightOperand, ASTNode node) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;

        if (rightOperandIsAddOrSubtractOperation(rightOperand)) {
            if (!leftChildOperandIsEqualToLeftOperand(leftOperand, rightOperand) && !oneOrMoreOperandsIsVariableReference(leftOperand, rightOperand)) {
                setError(node);
            }
        } else if (oneOrMoreOperandsIsVariableReference(leftOperand, rightOperand)) {
            setVariableTypesInOperation(leftOperand, rightOperand);
            validate(node);
        } else if (!operandsAreEqual(leftOperand, rightOperand)) {
            setError(node);
        }
    }

    private boolean operandsAreEqual(ASTNode leftOperand, ASTNode rightOperand) {
        return leftOperand.getClass().getSimpleName().equals(rightOperand.getClass().getSimpleName());
    }

    private boolean oneOrMoreOperandsIsVariableReference(ASTNode leftOperand, ASTNode rightOperand) {
        return leftOperand instanceof VariableReference || rightOperand instanceof VariableReference;
    }

    private boolean leftChildOperandIsEqualToLeftOperand(ASTNode leftOperand, ASTNode rightOperand) {
        String leftOperandType = leftOperand.getClass().getSimpleName();
        String leftOperandTypeOfRightOperand = rightOperand.getChildren().get(0).getClass().getSimpleName();

        return leftOperandType.equals(leftOperandTypeOfRightOperand);
    }

    private boolean rightOperandIsAddOrSubtractOperation(ASTNode rightOperand) {
        return rightOperand instanceof AddOperation || rightOperand instanceof SubtractOperation;
    }

    public void setVariableTypesInOperation(ASTNode leftOperand, ASTNode rightOperand) {
        String leftVariableName = getOperandAsString(leftOperand);
        String rightVariableName = getOperandAsString(rightOperand);

        for (int i = 0; i < variableTypes.getSize(); i++) {
            if (variableTypes.get(i).containsKey(leftVariableName)) {
                variableTypesInOperation.add(variableTypes.get(i).get(leftVariableName).toString());
                leftOperandIsVariable = true;
            } else if (variableTypes.get(i).containsKey(rightVariableName)) {
                variableTypesInOperation.add(variableTypes.get(i).get(rightVariableName).toString());
                rightOperandIsVariable = true;
            }
        }
    }

    public String getOperandAsString(ASTNode node) {
        int startingIndex = node.toString().indexOf("(") + 1;
        return node.toString().substring(startingIndex, node.getNodeLabel().length());
    }
    public void validate(ASTNode node) {
        if (variableTypesInOperation.size() > 1) {
            compareExpressionTypes(node);
        } else {
            compareExpressionTypeToLiteral(node);
        }
    }

    public void compareExpressionTypes(ASTNode node) {
        if (!variableTypesInOperation.get(0).equals(variableTypesInOperation.get(1))) {
            setError(node);
        }
    }

    public void setError(ASTNode node) {
        node.setError("IETS");
    }

    public void compareExpressionTypeToLiteral(ASTNode node) {
        String literalType = getTypeOfLiteral();
        if (!variableTypesInOperation.get(0).equals(literalType)) {
            setError(node);
        }
    }

    public String getTypeOfLiteral() {
        String literal = setCorrectLiteral();
        int endIndex = literal.indexOf("L");

        return literal.substring(0, endIndex).toUpperCase();
    }

    public String setCorrectLiteral() {
        String literal = "";

        if (leftOperandIsVariable) {
            literal = rightOperand.getClass().getSimpleName();
        } else {
            literal = leftOperand.getClass().getSimpleName();
        }
        return literal;
    }
}
