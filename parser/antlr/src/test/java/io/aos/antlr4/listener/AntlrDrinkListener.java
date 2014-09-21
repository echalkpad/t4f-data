package io.aos.antlr4.listener;

import io.aos.antlr4.drink.DrinkBaseListener;
import io.aos.antlr4.drink.DrinkParser.DrinkContext;

public class AntlrDrinkListener extends DrinkBaseListener {

    @Override
    public void enterDrink(DrinkContext ctx) {
        System.out.println(ctx.getText());
    }

}
