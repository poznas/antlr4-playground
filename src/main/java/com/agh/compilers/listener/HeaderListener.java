package com.agh.compilers.listener;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.repeat;

import com.agh.compilers.antlr4.HTMLParser.HtmlTagNameContext;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.java.Log;

@Log
public class HeaderListener extends BaseOutputListener {

  private boolean insideHeader = false;

  public HeaderListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  @Override
  public void exitHtmlTagName(HtmlTagNameContext ctx) {

    ofNullable(ctx.TAG_NAME().getText())
      .filter(tag -> tag.matches("h[1-6]"))
      .map(tag -> tag.charAt(1))
      .map(Character::getNumericValue)
      .map(level -> repeat('#', level) + " ")
      .ifPresent(result -> {
        if (!insideHeader) {
          newLineOutput(result);
        } else {
          newLineOutput();
        }
        insideHeader = !insideHeader;
      });
  }
}
