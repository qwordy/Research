package com.yfy.crr;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yfy on 9/24/16.
 */
public class GitLogParser {

  //private Db db;

  private String project, projectDir;

  private int fileCount, relatedCommitCount;

  private PrintWriter pw, pw2;

  private TaskType taskType;

  private Map<String, Integer> rq3map, rq3map2;

  private RQ3 rq3;

  private RevCommit commit;

  public GitLogParser() throws Exception {
    //db = new Db();
    //pw = new PrintWriter("../svm2/test");
    //pw2 = new PrintWriter("../svm/commitId");
    rq3map = new HashMap<>();
    rq3map2 = new HashMap<>();
    rq3 = new RQ3();
  }

  public GitLogParser setTaskType(TaskType taskType) {
    this.taskType = taskType;
    return this;
  }

  public void parseAll() throws Exception {
    parse("hadoop"); // 21m 35m
    parse("flink"); // 18m
    parse("tomcat"); // 5m
    //parse("mahout"); // 4m
    parse("cassandra"); // 9m
    parse("lucene-solr"); // 40m 1h
    parse("netty"); // 4m 9m
    //parse("guava"); // 2m 4m
    //p w.close();
    //pw2.close();

//    for (String key : rq3map.keySet())
//      Util.log(key + " " + rq3map.get(key));
//    Util.log("---");
//    for (String key : rq3map2.keySet())
//      Util.log(key + " " + rq3map2.get(key));

    rq3.print();
  }

  private void parse(String project) throws Exception {
    relatedCommitCount = 0;
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

    RQ2ChangeByTime rq2 = new RQ2ChangeByTime(project);
    int commitCount = 0;
    while (it.hasNext()) {
      RevCommit commit = it.next();
      this.commit = commit;
      commitCount++;
      //Util.log(commit.getFullMessage());
      Feature feature = feature(commit.name());
      if (taskType == TaskType.ChangeByTime)
        rq2.deal(commit, feature);
    }
    if (taskType == TaskType.ChangeByTime)
      rq2.finish();
//    Util.log("Project:             " + project);
//    Util.log("Commit count:        " + commitCount);
//    Util.log("Related commit count " + relatedCommitCount);
//    Util.log("Modified file count: " + fileCount);
//    Util.log("Selected file count: " + crFileCount);
    //db.commit();
  }

  // Get commit feature
  private Feature feature(String commitId) throws Exception {
    String cmd = "git show " + commitId;
    BufferedReader br = Execute.execWithOutput(cmd, projectDir);
    List<String> lines = br.lines().collect(Collectors.toList());

    Feature f = new Feature();
    textFeature(lines, f);
    codeFeature(lines, f);

    if (f.related())
      relatedCommitCount++;
//    if (f.related()) {
//      relatedCommitCount++;
//      fileCount++;
      //pw.println("1 " + f.toStr());
//      pw2.println(project + ' ' + commitId);
//      writeDiff(lines, Config.projectsDir + "/diff2", fileCount + "_" +
//          project + '_' + commitId + ".diff");
//    }
    return f;
  }

  public void textFeature(List<String> lines, Feature feature) {
    int i, j;
    for (i = 0; i < lines.size(); i++)
      if (lines.get(i).startsWith("diff --")) break;
    String message = "";
    for (j = 3; j < i; j++)
      message += lines.get(j) + '\n';

    String[] words = message.split("\\b");
    int keyNum = 0;
    for (String word : words) {
      for (String key : ConcurrentKeywords.list)
        if (word.equals(key)) keyNum++;
    }
    feature.msgKey = keyNum;
    if (keyNum > 0) {
      //Util.log(keyNum);
//      Util.log(commitId);
//      Util.log(message);
    }
  }

