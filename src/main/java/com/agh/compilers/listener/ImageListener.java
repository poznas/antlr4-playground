package com.agh.compilers.listener;

import static java.lang.String.format;

import com.agh.compilers.antlr4.HTMLParser.HtmlTagNameContext;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ImageListener extends AttributeReaderListener {

  private boolean insideImage = false;

  public ImageListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  @Override
  public void exitHtmlTagName(HtmlTagNameContext ctx) {
    if ("img".equals(ctx.getText())) {
      insideImage = true;
    }
  }

  @Override
  String getAttributeName() {
    return "src";
  }

  @Override
  void onAttributeValue(String value) {
    if (insideImage) {
      output(format("![](%s) ", value));
    }
  }

}
