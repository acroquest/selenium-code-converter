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

import org.yaml.snakeyaml.Yaml
// 引数の解析を実施し、実行不可能な引数であれば実行を中断する。
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
    String commandLineMessage = "必要な引数: targetPath(file or directory) outputPath(directory) settingPath";
    // 一つもなければNG
    if (values.size() <= 2) {
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

    // 置換設定ファイルがあるか確認する。
    File settingYamlPath = new File(values[2]);
    if (!settingYamlPath.exists()) {
        println "設定ファイルが存在しません。 path=" + settingYamlPath.getAbsolutePath();
        println commandLineMessage;
        return false;
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

File settingYamlFile = new File(args[2]);

// 出力先のフォルダを作成する。
File output = new File(args[1]);
output.mkdirs();
for (File targetFile : targets) {
    permutation(targetFile, output, settingYamlFile, target);
}
println "処理を終了します。";

/**
 * 対象のファイルを置換し、出力フォルダに出力する。
 * @param targetPath 置換対象ファイル
 * @param outputPath 出力先フォルダパス
 * @param settingPath 置換設定ファイルパス
 * @param rootFolder 置換対象ファイルまたはフォルダルート
 */
def permutation(File targetPath, File outputPath, File settingPath, File rootFolder) {
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

    // 設定ファイルを読み込み、置換を実施する。 20170317 - 設定ファイルをutf8に変更。
    InputStreamReader reader = new InputStreamReader(new FileInputStream(settingPath), "UTF-8");
    Yaml yaml = new Yaml();
    Object object = yaml.load(reader);
    if (!(object instanceof List)) {
        println "設定ファイルの書式が間違っています。";
        return;
    }
    String output = fileRead.toString();
    List settingList = (List) object;
    for (Object setting : settingList) {
        if (!(setting instanceof Map)) {
            continue;
        }
        Map settingMap = (Map)setting;
        String before = settingMap.get("before");
        String after = settingMap.get("after");
        if (before == null || after == null) {
            continue;
        }
        output = output.replaceAll(before, after);
    }

    // 保存する。
    try {
        String rootPath = rootFolder.getAbsolutePath();
        String targetFilePath = targetPath.getAbsolutePath();
        File outputFile;
        if (targetPath.absolutePath.equals(rootFolder.absolutePath))
        {
            String fileName = targetPath.getName();
            outputFile = new File(outputPath, fileName);
        }
        else
        {
            String fileName = targetFilePath.substring(rootPath.length() + 1);
            outputFile = new File(outputPath, fileName);
        }
        // 20170317 - ファイル名の先頭文字を大文字にを小文字に書き換え
        String s = outputFile.getName()
        outputFile = new File(outputFile.getParent(), Character.toUpperCase(s.charAt(0)).toString() + s.substring(1));

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