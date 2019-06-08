package com.agh.compilers.listener;

import static java.lang.String.format;

import com.agh.compilers.antlr4.HTMLParserBaseListener;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class BaseOutputListener extends HTMLParserBaseListener {

  private final Supplier<Boolean> isOutputEnabled;
  private final Consumer<String> output;

  void newLineOutput(String string) {
    output("\n" + string);
  }

  void output(String string) {
    if (isOutputEnabled.get()) {
      log.info(() -> format("%s::output - %s", this.getClass().getName(), string));
      output.accept(string);
    }
  }
}
