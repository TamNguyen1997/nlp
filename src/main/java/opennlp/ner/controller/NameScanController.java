package opennlp.ner.controller;

import java.util.HashSet;
import java.util.Set;
import opennlp.ner.model.NameScanCommand;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Span;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/default")
public class NameScanController {

  private final NameFinderME nameFinderME;
  private final TokenizerME tokenizerME;

  public NameScanController(NameFinderME nameFinderME, TokenizerME tokenizerME) {
    this.nameFinderME = nameFinderME;
    this.tokenizerME = tokenizerME;
  }

  @PostMapping(value = "/name")
  public String scanName(@RequestBody NameScanCommand command) {
    String[] tokens = tokenizerME.tokenize(command.getText());

    Set<String> result = new HashSet<>();

    Span[] spans = nameFinderME.find(tokens);

    for (Span span : spans) {
      result.add(tokens[span.getStart()]);
    }

    return Strings.join(result, ',');
  }
}
