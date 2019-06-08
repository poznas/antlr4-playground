package com.agh.compilers.listener;

import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.repeat;

import com.agh.compilers.antlr4.HTMLParser.HtmlTagNameContext;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.java.Log;

@Log
public class ListListener extends BaseOutputListener {

  private static final Map<String, String> LIST_TAG_MAP = Map.of("ul", "* ", "ol", "1. ");

  private Deque<String> listStates = new LinkedList<>();

  private boolean insideItem = true;

  public ListListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  @Override
  public void exitHtmlTagName(HtmlTagNameContext ctx) {

    String tag = ctx.getText();

    if (tag.matches("^((ul)|(ol))$")) {
      of(tag).map(LIST_TAG_MAP::get)
        .ifPresent(listState -> {
            if (insideItem) {
              listStates.push(listState);
              log.info("open list, states: " + listStates);
            } else {
              listStates.pop();
              log.info("close list, states: " + listStates);
            }
            insideItem = !insideItem;
          }
        );
    } else if ("li".equals(ctx.TAG_NAME().getText())) {
      if (!insideItem) {
        newLineOutput(repeat(" ", listStates.size() * 3) + listStates.peek());
      }
      insideItem = !insideItem;
    }
  }
}
