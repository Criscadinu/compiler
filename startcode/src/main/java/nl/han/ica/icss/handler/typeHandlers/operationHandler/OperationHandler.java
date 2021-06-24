package nl.han.ica.icss.handler.typeHandlers.operationHandler;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.types.ExpressionType;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class OperationHandler {
    ASTNode leftOperand;
    ASTNode rightOperand;
    boolean leftOperandIsVariable;
    boolean rightOperandIsVariable;
    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;
    private ArrayList<String> variableTypesInOperation;

    public OperationHandler(HANLinkedList<HashMap<String, ExpressionType>> types) {
        variableTypes = types;
        variableTypesInOperation = new ArrayList<>();
    }

    public void execute(ASTNode node) {
        setOperands(node);
        if (rightOperandIsAddOrSubtractOperation(rightOperand)) {
            if (!leftChildOperandIsEqualToLeftOperand(leftOperand, rightOperand) && !oneOreMoreOperandsIsVariableReference(leftOperand, rightOperand)) {
                setError(node);
            }
        } else if (oneOreMoreOperandsIsVariableReference(leftOperand, rightOperand)) {
            setVariableTypesInOperation(leftOperand, rightOperand);
            validate(node);
        } else if (!operandsAreEqual(leftOperand, rightOperand)) {
            setError(node);
        }
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

    public void compareExpressionTypeToLiteral(ASTNode node) {
        String literalType = getTypeOfLiteral();
        if (!variableTypesInOperation.get(0).equals(literalType)) {
            setError(node);
        }
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

    public String getTypeOfLiteral() {
        String literal = setCorrectLiteral();
        int endIndex = literal.indexOf("L");

        return literal.substring(0, endIndex).toUpperCase();
    }

    public String getOperandAsString(ASTNode node) {
        int startingIndex = node.toString().indexOf("(") + 1;
        return node.toString().substring(startingIndex, node.getNodeLabel().length());
    }

    public void setOperands(ASTNode node) {
        leftOperand = node.getChildren().get(0);
        rightOperand = node.getChildren().get(1);
    }

    public abstract boolean rightOperandIsAddOrSubtractOperation(ASTNode rightOperand);
    protected abstract boolean oneOreMoreOperandsIsVariableReference(ASTNode leftOperand, ASTNode rightOperand);
    public abstract boolean leftChildOperandIsEqualToLeftOperand(ASTNode leftOperand, ASTNode rightOperand);
    public abstract boolean isAddOrSubtractOperation(ASTNode node);
    public abstract boolean operandsAreEqual(ASTNode leftOperand, ASTNode rightOperand);
    protected abstract void setError(ASTNode node);
}
