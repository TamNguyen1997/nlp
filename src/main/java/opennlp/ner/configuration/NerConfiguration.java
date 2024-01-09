package opennlp.ner.configuration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.namefind.DictionaryNameFinder;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.RegexNameFinder;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenizerFactory;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.lang.en.TokenSampleStream;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.StringList;
import opennlp.tools.util.TrainingParameters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class NerConfiguration {

  @Bean
  public TokenNameFinderModel tokenNameFinderModel() throws IOException {
    File file = ResourceUtils.getFile("classpath:model/en-ner-person.bin");
    InputStream inputStream = new FileInputStream(file);
    return new TokenNameFinderModel(inputStream);
  }

  @Bean
  public TokenizerModel tokenizerModel() throws IOException {
    File file = ResourceUtils.getFile("classpath:model/en-token.bin");
    InputStream inputStream = new FileInputStream(file);
    return new TokenizerModel(inputStream);
  }

  @Bean
  public NameFinderME nameFinderME(TokenNameFinderModel tokenNameFinderModel) {
    return new NameFinderME(tokenNameFinderModel);
  }

  @Bean
  public TokenizerME tokenizerME(TokenizerModel tokenizerModel) {
    return new TokenizerME(tokenizerModel);
  }

  @Bean
  public DictionaryNameFinder dictionaryNameFinder() {
    Dictionary dictionary = new Dictionary();
    dictionary.put(new StringList("Holmes"));
    dictionary.put(new StringList("Mario"));
    dictionary.put(new StringList("Sherlock"));
    return new DictionaryNameFinder(dictionary, "Name");
  }

  @Bean
  public RegexNameFinder regexNameFinder() {
    Pattern pattern = Pattern.compile("[A-Z]([a-z]+|\\.)(?:\\s+[A-Z]([a-z]+|\\.))*(?:\\s+[a-z][a-z\\-]+){0,2}\\s+[A-Z]([a-z]+|\\.)");
    return new RegexNameFinder(new Pattern[]{pattern}, "Name");
  }

  @Bean
  public NameFinderME orgNameFinderME() throws IOException {
    File file = ResourceUtils.getFile("classpath:train/custom-ner-organization.train");

    ObjectStream<String> lineStream =
        new PlainTextByLineStream(new MarkableFileInputStreamFactory(file), StandardCharsets.UTF_8);

    TokenNameFinderModel model;

    try (ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream)) {
      model =
          NameFinderME.train(
              "eng",
              "organization",
              sampleStream,
              TrainingParameters.defaultParams(),
              new TokenNameFinderFactory());
    }

    try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream("./output.bin"))) {
      model.serialize(modelOut);
    }
    return new NameFinderME(model);
  }
}
