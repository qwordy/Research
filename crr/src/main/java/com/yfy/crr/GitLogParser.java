package com.yfy.crr;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yfy on 9/24/16.
 */
public class GitLogParser {

  private Db db;

  private String project, projectDir;

  private int crFileCount, fileCount;

  public GitLogParser() throws Exception {
    db = new Db();
  }

  public void parseAll() throws Exception {
    parse("hadoop"); // 21m 35m
    parse("flink"); // 18m
    parse("tomcat"); // 5m
    parse("mahout"); // 4m
    parse("cassandra"); // 9m
    parse("lucene-solr"); // 40m 1h
    parse("netty"); // 4m 9m
    parse("guava"); // 2m 4m
  }

  private void parse(String project) throws Exception {
    Util.log(project);
    this.project = project;
    //db.createTable(project);

    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    projectDir = Config.projectsDir + '/' + project;
    Repository repo = builder.setGitDir(new File(projectDir + "/.git"))
        .setMustExist(true)
        .build();
    Git git = new Git(repo);
    Iterable<RevCommit> log = git.log().call();
    Iterator<RevCommit> it = log.iterator();

    int commitCount = 0;
    crFileCount = fileCount = 0;
    while (it.hasNext()) {
      RevCommit commit = it.next();
      String msg = commit.getFullMessage();
      if (filter(msg)) {
        commitCount++;
//        if (commitCount == 300) break;
//        System.out.println(commit.getId());
//        System.out.println(commit.name());
//        System.out.println(msg);
        //getModifiedFiles(commit.name());
        showDiff(commit.name());
      }
    }
    Util.log("Project:             " + project);
    Util.log("Commit count:        " + commitCount);
    Util.log("Modified file count: " + fileCount);
    Util.log("Selected file count: " + crFileCount);
    db.commit();
  }

  private void showDiff(String commitId) throws Exception {
    String cmd = "git show " + commitId;
    BufferedReader br = Execute.execWithOutput(cmd, projectDir);
    List<String> lines = br.lines().collect(Collectors.toList());
    // single keyword
    for (String keyword : ConcurrentKeywords.list) {
      for (String line : lines) {
        if ((line.startsWith("+") || line.startsWith("-")) &&
            line.contains(keyword)) {
          writeDiff(lines, Config.projectsDir + "/diff/single/" + keyword,
              project + '_' + commitId);
          break;
        }
      }
    }
    // double keyword
    boolean find1, find2;
    List<String> list = ConcurrentKeywords.list;
    for (int i = 0; i < list.size() - 1; i++) {
      for (int j = i + 1; j < list.size(); j++) {
        String keyword1 = list.get(i);
        String keyword2 = list.get(j);
        find1 = find2 = false;
        for (String line : lines) {
          if (line.startsWith("diff --git ")) {
            
          }
          if (line.startsWith("+") || line.startsWith("-")) {
            if (line.contains(keyword1)) find1 = true;
            if (line.contains(keyword2)) find2 = true;
            if (find1 && find2) {
              writeDiff(lines, Config.projectsDir + "/diff/double/" + keyword1 +
                  '_' + keyword2, project + '_' + commitId);
              break;
            }
          }
        }
      }
    }
  }

  private void writeDiff(List<String> lines, String dir, String filename)
      throws Exception {
    File dirFile = new File(dir);
    if (!dirFile.exists()) dirFile.mkdirs();
    PrintWriter pw = new PrintWriter(new File(dirFile, filename));
    lines.forEach(pw::println);
    pw.close();
  }

  /**
   * Get modified java files of commitId
   *
   * @param commitId
   * @throws Exception
   */
  private void getModifiedFiles(String commitId)
      throws Exception {
    //if (!commitId.equals("813ca77250db29116812bc949e2a466a70f969a3")) return;

    String cmd = "git diff-tree --no-commit-id --name-status -r " + commitId;
    //Util.log(cmd);
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
        //Util.log(filename);
        fileCount++;
        checkoutCommitId(commitId, filename);
      }
    }
  }

  private void checkoutCommitId(String commitId, String filename)
      throws Exception {
    String cmd = "git checkout " + commitId + "^ -- " + filename;
    Execute.execIgnoreOutput(cmd, projectDir);
    String fullFilename = projectDir + '/' + filename;
    //Reader reader1 = new FileReader(fullFilename);
    String content1 = new String(Files.readAllBytes(Paths.get(fullFilename)));

    List<String> keywords = ConcurrentKeywords.list;
    for (String keyword : keywords) {
      if (content1.contains(keyword)) {
        crFileCount++;
        cmd = "git checkout " + commitId + " -- " + filename;
        Execute.execIgnoreOutput(cmd, projectDir);
        fullFilename = projectDir + '/' + filename;
        //Reader reader2 = new FileReader(fullFilename);
        String content2 = new String(
            Files.readAllBytes(Paths.get(fullFilename)));
        //db.addPairs(project, reader1, reader2);
        db.addPairs(project, content1, content2);
        break;
      }
    }
//    cmd = "git checkout " + commitId + " -- " + filename;
//    Execute.exec(cmd, projectDir);
  }

  private boolean filter2(String msg) {
    Collection<String> keywords = Arrays.asList(
        "Concurren", "concurren",
        "Synchroniz", "synchroniz",
        "Atomic", "atomic",
        "Lock", " lock");
    for (String keyword : keywords)
      if (msg.contains(keyword)) return true;
    return false;
  }

  private boolean filter(String msg) {
    return true;
  }

  public static void main(String[] args) {
    try {
      GitLogParser parser = new GitLogParser();
      parser.parseAll();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
