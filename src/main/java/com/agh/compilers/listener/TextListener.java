package com.agh.compilers.listener;

import com.agh.compilers.antlr4.HTMLParser.HtmlChardataContext;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextListener extends BaseOutputListener {

  public TextListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  @Override
  public void exitHtmlChardata(HtmlChardataContext ctx) {
    output(ctx.getText());
  }
}
