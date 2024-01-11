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
@RequestMapping("/organisation")
public class OrganisationScanController {

  private final NameFinderME orgNameFinderME;
  private final TokenizerME tokenizerModel;

  public OrganisationScanController(NameFinderME orgNameFinderME, TokenizerME tokenizerModel) {
    this.orgNameFinderME = orgNameFinderME;
    this.tokenizerModel = tokenizerModel;
  }

  @PostMapping(value = "/name")
  public String scanName(@RequestBody NameScanCommand command) {
    String[] tokens = tokenizerModel.tokenize(command.getText());

    Set<String> result = new HashSet<>();

    Span[] spans = orgNameFinderME.find(tokens);

    for (Span span : spans) {
      result.add(tokens[span.getStart()]);
    }

    return Strings.join(result, ',');
  }
}
