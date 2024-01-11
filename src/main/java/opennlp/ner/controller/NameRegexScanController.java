package opennlp.ner.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import opennlp.ner.model.NameScanCommand;
import opennlp.tools.namefind.RegexNameFinder;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Span;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regex")
public class NameRegexScanController {

  private final RegexNameFinder regexNameFinder;
  private final TokenizerME tokenizerME;

  public NameRegexScanController(RegexNameFinder regexNameFinder, TokenizerME tokenizerME) {
    this.regexNameFinder = regexNameFinder;
    this.tokenizerME = tokenizerME;
  }

  @PostMapping(value = "/name")
  public String scanName(@RequestBody NameScanCommand command) {
    String[] tokens = tokenizerME.tokenize(command.getText());

    Set<String> result = new HashSet<>();

    Span[] spans = regexNameFinder.find(tokens);

    for (Span span : spans) {
      result.add(tokens[span.getStart()]);
    }

    return Strings.join(result, ',');
  }
}
