/*
 * This software and all files contained in it are distrubted under the MIT license.
 *
 * Copyright (c) 2013 Cogito Learning Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * @mainpage CogPar is lightweight but versatile parser for mathematical expressions.
 * <p>
 * It can be used to analyse expressions and store them in an internal data structure for later
 * evaluation. Repeated evaluation of the same expression using CogPar is fast.
 * <p>
 * CogPar comes with a highly configurable tokenizer which can be adapted for your own needs.
 * <p>
 * Arbitrary named variables are supported and values can be assigned in a single line of code.
 * <p>
 * The parser, it's grammar an the tokenizer are well documented. You can read more about the internal
 * workings of CogPar <a href="http://cogitolearning.co.uk/?p=523" alt="CogPar tutorial">in these posts</a>.
 * <p>
 * CogPar is distributed under the MIT license, so feel free to use it in your own projects.
 * <p>
 * To download CogPar, <a href="" alt="Download CogPar">follow this link.</a>
 */

package co.uk.cogitolearning.cogpar.parser;

import co.uk.cogitolearning.cogpar.ParserException;
import co.uk.cogitolearning.cogpar.lexer.Token;
import co.uk.cogitolearning.cogpar.tree.*;

import java.util.Deque;

/**
 * A parser for mathematical expressions. The parser class defines a method
 * parse() which takes a string and returns an ExpressionNode that holds a
 * representation of the expression.
 * <p>
 * Parsing is implemented in the form of a recursive descent parser.
 */
public class Parser {
    /**
     * the tokens to parse
     */
    private Deque<Token> tokens;
    /**
     * the next token
     */
    private Token lookahead;

    /**
     * Parse a mathematical expression in contained in a list of tokens and return
     * an ExpressionNode.
     *
     * @param tokens a list of tokens holding the tokenized input
     * @return the internal representation of the expression in form of an
     * expression tree made out of ExpressionNode objects
     */
    public ExpressionNode parse(Deque<Token> tokens) {
        // implementing a recursive descent parser
        this.tokens = tokens;
        lookahead = this.tokens.getFirst();

        // top level non-terminal is expression
        ExpressionNode expr = expression();

        if (lookahead.tokenId != Token.EPSILON)
            throw new ParserException("Unexpected symbol %s found", lookahead);

        return expr;
    }

    /**
     * handles the non-terminal expression
     */
    private ExpressionNode expression() {
        // only one rule
        // expression -> signed_term sum_op
        ExpressionNode expr = signedTerm();
        expr = sumOp(expr);
        return expr;
    }

    /**
     * handles the non-terminal sum_op
     */
    private ExpressionNode sumOp(ExpressionNode expr) {
        // sum_op -> PLUSMINUS term sum_op
        if (lookahead.tokenId == Token.PLUS) {
            AdditionNode sum;
            // This means we are actually dealing with a sum
            // If expr is not already a sum, we have to create one
            if (expr.getType() == ExpressionNode.ADDITION_NODE)
                sum = (AdditionNode) expr;
            else
                sum = new AdditionNode(expr, true);

            // reduce the input and recursively call sum_op
            boolean positive = lookahead.sequence.equals("+");
            nextToken();
            ExpressionNode t = term();
            sum.add(t, positive);

            return sumOp(sum);
        }

        if (lookahead.tokenId == Token.MINUS) {
            SubtractionNode sum;
            // This means we are actually dealing with a sum
            // If expr is not already a sum, we have to create one
            if (expr.getType() == ExpressionNode.ADDITION_NODE)
                sum = (SubtractionNode) expr;
            else
                sum = new SubtractionNode(expr, true);

            // reduce the input and recursively call sum_op
            boolean positive = lookahead.sequence.equals("+");
            nextToken();
            ExpressionNode t = term();
            sum.add(t, positive);

            return sumOp(sum);
        }

        // sum_op -> EPSILON
        return expr;
    }

    /**
     * handles the non-terminal signed_term
     */
    private ExpressionNode signedTerm() {
        // signed_term -> PLUSMINUS term
        if (lookahead.tokenId == Token.PLUS) {
            boolean positive = lookahead.sequence.equals("+");
            nextToken();
            ExpressionNode t = term();
            return positive ? t : new AdditionNode(t, false);
        }

        if (lookahead.tokenId == Token.MINUS) {
            boolean positive = lookahead.sequence.equals("+");
            nextToken();
            ExpressionNode t = term();
            return positive ? t : new SubtractionNode(t, false);
        }

        // signed_term -> term
        return term();
    }

