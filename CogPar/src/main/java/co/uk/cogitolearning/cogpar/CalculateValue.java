package co.uk.cogitolearning.cogpar;

import co.uk.cogitolearning.cogpar.tree.ExpressionNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


public class CalculateValue {

    public CalculateValue() {
    }

    private static double calculatePolishNotation(ArrayList<ExpressionNode> list) {
        // https://en.wikipedia.org/wiki/Polish_notation
        Collections.reverse(list); // Scan the given prefix expression from right to left
        Stack<Double> stack = new Stack<>();
        CalculateVisitor calculateVisitor = new CalculateVisitor(stack);

        for (ExpressionNode node : list)
            node.acceptOnce(calculateVisitor);

        return stack.pop();
    }


    static public double calculate(ExpressionNode expr) {
        ArrayList<ExpressionNode> polishNotationList = new ArrayList<>();

        for (ExpressionNode node : (Iterable<ExpressionNode>) expr)
            polishNotationList.add(node);

        return CalculateValue.calculatePolishNotation(polishNotationList);
    }

}
