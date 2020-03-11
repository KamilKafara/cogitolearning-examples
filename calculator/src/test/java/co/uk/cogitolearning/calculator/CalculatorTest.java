package co.uk.cogitolearning.calculator;

import co.uk.cogitolearning.calculator.tree.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class CalculatorTest {
    @Test
    public void shouldCalculateSimpleOperation() {
        // (2+3)*4
        ConstantNode two = new ConstantNode(2);
        ConstantNode three = new ConstantNode(3);
        ConstantNode four = new ConstantNode(4);

        AdditionNode sum = new AdditionNode(two, three);
        MultiplicationNode multi = new MultiplicationNode(sum, four);

        CalculationVisitor visitor = new CalculationVisitor();
        Assert.assertEquals(20.0, Calculator.calculateTree(multi, visitor), 0.1);
    }

    @Test
    public void shouldCalculateExtendedCalculation() {

        // String exprstr = "6*(3+sin(pi/2))^5"; //=6144
        ConstantNode five = new ConstantNode(6);

        VariableNode pi = new VariableNode("pi");
        DivNode halfPi = new DivNode(pi, new ConstantNode(2));

        FunctionNode sin = new FunctionNode(FunctionNode.SIN, halfPi);
        AdditionNode braces = new AdditionNode(new ConstantNode(3), sin);
        ExponentiationNode exponent = new ExponentiationNode(braces, new ConstantNode(5));
        MultiplicationNode root = new MultiplicationNode(five, exponent);

        CalculationVisitor visitor = new CalculationVisitor();
        visitor.addVariable("pi", Math.PI);
        Assert.assertEquals(6144.0, Calculator.calculateTree(root, visitor), 0.1);
    }

    @Test
    public void shouldParse() {
        // given
        Calculator calculator = new Calculator()
                .withVariable("pi", Math.PI);
        // when
        double value = calculator.calculate("2*(1+sin(pi/2))^2");
        // then
        Assert.assertEquals(8.0, value, 0.1);
    }

    @Test
    public void shouldParseSub() {
        // given
        Calculator calculator = new Calculator();
        // when
        double value = calculator.calculate("3-1");
        // then
        Assert.assertEquals(3.0 - 1.0, value, 0.1);
    }

    @Test
    public void shouldParseSub2() {
        // given
        Calculator calculator = new Calculator();
        // when
        double value = calculator.calculate("4-1-2");
        // then
        Assert.assertEquals(4.0 - 1.0 - 2.0, value, 0.1);
    }

    @Test
    public void shouldParseSub2Sin() {
        // given
        Calculator calculator = new Calculator()
                .withVariable("pi", Math.PI);
        // when
        double value = calculator.calculate("4-sin(pi/2)");
        // then
        Assert.assertEquals(4 - Math.sin(Math.PI / 2), value, 0.1);
    }

    @Test
    public void shouldParseSub3Sin() {
        // given
        Calculator calculator = new Calculator()
                .withVariable("pi", Math.PI);
        // when
        double value = calculator.calculate("4-1-sin(pi/2)");
        // then
        Assert.assertEquals(4 - 1 - Math.sin(Math.PI / 2), value, 0.1);
    }

    @Test
    public void shouldParseSub2Pi() {
        // given
        Calculator calculator = new Calculator()
                .withVariable("pi", Math.PI);
        // when
        double value = calculator.calculate("4-pi");
        // then
        Assert.assertEquals(4 - Math.PI, value, 0.1);
    }

    @Test
    public void shouldParseMul2Pi() {
        // given
        Calculator calculator = new Calculator()
                .withVariable("pi", Math.PI);
        // when
        double value = calculator.calculate("4-pi");
        // then
        Assert.assertEquals(4 - Math.PI, value, 0.1);
    }

    @Test
    public void shouldParseSub3Pi() {
        // given
        Calculator calculator = new Calculator()
                .withVariable("pi", Math.PI);
        // when
        double value = calculator.calculate("5-1-pi");
        // then
        Assert.assertEquals(5 - 1 - Math.PI, value, 0.1);
    }

    @Test
    public void shouldParseAdd2() {
        // given
        Calculator calculator = new Calculator();
        // when
        double value = calculator.calculate("1+2+3");
        // then
        Assert.assertEquals(1.0 + 2.0 + 3.0, value, 0.1);
    }

    @Ignore
    @Test
    public void shouldParseRump() {
        // https://books.google.pl/books?id=fZsXBgAAQBAJ&pg=PA179&lpg=PA179&dq=floating+point+error+ibm+format&source=bl&ots=MI5qlHzp51&sig=ACfU3U1lp8FThhWkCmk3erU9ShJGavLl3Q&hl=en&sa=X&ved=2ahUKEwjdm8XH4JLoAhUwSBUIHUxsCiUQ6AEwEHoECAoQAQ#v=onepage&q&f=false
        // x=77617, y=33096
        // given
        Calculator calculator = new Calculator()
                .withVariable("x", 77617)
                .withVariable("y", 33096);
        // when
        double value = calculator.calculate("333.75y^6+x^2(11x^2*y^2-y^6-121y^4-2)+5.5y^8+x/2y");

        // then
        Assert.assertEquals(-0.827, value, 0.1);
    }

}