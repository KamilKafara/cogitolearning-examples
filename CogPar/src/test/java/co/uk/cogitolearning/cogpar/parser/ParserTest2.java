package co.uk.cogitolearning.cogpar.parser;

import co.uk.cogitolearning.cogpar.lexer.Token;
import co.uk.cogitolearning.cogpar.tree.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest2 {

    @Test
    public void shouldParseAdd2() {
        // Given
        Parser2 parser = new Parser2();

        // When
        ExpressionNode tree = parser.parse(new LinkedList<>(Arrays.asList(
                new Token(Token.NUMBER, "1", 0),
                new Token(Token.PLUS, "+", 1),
                new Token(Token.NUMBER, "2", 2))));

        // Then
        AdditionNodeNew node = new AdditionNodeNew(new ConstantNode(1.0), new ConstantNode(2.0));

        assertThat(tree).isEqualTo(node);
    }

    @Test
    public void shouldParseAdd3() {
        // Given
        Parser2 parser = new Parser2();

        // When
        ExpressionNode tree = parser.parse(new LinkedList<>(Arrays.asList(
                new Token(Token.NUMBER, "1", 0),
                new Token(Token.PLUS, "+", 1),
                new Token(Token.NUMBER, "2", 2),
                new Token(Token.PLUS, "+", 3),
                new Token(Token.NUMBER, "3", 4)
        )));

        // Then
        AdditionNodeNew right = new AdditionNodeNew(new ConstantNode(2.0), new ConstantNode(3.0));
        AdditionNodeNew node = new AdditionNodeNew(new ConstantNode(1.0), right);

        assertThat(tree).isEqualTo(node);
    }

    @Test
    public void shouldParseSub2() {
        // Given
        Parser2 parser = new Parser2();

        // When
        ExpressionNode tree = parser.parse(new LinkedList<>(Arrays.asList(
                new Token(Token.NUMBER, "3", 0),
                new Token(Token.MINUS, "-", 1),
                new Token(Token.NUMBER, "1", 2))));

        // Then
        SubtractionNodeNew node = new SubtractionNodeNew(new ConstantNode(3.0), new ConstantNode(1.0));

        assertThat(tree).isEqualTo(node);
    }

    @Test
    public void shouldParseSub3() {
        // Given
        Parser2 parser = new Parser2();

        // When
        ExpressionNode tree = parser.parse(new LinkedList<>(Arrays.asList(
                new Token(Token.NUMBER, "6", 0),
                new Token(Token.MINUS, "-", 1),
                new Token(Token.NUMBER, "2", 2),
                new Token(Token.MINUS, "-", 3),
                new Token(Token.NUMBER, "3", 4)
        )));

        // Then
        SubtractionNodeNew right = new SubtractionNodeNew(new ConstantNode(2.0), new ConstantNode(3.0));
        SubtractionNodeNew node = new SubtractionNodeNew(new ConstantNode(6.0), right);

        assertThat(tree).isEqualTo(node);
    }

    @Test
    public void shouldParseExtended() {
        // Given
        Parser2 parser = new Parser2();

        // When
        // 6*(3+sin(3.1415/2))^5
        ExpressionNode tree = parser.parse(new LinkedList<>(Arrays.asList(
                new Token(Token.NUMBER, "6", 0),
                new Token(Token.MULT, "*", 1),
                new Token(Token.OPEN_BRACKET, "(", 2),
                new Token(Token.NUMBER, "3", 3),
                new Token(Token.PLUS, "+", 4),
                new Token(Token.FUNCTION, "sin", 5),
                new Token(Token.OPEN_BRACKET, "(", 8),
                new Token(Token.NUMBER, "3.1415", 9),
                new Token(Token.DIV, "/", 15),
                new Token(Token.NUMBER, "2", 16),
                new Token(Token.CLOSE_BRACKET, ")", 17),
                new Token(Token.CLOSE_BRACKET, ")", 18),
                new Token(Token.RAISED, "^", 19),
                new Token(Token.NUMBER, "5", 20)
        )));

        // Then
        MultiplicationNodeNew expected;
        DivNodeNew div = new DivNodeNew(new ConstantNode(3.1415), new ConstantNode(2.0));
        FunctionNode sin = new FunctionNode(FunctionNode.SIN, div);
        AdditionNodeNew base = new AdditionNodeNew(new ConstantNode(3.0), sin);
        ExponentiationNode exp = new ExponentiationNode(base, new ConstantNode(5.0));

        expected = new MultiplicationNodeNew(new ConstantNode(6.0), exp);

        assertThat(tree).isEqualTo(expected);
    }
}
