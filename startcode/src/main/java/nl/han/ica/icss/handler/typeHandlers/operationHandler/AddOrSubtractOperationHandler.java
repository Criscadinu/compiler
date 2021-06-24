package nl.han.ica.icss.handler.typeHandlers.operationHandler;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class AddOrSubtractOperationHandler extends OperationHandler{

    public AddOrSubtractOperationHandler(HANLinkedList<HashMap<String, ExpressionType>> types) {
        super(types);
    }

    @Override
    public boolean rightOperandIsAddOrSubtractOperation(ASTNode rightOperand) {
        return rightOperand instanceof AddOperation || rightOperand instanceof SubtractOperation;
    }

    @Override
    protected boolean oneOreMoreOperandsIsVariableReference(ASTNode leftOperand, ASTNode rightOperand) {
        return leftOperand instanceof VariableReference || rightOperand instanceof VariableReference;
    }

    @Override
    public boolean leftChildOperandIsEqualToLeftOperand(ASTNode leftOperand, ASTNode rightOperand) {
        String leftOperandType = leftOperand.getClass().getSimpleName();
        String leftOperandTypeOfRightOperand = rightOperand.getChildren().get(0).getClass().getSimpleName();

        return leftOperandType.equals(leftOperandTypeOfRightOperand);
    }

    @Override
    public boolean isAddOrSubtractOperation(ASTNode node) {
        return node instanceof AddOperation || node instanceof SubtractOperation;
    }

    @Override
    public boolean operandsAreEqual(ASTNode leftOperand, ASTNode rightOperand) {
        return leftOperand.getClass().getSimpleName().equals(rightOperand.getClass().getSimpleName());
    }

    @Override
    protected void setError(ASTNode node) {
        node.setError("Not a valid add or subtract operation");
    }
}
