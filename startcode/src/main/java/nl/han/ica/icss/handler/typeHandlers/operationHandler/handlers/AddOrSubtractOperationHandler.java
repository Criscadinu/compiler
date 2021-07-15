package nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.OperationHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class AddOrSubtractOperationHandler implements Handler {
    boolean leftOperandIsVariable;
    boolean rightOperandIsVariable;
    ASTNode leftOperand;
    ASTNode rightOperand;
    private HANLinkedList<HashMap<String, Object>> symbolTable;
    private ArrayList<String> variableTypesInOperation;

    public AddOrSubtractOperationHandler( HANLinkedList<HashMap<String, Object>> symbolTable) {
        variableTypesInOperation = new ArrayList<>();
        this.symbolTable = symbolTable;
    }

    @Override
    public void handle(ASTNode node) {
        setOperands(node);
        if (rightOperandIsAddOrSubtractOperation()) {
            checkForUnequalChildOperand(node);
        } else if (oneOrMoreOperandsIsVariableReference()) {
            setVariableTypesInOperation();
            validate(node);
        } else if (!operandsAreEqual()) {
            setError(node);
        }
    }

    public void setOperands(ASTNode node) {
        leftOperand = node.getChildren().get(0);
        rightOperand = node.getChildren().get(1);
    }

    private boolean rightOperandIsAddOrSubtractOperation() {
        return rightOperand instanceof AddOperation || rightOperand instanceof SubtractOperation;
    }

    private void checkForUnequalChildOperand(ASTNode node) {
        if (leftChildOperandIsEqualToLeftOperand() || oneOrMoreOperandsIsVariableReference()) {
            return;
        }
        setError(node);
    }

    private boolean leftChildOperandIsEqualToLeftOperand() {
        String leftOperandType = leftOperand.getClass().getSimpleName();
        String leftOperandTypeOfRightOperand = rightOperand.getChildren().get(0).getClass().getSimpleName();

        return leftOperandType.equals(leftOperandTypeOfRightOperand);
    }

    private boolean oneOrMoreOperandsIsVariableReference() {
        return leftOperand instanceof VariableReference || rightOperand instanceof VariableReference;
    }

    public void setVariableTypesInOperation() {
        String leftVariableName = getOperandAsString(leftOperand);
        String rightVariableName = getOperandAsString(rightOperand);

        for (int i = 0; i < symbolTable.getSize(); i++) {
            if (symbolTable.get(i).get("name").equals(leftVariableName)) {
                variableTypesInOperation.add(symbolTable.get(i).get("type").toString());
                leftOperandIsVariable = true;
            }
            if (symbolTable.get(i).get("name").equals(rightVariableName)) {
                variableTypesInOperation.add(symbolTable.get(i).get("type").toString());
                rightOperandIsVariable = true;
            }
        }
    }

    private boolean operandsAreEqual() {
        return leftOperand.getClass().getSimpleName().equals(rightOperand.getClass().getSimpleName());
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

    private void compareExpressionTypes(ASTNode node) {
        if (!variableTypesInOperation.get(0).equals(variableTypesInOperation.get(1))) {
            setError(node);
        }
    }

    private void setError(ASTNode node) {
        node.setError("Operands are not of equal type");
    }

    private void compareExpressionTypeToLiteral(ASTNode node) {
        String literalType = getTypeOfLiteral();
        if (variableTypesInOperation.size() == 0) {
            return;
        }
        if (!variableTypesInOperation.get(0).equals(literalType)) {
            setError(node);
        }
    }

    private String getTypeOfLiteral() {
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
