package nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers;


import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
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

        if (!scalarExists()) {
            node.setError("Missing at least 1 scalar");
        }

        if (booleanExists()) {
            node.setError("Boolean not allowed in multiplication operation");
        }
    }

    public void setOperands(ASTNode node) {
        leftOperand = node.getChildren().get(0);
        rightOperand = node.getChildren().get(1);
    }

    private boolean scalarExists() {
        return leftOperand instanceof ScalarLiteral || rightOperand instanceof ScalarLiteral;
    }

    private boolean booleanExists() {
        return leftOperand instanceof BoolLiteral || rightOperand instanceof BoolLiteral;
    }

    public ExpressionType getEvaluatedExpressionType(ASTNode multiplicationNode) {
        ExpressionType multiplicationNodeLeftType = handler.getTypeOf(multiplicationNode.getChildren().get(0));
        ExpressionType multiplicationNodeRightType = handler.getTypeOf(multiplicationNode.getChildren().get(1));

        return multiplicationNodeLeftType == ExpressionType.SCALAR ? multiplicationNodeRightType : multiplicationNodeLeftType;
    }

}
