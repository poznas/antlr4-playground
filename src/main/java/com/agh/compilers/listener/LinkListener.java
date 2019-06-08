package com.agh.compilers.listener;

import static java.lang.String.format;

import com.agh.compilers.antlr4.HTMLParser.HtmlTagNameContext;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LinkListener extends AttributeReaderListener {

  private boolean insideLink = false;
  private String url = "";

  public LinkListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  @Override
  String getAttributeName() {
    return "href";
  }

  @Override
  void onAttributeValue(String value) {
    url = value;
  }

  @Override
  public void exitHtmlTagName(HtmlTagNameContext ctx) {
    if ("a".equals(ctx.getText())) {
      if (!insideLink) {
        resourceOutput("[");
      } else {
        output(format("](%s) ", url));
        url = "";
      }
      insideLink = !insideLink;
    }
  }
}
