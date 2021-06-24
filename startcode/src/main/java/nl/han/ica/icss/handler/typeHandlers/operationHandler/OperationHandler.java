package nl.han.ica.icss.handler.typeHandlers.operationHandler;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers.Handler;

public class OperationHandler {
    ASTNode leftOperand;
    ASTNode rightOperand;
    private Handler concreteHandler;

    public void execute(ASTNode node, Handler concreteHandler) {
        this.concreteHandler = concreteHandler;
        setOperands(node);
        this.concreteHandler.handle(leftOperand, rightOperand, node);
    }

    public void setOperands(ASTNode node) {
        leftOperand = node.getChildren().get(0);
        rightOperand = node.getChildren().get(1);
    }
}
