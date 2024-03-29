package com.agh.compilers.listener;

import com.agh.compilers.antlr4.HTMLParser.HtmlTagNameContext;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextStyleListener extends BaseOutputListener {

  public TextStyleListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  @Override
  public void exitHtmlTagName(HtmlTagNameContext ctx) {
    switch (ctx.TAG_NAME().getText()) {
      case "b": case "strong": output("__"); break;
      case "i": case "em": output("*"); break;
      case "s": case "strike": output("~~"); break;
      case "mark": case "code": output("`"); break;
      default:
    }
  }
}
