package com.yfy.crr;

import com.yfy.crr.Execute;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by yfy on 9/24/16.
 */
public class GitLogParser {

  public void parseAll() throws Exception {
//    parse("hadoop");
//    parse("tomcat");
    parse("cassandra");
  }

  private void parse(String project) throws Exception {
    System.out.println(new File("..").getAbsolutePath());
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    String projectDir = "../../" + project;
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
        getModifiedFiles(commit.name(), projectDir);
        checkout(commit);
      }
    }
    System.out.println(count);
  }

  private void getModifiedFiles(String commitId, String dir) {
    String cmd = "git diff-tree --no-commit-id --name-status -r " + commitId;
    //Execute.exec(cmd, dir, new EchoTask());
    Execute.exec(cmd, dir, new ParseTask());
  }

  private void checkout(RevCommit commit) {

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
