package com.yfy.crr;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yfy on 9/24/16.
 */
public class GitLogParser {

  private String projectDir;

  private int crFileCount;

  public void parseAll() throws Exception {
//    parse("hadoop");
//    parse("tomcat");
    parse("cassandra");
  }

  private void parse(String project) throws Exception {
    System.out.println(new File("..").getAbsolutePath());
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    projectDir = "../../" + project;
    Repository repo = builder.setGitDir(new File(projectDir + "/.git"))
        .setMustExist(true)
        .build();
    Git git = new Git(repo);
    Iterable<RevCommit> log = git.log().call();
    Iterator<RevCommit> it = log.iterator();

    int count = 0;
    while (it.hasNext()) {
      RevCommit commit = it.next();
      String msg = commit.getFullMessage();
      if (filter(msg)) {
        count++;
        System.out.println(commit.getId());
//        System.out.println(commit.name());
//        System.out.println(msg);
        getModifiedFiles(commit.name());
      }
    }
    Util.log(count);
    Util.log(crFileCount);
  }

  /**
   * Get modified java files of commitId
   * @param commitId
   * @throws Exception
   */
  private void getModifiedFiles(String commitId)
      throws Exception {
    String cmd = "git diff-tree --no-commit-id --name-status -r " + commitId;
    //Execute.exec(cmd, dir, new EchoTask());
    BufferedReader br = Execute.execWithOutput(cmd, projectDir);
    if (br == null) {
      Util.log("[Error] get modified files");
      return;
    }
    String line;
    while ((line = br.readLine()) != null) {
      if (line.length() > 5 && line.charAt(0) == 'M' &&
          line.substring(line.length() - 5).equals(".java")) {
        String filename = line.substring(2);
        Util.log(filename);
        checkoutCommitId(commitId, filename);
      }
    }
  }

  private void checkoutCommitId(String commitId, String filename)
      throws Exception {
    String cmd = "git checkout " + commitId + "^ -- " + filename;
    Execute.exec(cmd, projectDir);
    String fullFilename = projectDir + '/' + filename;
    String content = new String(Files.readAllBytes(Paths.get(fullFilename)));
    List<String> keywords = ConcurrentKeywords.list;
    for (String keyword : keywords) {
      if (content.contains(keyword)) {
        crFileCount++;
        break;
      }
    }
//    cmd = "git checkout " + commitId + " -- " + filename;
//    Execute.exec(cmd, projectDir);
  }

  private boolean filter(String msg) {
    Collection<String> keywords = Arrays.asList(
        "Concurren", "concurren",
        "Synchroniz", "synchroniz",
        "Atomic", "atomic",
        "Lock", " lock");
    for (String keyword : keywords)
      if (msg.contains(keyword)) return true;
    return false;
  }

  public static void main(String[] args) {
    GitLogParser parser = new GitLogParser();
    try {
      parser.parseAll();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
