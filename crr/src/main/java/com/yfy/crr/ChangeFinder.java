package com.yfy.crr;

import java.io.File;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
//import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yfy on 10/20/16.
 */
public class ChangeFinder {

  final static Logger logger = LoggerFactory.getLogger(ChangeFinder.class);

  private void getChange() {
    File file1 = new File("file1.java");
    File file2 = new File("file2.java");
    FileDistiller distiller = ChangeDistiller.createFileDistiller(
        ChangeDistiller.Language.JAVA);
    try {
      distiller.extractClassifiedSourceCodeChanges(file1, file2);
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
    if (changes != null) {
      for (SourceCodeChange change : changes) {
        logger.info(change.getChangeType().toString());
        logger.info(change.getLabel());
        logger.info(change.getChangedEntity().toString());
        logger.info(change.toString());
      }
    }
  }

  public static void main(String[] args) {
    //BasicConfigurator.configure();
    ChangeFinder changeFinder = new ChangeFinder();
    changeFinder.getChange();
  }
}
