package opennlp.ner.controller;

import java.util.ArrayList;
import java.util.List;
import opennlp.ner.model.NameScanCommand;
import opennlp.tools.namefind.DictionaryNameFinder;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Span;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scan/dictionary")
public class NameDictionaryScanController {

  private final DictionaryNameFinder dictionaryNameFinder;
  private final TokenizerME tokenizerME;

  public NameDictionaryScanController(DictionaryNameFinder dictionaryNameFinder, TokenizerME tokenizerME) {
    this.dictionaryNameFinder = dictionaryNameFinder;
    this.tokenizerME = tokenizerME;
  }

  @PostMapping(value = "/name")
  public String scanMovie(@RequestBody NameScanCommand command) {
    String[] tokens = tokenizerME.tokenize(command.getText());

    List<String> result = new ArrayList<>();

    Span[] spans = dictionaryNameFinder.find(tokens);

    for (Span span : spans) {
      result.add(tokens[span.getStart()]);
    }

    return Strings.join(result, ',');
  }
}
