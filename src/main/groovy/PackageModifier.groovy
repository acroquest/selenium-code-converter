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

// �Ώۂ̃t�@�C����Root�t�H���_����̃p�b�P�[�W��ǉ�����B
boolean isExecute = checkArgs(args);
if (!isExecute) {
    println "�������I�����܂��B";
    return;
}

/**
 * �����`�F�b�N���s���B
 *
 */
def checkArgs(String[] values) {
    String commandLineMessage = "�g�p�@: groovy -e targetPath(file or directory) outputPath(directory) settingPath";
    // ����Ȃ����NG
    if (values.size() <= 1) {
        println "����������܂���B";
        println commandLineMessage;
        return false;
    }

    // �������̃t�@�C���������̓t�H���_�����邩�m�F����B
    String inputPath = values[0];
    File file = new File(inputPath);
    if(file.exists() == false) {
        println "�ϊ���̃p�X�Ƀt�@�C���������̓t�H���_������܂���Bpath=" + file.getAbsolutePath();
        println commandLineMessage;
        return false;
    }

    // �o�̓t�@�C����ɂ��łɃt�@�C��������ꍇ�͏����𒆒f����B
    File outputFolder = new File(values[1]);
    if (outputFolder.exists()) {
        if (!outputFolder.isDirectory()) {
            println "�o�͐悪�t�H���_�ł͂���܂���Bpath=" + outputFolder.getAbsolutePath();
            println commandLineMessage;
            return false;
        }
    }

    // java �t�@�C�������邩�`�F�b�N����B
    if (file.isDirectory()) {
        File[] javaFiles = FileUtil.getFileWithSubFolder(file, new JavaFileFilter());
        if (javaFiles == null || javaFiles.length == 0) {
            println "�ϊ��Ώۂ�java�t�@�C��������܂���B";
            println commandLineMessage;
            return false;
        }
    } else {
        if (!file.name.endsWith(".java")) {
            println "�ϊ��Ώۂ̃t�@�C����java�t�@�C���ł͂���܂���B";
            println commandLineMessage;
            return false;
        }
    }
    return true;
}

// �ϊ���̃t�@�C�����w�肷��B
File target = new File(args[0]);
File[] targets;
if (target.isDirectory()) {
    targets = FileUtil.getFileWithSubFolder(target, new JavaFileFilter());
} else {
    targets = [target] as File[];
}

// �o�͐�̃t�H���_���쐬����B
File output = new File(args[1]);
output.mkdirs();
for (File targetFile : targets) {
    permutation(targetFile, output, target);
}
println "�������I�����܂��B";


/**
 * �Ώۂ̃t�@�C����u�����A�o�̓t�H���_�ɏo�͂���B
 * @param file �u���t�@�C��
 * @param outputPath �o�͐�t�@�C���p�X
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

    // �o�͐�̃p�X���擾����
    String rootPath = rootFolder.getAbsolutePath();
    String targetFilePath = targetPath.getAbsolutePath();
    File outputFile;
    String packageName = "";
    if (targetPath.absolutePath.equals(rootFolder.absolutePath))
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


    // �ݒ�t�@�C����ǂݍ��݁A�u�������{����B
    String output = fileRead.toString();
    String before = "package com\\.example\\.tests;";
    String after = "";
    if (!"".equals(packageName))
    {
        after = "package " + packageName + ";";
    }
    output = output.replaceAll(before, after);

    // �ۑ�����B
    try {

        if (outputFile.exists()) {
            println "�o�͐�ɂ��łɃt�@�C�������邽�߁A�㏑�����܂��B";
            boolean isDelete = outputFile.delete();
            if (!isDelete) {
                println "�㏑���ł��܂���B�ۑ����X�L�b�v���܂��Bpath:" + outputFile.absolutePath;
                return;
            }
        }
        File parentPath = outputFile.getParentFile();
        parentPath.mkdirs();
        OutputStreamWriter filewriter = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
        filewriter.write(output);
        filewriter.close();
        println "�u����̃t�@�C����ۑ����܂����B path:" + outputFile.absolutePath;
    } catch ( IOException ex ) {
        ex.printStackTrace();
    }
}

