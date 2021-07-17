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
    ASTNode leftOperand;
    ASTNode rightOperand;
    VariablesHandler handler;

    public MultiplyOperationHandler(VariablesHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(ASTNode node) {
        setOperands(node);


    }
    public void setOperands(ASTNode node) {
        leftOperand = node.getChildren().get(0);
        rightOperand = node.getChildren().get(1);
    }

}
