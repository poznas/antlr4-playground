package com.agh.compilers.listener;

import com.agh.compilers.antlr4.HTMLParser.HtmlTagNameContext;
import com.agh.compilers.antlr4.HTMLParserBaseListener;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class BodyListener extends HTMLParserBaseListener {

  private final Consumer<Boolean> setEnableOutput;
  private boolean enabled;

  @Override
  public void exitHtmlTagName(HtmlTagNameContext ctx) {
    if (ctx.TAG_NAME().getText().equals("body")) {
      enabled = !enabled;
      log.info("string output enabled? : " + enabled);
      setEnableOutput.accept(enabled);
    }
  }

}
