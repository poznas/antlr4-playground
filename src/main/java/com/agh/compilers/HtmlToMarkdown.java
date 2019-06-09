package com.agh.compilers;

import com.agh.compilers.antlr4.HTMLLexer;
import com.agh.compilers.antlr4.HTMLParser;
import com.agh.compilers.antlr4.HTMLParserBaseListener;
import com.agh.compilers.listener.BaseOutputListener;
import com.agh.compilers.listener.HeaderListener;
import com.agh.compilers.listener.ImageListener;
import com.agh.compilers.listener.LinkListener;
import com.agh.compilers.listener.ListListener;
import com.agh.compilers.listener.OutputLockController;
import com.agh.compilers.listener.TableListener;
import com.agh.compilers.listener.TextListener;
import com.agh.compilers.listener.TextStyleListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@Log
@Setter
@Getter
public class HtmlToMarkdown {

  private static final Stream<OutputListenerConstructor> listenerConstructors = Stream.of(
    HeaderListener::new, TextStyleListener::new, TextListener::new, ListListener::new,
    LinkListener::new, ImageListener::new, TableListener::new
  );

  private boolean enableOutput = false;


  public static void main(String[] args) throws Exception {
    String outputPath = "output.md";

    if (args.length < 1) {
      throw new IllegalArgumentException("at least source file should be specified");
    } else if (args.length > 2) {
      throw new IllegalArgumentException("max 2 args: sourcePath outputPath");
    } else if (args.length == 2) {
      outputPath = args[1];
    }
    new HtmlToMarkdown().run(args[0], outputPath);
  }

  private void run(String sourcePath, String outputPath) throws IOException {

    var sourceFile = new FileInputStream(sourcePath);

    var lexer = new HTMLLexer(CharStreams.fromStream(sourceFile));
    var parser = new HTMLParser(new CommonTokenStream(lexer));

    parser.addParseListener(new OutputLockController(this::setEnableOutput));

    try (var output = new PrintWriter(new FileWriter(outputPath), true)) {

      listenerConstructors.map(c -> c.apply(this::isEnableOutput, output::print))
        .forEach(parser::addParseListener);

      var walker = new ParseTreeWalker();

      walker.walk(new HTMLParserBaseListener(), parser.htmlDocument());
    }
  }

  interface OutputListenerConstructor extends
    BiFunction<Supplier<Boolean>, Consumer<String>, BaseOutputListener> {

  }

}
