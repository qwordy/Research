package com.yfy.cr;

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

  public void parse(String project) throws Exception {
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    Repository repo = builder.setGitDir(new File("../../" + project + "/.git"))
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
        System.out.println(msg);
      }
    }
    System.out.println(count);
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

}
