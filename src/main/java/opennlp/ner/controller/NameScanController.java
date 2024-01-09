package opennlp.ner.controller;

import opennlp.ner.model.NameScanCommand;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Span;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/scan")
public class NameScanController {

  private final NameFinderME nameFinderME;
  private final NameFinderME orgNameFinderME;
  private final TokenizerME tokenizerME;

  public NameScanController(NameFinderME nameFinderME, NameFinderME orgNameFinderME, TokenizerME tokenizerME) {
    this.nameFinderME = nameFinderME;
    this.tokenizerME = tokenizerME;
    this.orgNameFinderME = orgNameFinderME;
  }

  @PostMapping(value = "/name")
  public String scanName(@RequestBody NameScanCommand command) {
    String[] tokens = tokenizerME.tokenize(command.getText());

    List<String> result = new ArrayList<>();

    Span[] spans = nameFinderME.find(tokens);

    for (Span span : spans) {
      result.add(tokens[span.getStart()]);
    }

    return Strings.join(result, ',');
  }

  @PostMapping(value = "/org")
  public String scanOrg(@RequestBody NameScanCommand command) throws AWTException {
    String[] tokens = tokenizerME.tokenize(command.getText());

    List<String> result = new ArrayList<>();

    Span[] spans = orgNameFinderME.find(tokens);

    for (Span span : spans) {
      result.add(tokens[span.getStart()]);
    }
    displayTray();
    return Strings.join(result, ',');
  }

  private void displayTray() throws AWTException {
    SystemTray tray = SystemTray.getSystemTray();

    Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

    TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
    trayIcon.setImageAutoSize(true);
    trayIcon.setToolTip("System tray icon demo");
    tray.add(trayIcon);

    trayIcon.displayMessage("Hello, World", "notification demo", TrayIcon.MessageType.INFO);
  }
}