  public void codeFeature(List<String> lines, Feature feature) throws Exception {
    int fileNum = 0, hunkNum = 0, lineAdd = 0, lineRemove = 0,
        keyAdd = 0, keyRemove = 0, contextKey = 0;
    boolean isJava = false, isHunk = false;
    for (String line : lines) {
      if (line.startsWith("diff --git ")) {
        isJava = line.endsWith(".java");
        if (isJava) fileNum++;
        isHunk = false;
      } else if (isJava) {
        if (line.startsWith("@@ -")) {
          hunkNum++;
          isHunk = true;
        }
        if (isHunk) {
          String[] words;
          if (line.length() == 0) continue;
          words = line.substring(1, line.length()).split("\\b|\\s");
          if (isIgnore(words)) continue;
          //Util.log(words.length);
          int keyNum = 0;
          for (String word : words) {
            for (String key : ConcurrentKeywords.list)
              if (word.equals(key)) keyNum++;
            // rq3
            for (String key : ConcurrentKeywords.classList)
              if (word.equals(key)) {
                if (line.charAt(0) == '+')
                  rq3.add(key, true, commit);
                  //rq3map.put(key, rq3map.getOrDefault(key, 0) + 1);
                else
                  rq3.add(key, false, commit);
                  //rq3map2.put(key, rq3map2.getOrDefault(key, 0) + 1);
              }
          }
          if (line.charAt(0) == '+') {
            lineAdd++;
            keyAdd += keyNum;
          }
          else if (line.charAt(0) == '-') {
            lineRemove++;
            keyRemove += keyNum;
          } else {
            contextKey += keyNum;
          }
        }
      }
    }
    int lineSub = Math.abs(lineAdd - lineRemove);
    int lineSum = lineAdd + lineRemove;
    int keySub = Math.abs(keyAdd - keyRemove);
    int keySum = keyAdd + keyRemove;
//    System.out.printf("fileNum:%d hunkNum:%d lineAdd:%d lineRemove:%d keyAdd:%d keyRemove:%d contextKey: %d\n",
//        fileNum, hunkNum, lineAdd, lineRemove, keyAdd, keyRemove, contextKey);
    feature.file = fileNum;
    feature.hunk = hunkNum;
    feature.lineAdd = lineAdd;
    feature.lineRemove = lineRemove;
    feature.lineSub = lineSub;
    feature.lineSum = lineSum;
    feature.keyAdd = keyAdd;
    feature.keyRemove = keyRemove;
    feature.keySub = keySub;
    feature.keySum = keySum;
    feature.contextKey = contextKey;
  }

  private boolean isIgnore(String[] words) {
    for (String word : words) {
      if (isEmpty(word)) continue;
      return word.equals("//") || word.equals("*") || word.equals("import");
    }
    return false;
  }

  private boolean isEmpty(String word) {
    for (int i = 0; i < word.length(); i++)
      if (word.charAt(i) != ' ' || word.charAt(i) != '\t')
        return false;
    return true;
  }

  private void writeDiff(List<String> lines, String dir, String filename)
      throws Exception {
    File dirFile = new File(dir);
    if (!dirFile.exists()) dirFile.mkdirs();
    PrintWriter pw = new PrintWriter(new File(dirFile, filename));
    lines.forEach(pw::println);
    pw.close();
  }

  /*private void showDiff(String commitId) throws Exception {
    String cmd = "git show " + commitId;
    BufferedReader br = Execute.execWithOutput(cmd, projectDir);
    List<String> lines = br.lines().collect(Collectors.toList());
    // single keyword
    for (String keyword : ConcurrentKeywords.list) {
      for (String line : lines) {
        if ((line.startsWith("+") || line.startsWith("-")) &&
            line.contains(keyword)) {
//          writeDiff(lines, Config.projectsDir + "/diff/single/" + keyword,
//              project + '_' + commitId);
          break;
        }
      }
    }
    // double keyword
    boolean find1, find2, isJava;
    List<String> list = ConcurrentKeywords.list;
    for (int i = 0; i < list.size() - 1; i++) {
      for (int j = i + 1; j < list.size(); j++) {
        String keyword1 = list.get(i);
        String keyword2 = list.get(j);
        find1 = find2 = isJava = false;
        for (String line : lines) {
          if (line.startsWith("diff --git ")) {
            isJava = line.endsWith(".java");
            find1 = find2 = false;
          }
          if (isJava && (line.startsWith("+") || line.startsWith("-"))) {
            if (line.contains(keyword1)) find1 = true;
            if (line.contains(keyword2)) find2 = true;
            if (find1 && find2) {
//              writeDiff(lines, Config.projectsDir + "/diff/double/" + keyword1 +
//                  '_' + keyword2, project + '_' + commitId);
              break;
            }
          }
        }
      }
    }
  }

  *//**
   * Get modified java files of commitId
   *
   * @param commitId
   * @throws Exception
   *//*
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
        //db.addPairs(project, content1, content2);
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
  }*/

}
