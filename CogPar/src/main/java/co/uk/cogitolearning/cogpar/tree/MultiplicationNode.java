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

import co.uk.cogitolearning.cogpar.ExpressionNodeIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An ExpressionNode that handles multiplications and divisions. The node can hold
 * an arbitrary number of factors that are either multiplied or divided to the product.
 */
public class MultiplicationNode implements ExpressionNode {
    /**
     * the list of terms in the sequence
     */
    public final List<SequenceNode.Term> terms; //FIXME: Exposing internal representation.

    /**
     * Constructor to create a multiplication with the first term already added.
     *
     * @param node     the term to be added
     * @param positive a flag indicating whether the term is multiplied or divided
     */
    public MultiplicationNode(ExpressionNode node, boolean positive) {
        this.terms = new ArrayList<>();
        this.terms.add(new SequenceNode.Term(positive, node));
    }

    /**
     * Returns the type of the node, in this case ExpressionNode.MULTIPLICATION_NODE
     */
    public int getType() {
        return ExpressionNode.MULTIPLICATION_NODE;
    }

    @Override
    public void accept(ExpressionNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Iterator<ExpressionNode> iterator() {
        return new ExpressionNodeIterator(this);
    }

    /**
     * Add another term to the sequence
     *
     * @param node     the term to be added
     * @param positive a boolean flag
     */
    public void add(ExpressionNode node, boolean positive) {
        this.terms.add(new SequenceNode.Term(positive, node));
    }

}