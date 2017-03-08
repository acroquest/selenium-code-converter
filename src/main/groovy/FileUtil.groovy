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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ファイルシステム関連のユーティリティクラス。
 *
 */
public class FileUtil {

    /**
     * 
     */
    private FileUtil() {
        // Do Nothing.
    }

    public static File[] getFileWithSubFolder(File rootFolder, FilenameFilter filter) {
        List<File> subFileList = new ArrayList<File>();

        File[] fileArray = rootFolder.listFiles(filter);
        subFileList.addAll(Arrays.asList(fileArray));
        File[] subFolderList = rootFolder.listFiles();
        for (File folder : subFolderList) {
            if (folder.isDirectory()) {
                File[] subFileArray = getFileWithSubFolder(folder, filter);
                subFileList.addAll(Arrays.asList(subFileArray));
            }
        }
        return subFileList.toArray(new File[0]);
    }
}
