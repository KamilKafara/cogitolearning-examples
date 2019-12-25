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

package co.uk.cogitolearning.cogpar.tree;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A base class for AdditionExpressionNode and MultiplicationExpressionNode.
 * <p>
 * Holds an arbitrary number of ExpressionNodes together with boolean flags.
 */
@ToString
@EqualsAndHashCode
public abstract class SequenceNode implements ExpressionNode {
    @Override
    public Iterator<ExpressionNode> iterator() {
        return null;
    }

    /**
     * An inner class that defines a pair containing an ExpressionNode and a
     * boolean flag.
     */
    @ToString
    @EqualsAndHashCode
    public static class Term {
        /**
         * the boolean flag
         */
        public boolean positive;
        /**
         * the expression node
         */
        public ExpressionNode expression;

        /**
         * Construct the Term object with some values.
         *
         * @param positive   the boolean flag
         * @param expression the expression node
         */
        public Term(boolean positive, ExpressionNode expression) {
            super();
            this.positive = positive;
            this.expression = expression;
        }
    }

    /**
     * the list of terms in the sequence
     */
    public final ArrayList<Term> terms; //FIXME: Exposing internal representation.

    /**
     * Default constructor.
     */
    public SequenceNode() {
        this.terms = new ArrayList<>();
    }

    /**
     * Constructor to create a sequence with the first term already added.
     *
     * @param node     the term to be added
     * @param positive a boolean flag
     */
    public SequenceNode(ExpressionNode node, boolean positive) {
        this.terms = new ArrayList<>();
        this.terms.add(new Term(positive, node));
    }

    /**
     * Add another term to the sequence
     *
     * @param node     the term to be added
     * @param positive a boolean flag
     */
    public void add(ExpressionNode node, boolean positive) {
        this.terms.add(new Term(positive, node));
    }

}
