/**
 * Copyright (c) Acroquest Technology Co, Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE SOFTWARE IS PROVIDED BY Acroquest Technology Co., Ltd.,
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

import java.io.File;

// 対象のファイルにRootフォルダからのパッケージを追加する。
boolean isExecute = checkArgs(args);
if (!isExecute) {
    println "処理を終了します。";
    return;
}

/**
 * 引数チェックを行う。
 *
 */
def checkArgs(String[] values) {
    String commandLineMessage = "必要な引数: targetPath(file or directory) outputPath(directory)";
    // 一つもなければNG
    if (values.size() <= 1) {
        println "引数が足りません。";
        println commandLineMessage;
        return false;
    }

    // 第一引数のファイルもしくはフォルダがあるか確認する。
    String inputPath = values[0];
    File file = new File(inputPath);
    if(file.exists() == false) {
        println "変換先のパスにファイルもしくはフォルダがありません。path=" + file.getAbsolutePath();
        println commandLineMessage;
        return false;
    }

    // 出力ファイル先にすでにファイルがある場合は処理を中断する。
    File outputFolder = new File(values[1]);
    if (outputFolder.exists()) {
        if (!outputFolder.isDirectory()) {
            println "出力先がフォルダではありません。path=" + outputFolder.getAbsolutePath();
            println commandLineMessage;
            return false;
        }
    }

    // java ファイルがあるかチェックする。
    if (file.isDirectory()) {
        File[] javaFiles = FileUtil.getFileWithSubFolder(file, new JavaFileFilter());
        if (javaFiles == null || javaFiles.length == 0) {
            println "変換対象のjavaファイルがありません。";
            println commandLineMessage;
            return false;
        }
    } else {
        if (!file.name.endsWith(".java")) {
            println "変換対象のファイルはjavaファイルではありません。";
            println commandLineMessage;
            return false;
        }
    }
    return true;
}

// 変換対象のファイルを指定する。
File target = new File(args[0]);
File[] targets;
if (target.isDirectory()) {
    targets = FileUtil.getFileWithSubFolder(target, new JavaFileFilter());
} else {
    targets = [target] as File[];
}

// 出力先のフォルダを作成する。
File output = new File(args[1]);
output.mkdirs();
for (File targetFile : targets) {
    permutation(targetFile, output, target);
}
println "処理を終了します。";


/**
 * 対象のファイルのパッケージ指定をフォルダ構成に準じて置換し、出力フォルダに出力する。
 * @param targetPath 置換対象ファイル
 * @param outputPath 出力先ファイルパス
 * @param rootFolder 置換対象ファイルまたはフォルダルート
 */
def permutation(File targetPath, File outputPath, File rootFolder) {
    StringBuilder fileRead = new StringBuilder();
    SeparatorBuilder separatorBuilder = new SeparatorBuilder();
    try {
        InputStreamReader fr = new InputStreamReader(new FileInputStream(targetPath), "UTF-8");
        BufferedReader br = new BufferedReader( fr );
        for (int charCode = br.read(); charCode != -1; charCode = br.read()) {
            fileRead.append((char) charCode);
            separatorBuilder.inputCharCode(charCode);
        }
        br.close();
    } catch ( FileNotFoundException ex ) {
        System.out.println( ex );
    } catch ( IOException ex ) {
        System.out.println( ex );
    }

    // 出力先のパスを取得する
    String rootPath = rootFolder.getAbsolutePath();
    String targetFilePath = targetPath.getAbsolutePath();
    File outputFile;
    String packageName = "";
    if (targetPath.absolutePath.equals(rootFolder.absolutePath)
        || targetPath.getParent().equals(rootFolder.absolutePath))    // 20170317 - ファイルがフォルダルートに置かれた場合
    {
        String fileName = targetPath.getName();
        outputFile = new File(outputPath, fileName);
    }
    else
    {
        String fileName = targetFilePath.substring(rootPath.length() + 1);
        String packagePath = targetFilePath.substring(rootPath.length() + 1, targetFilePath.length() - targetPath.getName().length() - 1);
        packageName = packagePath.replace("\\", ".");
        outputFile = new File(outputPath, fileName);
    }


    // 設定ファイルを読み込み、置換を実施する。
    String output = fileRead.toString();
    String before = "package com\\.example\\.tests;";
    String after = "";
    if (!"".equals(packageName))
    {
        after = "package " + packageName + ";";
    }
    output = output.replaceAll(before, after);

    // 保存する。
    try {

        if (outputFile.exists()) {
            println "出力先にすでにファイルがあるため、上書きします。";
            boolean isDelete = outputFile.delete();
            if (!isDelete) {
                println "上書きできません。保存をスキップします。path:" + outputFile.absolutePath;
                return;
            }
        }
        File parentPath = outputFile.getParentFile();
        parentPath.mkdirs();
        OutputStreamWriter filewriter = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        filewriter.write(output);
        filewriter.close();
        println "置換後のファイルを保存しました。 path:" + outputFile.absolutePath;
    } catch ( IOException ex ) {
        ex.printStackTrace();
    }
}