    /**
     * handles the non-terminal term
     */
    private ExpressionNode term() {
        // term -> factor term_op
        ExpressionNode f = factor();
        return termOp(f);
    }

    /**
     * handles the non-terminal term_op
     */
    private ExpressionNode termOp(ExpressionNode expression) {
        // term_op -> MULTDIV factor term_op
        if (lookahead.tokenId == Token.MULT) {
            MultiplicationNode prod;

            // This means we are actually dealing with a product
            // If expr is not already a PRODUCT, we have to create one
            if (expression.getType() == ExpressionNode.MULTIPLICATION_NODE)
                prod = (MultiplicationNode) expression;
            else
                prod = new MultiplicationNode(expression, true);

            // reduce the input and recursively call sum_op
            boolean positive = lookahead.sequence.equals("*");
            nextToken();
            ExpressionNode f = signedFactor();
            prod.add(f, positive);

            return termOp(prod);
        }

        if (lookahead.tokenId == Token.DIV) {
            DivNode prod;

            // This means we are actually dealing with a product
            // If expr is not already a PRODUCT, we have to create one
            if (expression.getType() == ExpressionNode.DIVISION_NODE)
                prod = (DivNode) expression;
            else
                prod = new DivNode(expression, true);

            // reduce the input and recursively call sum_op
            boolean positive = lookahead.sequence.equals("*");
            nextToken();
            ExpressionNode f = signedFactor();
            prod.add(f, positive);

            return termOp(prod);
        }

        // term_op -> EPSILON
        return expression;
    }

    /**
     * handles the non-terminal signed_factor
     */
    private ExpressionNode signedFactor() {
        // signed_factor -> PLUSMINUS factor
        if (lookahead.tokenId == Token.PLUS) {
            boolean positive = lookahead.sequence.equals("+");
            nextToken();
            ExpressionNode t = factor();
            return positive ? t : new AdditionNode(t, false);
        }

        if (lookahead.tokenId == Token.MINUS) {
            boolean positive = lookahead.sequence.equals("+");
            nextToken();
            ExpressionNode t = factor();
            return positive ? t : new AdditionNode(t, false);
        }

        // signed_factor -> factor
        return factor();
    }

    /**
     * handles the non-terminal factor
     */
    private ExpressionNode factor() {
        // factor -> argument factor_op
        ExpressionNode a = argument();
        return factorOp(a);
    }


    /**
     * handles the non-terminal factor_op
     */
    private ExpressionNode factorOp(ExpressionNode expr) {
        // factor_op -> RAISED expression
        if (lookahead.tokenId == Token.RAISED) {
            nextToken();
            ExpressionNode exponent = signedFactor();

            return new ExponentiationNode(expr, exponent);
        }

        // factor_op -> EPSILON
        return expr;
    }

    /**
     * handles the non-terminal argument
     */
    private ExpressionNode argument() {
        // argument -> FUNCTION argument
        if (lookahead.tokenId == Token.FUNCTION) {
            int function = FunctionNode.stringToFunction(lookahead.sequence);
            nextToken();
            ExpressionNode expr = argument();
            return new FunctionNode(function, expr);
        }
        // argument -> OPEN_BRACKET sum CLOSE_BRACKET
        else if (lookahead.tokenId == Token.OPEN_BRACKET) {
            nextToken();
            ExpressionNode expr = expression();
            if (lookahead.tokenId != Token.CLOSE_BRACKET)
                throw new ParserException("Closing brackets expected", lookahead);
            nextToken();
            return expr;
        }

        // argument -> value
        return value();
    }

    /**
     * handles the non-terminal value
     */
    private ExpressionNode value() {
        // argument -> NUMBER
        if (lookahead.tokenId == Token.NUMBER) {
            ExpressionNode expr = new ConstantNode(Double.parseDouble(lookahead.sequence));
            nextToken();
            return expr;
        }

        // argument -> VARIABLE
        if (lookahead.tokenId == Token.VARIABLE) {
            ExpressionNode expr = new VariableNode(lookahead.sequence);
            nextToken();
            return expr;
        }

        if (lookahead.tokenId == Token.EPSILON)
            throw new ParserException("Unexpected end of input");
        else
            throw new ParserException("Unexpected symbol %s found", lookahead);
    }

    /**
     * Remove the first token from the list and store the next token in lookahead
     */
    private void nextToken() {
        tokens.pop();
        // at the end of input we return an epsilon token
        lookahead = tokens.isEmpty() ? new Token(Token.EPSILON, "", -1) : tokens.getFirst();
    }
}
