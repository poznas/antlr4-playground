package com.agh.compilers.listener;

import static com.agh.compilers.listener.TableListener.ALIGN.LEFT;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import com.agh.compilers.antlr4.HTMLParser.HtmlTagNameContext;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
public class TableListener extends AttributeReaderListener {

  private static final Set<String> CELL_TAGS = Set.of("th", "td");
  private static final String PIPE = " | ";

  private static final ALIGN DEFAULT_ALIGN = LEFT;

  private boolean insideTable = false;
  private boolean insideFirstRow = false;
  private boolean insideRow = false;
  private boolean insideCell = false;

  private Deque<ALIGN> alignments = new LinkedList<>();

  public TableListener(Supplier<Boolean> isOutputEnabled, Consumer<String> output) {
    super(isOutputEnabled, output);
  }

  @Override
  public void exitHtmlTagName(HtmlTagNameContext ctx) {

    String tag = ctx.getText();

    if ("table".equals(tag)) {
      if (insideTable) {
        alignments.clear();
        doubleNewLineOutput();
      } else {
        newLineOutput();
      }
      insideTable = !insideTable;

    } else if ("tr".equals(tag)) {

      if (!insideRow) {
        newLineOutput();
      }
      insideRow = !insideRow;

      if (insideFirstRow) {
        String alignSpec = alignments.stream()
          .map(a -> PIPE + a.mdValue).collect(joining());

        newLineOutput(alignSpec);
      }
      insideFirstRow = alignments.isEmpty();

    } else if (CELL_TAGS.contains(tag)) {

      if (!insideCell) {
        if (insideFirstRow) {
          alignments.addLast(DEFAULT_ALIGN);
          log.info("push default align value, alignments: " + alignments);
        }
        output(PIPE);
      }
      insideCell = !insideCell;
    }
  }

  @Override
  String getAttributeName() {
    return "align";
  }

  @Override
  void onAttributeValue(String value) {
    if (insideFirstRow) {
      alignments.pollLast();
      alignments.addLast(ALIGN.of(value));
      log.info(format("found `align` attribute value (%s), alignments: %s", value, alignments));
    }
  }

  @Getter
  @RequiredArgsConstructor
  enum ALIGN {
    LEFT(Set.of("left", "justify", "char"), ":---"),
    CENTER(Set.of("center"), ":---:"),
    RIGHT(Set.of("right"), "---:");

    private final Set<String> htmlValues;
    private final String mdValue;

    static ALIGN of(String htmlValue) {
      return Stream.of(values())
        .filter(align -> align.htmlValues.contains(htmlValue))
        .findFirst().orElse(DEFAULT_ALIGN);
    }
  }
}
