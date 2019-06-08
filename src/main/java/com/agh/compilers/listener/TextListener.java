package com.agh.compilers.listener;

import static java.util.Optional.ofNullable;

import com.agh.compilers.antlr4.HTMLParser.HtmlChardataContext;
import com.agh.compilers.antlr4.HTMLParser.HtmlTagNameContext;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;

public class TextListener extends BaseOutputListener {

  private static final Set<String> NEW_LINE_TARGETS = Set.of("br", "p");

  public TextListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  @Override
  public void exitHtmlChardata(HtmlChardataContext ctx) {
    ofNullable(ctx.getText())
      .filter(StringUtils::isNotBlank)
      .map(s -> s.replaceAll("\\s+$",""))
      .ifPresent(this::output);
  }

  @Override
  public void exitHtmlTagName(HtmlTagNameContext ctx) {
    ofNullable(ctx.TAG_NAME().getText())
      .filter(NEW_LINE_TARGETS::contains).ifPresent(br -> output("\n<br>\n"));
  }
}
