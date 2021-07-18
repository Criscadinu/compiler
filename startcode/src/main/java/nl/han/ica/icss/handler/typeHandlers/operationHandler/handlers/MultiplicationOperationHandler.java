package nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;
import java.util.HashMap;

public class MultiplicationOperationHandler implements Handler {
    ASTNode leftOperand;
    ASTNode rightOperand;
    VariablesHandler handler;

    public MultiplicationOperationHandler(VariablesHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(ASTNode node) {
        setOperands(node);
        setErrorIfNoScalar(node);

        if (rightOperandIsAddOrSubtractOperation()) {
            startAddOrSubtractValidation(node);
        }

        if (handler.oneOrMoreOperandsIsVariableReference(leftOperand, rightOperand)) {
            handler.setVariableTypesInOperation(leftOperand, rightOperand);
            validate(node);
        }
    }

    private void startAddOrSubtractValidation(ASTNode node) {
        checkForUnequalChildOperands(node);
    }

    private void checkForUnequalChildOperands(ASTNode node) {
        if (!leftChildOperandIsEqualToLeftOperand() && !oneOrMoreOperandsIsVariableReference()) {
            node.setError("Left operand is not equal to left operand of child");
        }
    }

    private boolean leftChildOperandIsEqualToLeftOperand() {
        HANLinkedList<HashMap<String, Object>> symbolTable;
        symbolTable = handler.getSymbolTable();
        ExpressionType leftOperandType = handler.getTypeOf(leftOperand);
        ExpressionType leftOperandTypeOfRightOperand = handler.getTypeOf(rightOperand.getChildren().get(0));
        String name = getVariableNameFrom(rightOperand.getChildren().get(0));

        if (rightOperand.getChildren().get(0) instanceof VariableReference) {
            ExpressionType type = null;
            for (int i = 0; i < symbolTable.getSize(); i++) {
                if (symbolTable.get(i).get("name").equals(name)) {
                    type = (ExpressionType) symbolTable.get(i).get("type");
                }
            }
            return leftOperandType.equals(type);
        }
        return leftOperandType.equals(leftOperandTypeOfRightOperand);
    }

    private String getVariableNameFrom(ASTNode variableReference) {
        int startingIndex = variableReference.toString().indexOf("(") + 1;
        return variableReference.toString().substring(startingIndex, variableReference.getNodeLabel().length());
    }

    private boolean oneOrMoreOperandsIsVariableReference() {
        return leftOperand instanceof VariableReference || rightOperand instanceof VariableReference;
    }

    private boolean rightOperandIsAddOrSubtractOperation() {
        return rightOperand instanceof AddOperation || rightOperand instanceof SubtractOperation;
    }

    public void setOperands(ASTNode node) {
        leftOperand = node.getChildren().get(0);
        rightOperand = node.getChildren().get(1);
    }

    public ExpressionType getEvaluatedExpressionType(ASTNode multiplicationNode) {
        ExpressionType multiplicationNodeLeftType = handler.getTypeOf(multiplicationNode.getChildren().get(0));
        ExpressionType multiplicationNodeRightType = handler.getTypeOf(multiplicationNode.getChildren().get(1));

        return multiplicationNodeLeftType == ExpressionType.SCALAR ? multiplicationNodeRightType : multiplicationNodeLeftType;
    }

    private void validate(ASTNode node) {
        if (handler.getVariableTypesInOperation().size() > 1) {
            setErrorIfNoScalar(node);
        }
    }

    private void setErrorIfNoScalar(ASTNode multiplicationNode) {
        ExpressionType multiplicationNodeLeftType = handler.getTypeOf(multiplicationNode.getChildren().get(0));
        ExpressionType multiplicationNodeRightType = handler.getTypeOf(multiplicationNode.getChildren().get(1));

        if (!(multiplicationNodeLeftType == ExpressionType.SCALAR) && !(multiplicationNodeRightType == ExpressionType.SCALAR)) {
            multiplicationNode.setError("Does not contain scalar");
        }
    }
}
