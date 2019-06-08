package com.agh.compilers.listener;

import com.agh.compilers.antlr4.HTMLParser.HtmlAttributeNameContext;
import com.agh.compilers.antlr4.HTMLParser.HtmlAttributeValueContext;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AttributeReaderListener extends BaseOutputListener {

  private boolean readAttributeValue = false;

  AttributeReaderListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  abstract String getAttributeName();

  abstract void onAttributeValue(String value);

  @Override
  public void exitHtmlAttributeName(HtmlAttributeNameContext ctx) {
    if (!readAttributeValue && getAttributeName().equals(ctx.getText())) {
      readAttributeValue = true;
    }
  }

  @Override
  public void exitHtmlAttributeValue(HtmlAttributeValueContext ctx) {
    if(readAttributeValue) {
      onAttributeValue(ctx.getText().replaceAll("(^\")|(\"$)", ""));
      readAttributeValue = false;
    }
  }
}
