package com.agh.compilers;

import com.agh.compilers.antlr4.HTMLLexer;
import com.agh.compilers.antlr4.HTMLParser;
import com.agh.compilers.listener.HeaderListener;
import java.io.IOException;
import lombok.extern.java.Log;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

@Log
public class HtmlToMarkdown {

  public static void main(String[] args) throws Exception {
    new HtmlToMarkdown().run();
  }

  private void run() throws IOException {
    var sourceFile = getClass().getClassLoader().getResourceAsStream("inputs/simplest.html");

    assert sourceFile != null;
    var lexer = new HTMLLexer(CharStreams.fromStream(sourceFile));
    var parser = new HTMLParser(new CommonTokenStream(lexer));

    parser.addParseListener(new HeaderListener(log::info, 1));

    var walker = new ParseTreeWalker();

    walker.walk(new HeaderListener(log::info, 2), parser.htmlDocument());
  }

}
