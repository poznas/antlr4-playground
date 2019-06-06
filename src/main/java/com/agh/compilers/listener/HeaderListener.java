package com.agh.compilers.listener;

import com.agh.compilers.antlr4.HTMLParser.HtmlElementContext;
import com.agh.compilers.antlr4.HTMLParserBaseListener;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class HeaderListener extends HTMLParserBaseListener {

  private final Consumer<String> output;
  private final int x;

  @Override
  public void enterHtmlElement(HtmlElementContext ctx) {
    log.info("enter" + x + ctx.getText());
    log.info(x + ctx.htmlTagName().toString());
  }

  @Override
  public void exitHtmlElement(HtmlElementContext ctx) {
    log.info("exit" + x + ctx.getText());
  }
}
