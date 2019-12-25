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

package co.uk.cogitolearning.cogpar;

import co.uk.cogitolearning.cogpar.tree.*;

/**
 * A visitor that sets a variable with a specific name to a given value
 */
public class VariableSetterVisitor implements ExpressionNodeVisitor<Void> {

    private String name;
    private double value;

    /**
     * Construct the visitor with the name and the value of the variable to set
     *
     * @param name  the name of the variable
     * @param value the value of the variable
     */
    VariableSetterVisitor(String name, double value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * Checks the nodes name against the name to set and sets the value if the two
     * strings match
     */
    @Override
    public Void visit(VariableNode node) {
        if (node.getName().equals(name))
            node.setValue(value);
        return null;
    }

    /**
     * Do nothing
     */
    @Override
    public Void visit(ConstantNode node) {
        return null;
    }

    /**
     * Do nothing
     */
    @Override
    public Void visit(ExponentiationNode node) {
        return null;
    }

    /**
     * Do nothing
     */
    @Override
    public Void visit(FunctionNode node) {
        return null;
    }

    @Override
    public Void visit(AdditionNodeNew node) {
        return null;
    }

    @Override
    public Void visit(SubtractionNodeNew node) {
        return null;
    }

    @Override
    public Void visit(MultiplicationNodeNew node) {
        return null;
    }

    @Override
    public Void visit(DivNodeNew node) {
        return null;
    }

}
