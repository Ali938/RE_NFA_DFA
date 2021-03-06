package NFA.transition;

import NFA.Expression;
import NFA.Node;
import NFA.NodeKind;

import java.util.ArrayList;


public class Transition {

    private Node start;
    private Node end;
    private Expression expression;

    Transition() {
    }

    public Transition(Node start, Node end, Expression expression) {
        this.start = start;
        this.end = end;
        this.expression = expression;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public Expression getExpression() {
        return expression;
    }

    public ArrayList<Transition> simplify() {
        if (expression.isFinal()) {
            return null;
        } else {
            switch (expression.getOperation()) {
                case concat: {
                    return (new AndTransition(start, end, expression.getFirstSequencePart(), expression.getSecondSequencePart())).getFirstStepTransition();
                }
                case or: {
                    return (new OrTransition(start, end, expression.getFirstSequencePart(), expression.getSecondSequencePart())).getFirstStepTransition();
                }
                case star: {
                    Node tempEnd = new Node(NodeKind.normal);
                    StarTransition star = new StarTransition(start, tempEnd, expression.getFirstSequencePart());

                    String temp = expression.getSecondSequencePart();
                    Expression tempEx;
                    if (!temp.isEmpty()) {
                        tempEx = new Expression(temp);
                        Transition tempAdd = new Transition(tempEnd, end, tempEx);
                        tempEnd.addTransition(tempAdd);
                    } else {
                        tempEx = new Expression("y");
                        Transition tempAdd = new Transition(tempEnd, end, tempEx);
                        tempEnd.addTransition(tempAdd);
                    }

                    return star.getFirstStepTransition();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Transition(" +
                start.getName() +
                " , " + expression.getSequence() +
                " ) = " + end.getName();
    }
}
