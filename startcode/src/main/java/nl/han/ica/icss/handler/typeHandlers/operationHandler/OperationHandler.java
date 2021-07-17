package nl.han.ica.icss.handler.typeHandlers.operationHandler;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.handler.typeHandlers.operationHandler.handlers.Handler;

public class OperationHandler {
    private Handler concreteHandler;

    public void execute(ASTNode node, Handler concreteHandler) {
        this.concreteHandler = concreteHandler;
        this.concreteHandler.handle(node);
    }
}
