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


import java.io.File
import java.io.FilenameFilter;

/**
 * Javaファイルのフィルタリングのためのクラス。
 *
 */
class JavaFileFilter implements FilenameFilter {

    /**
     * 
     */
    public JavaFileFilter() {
    }

    /* (non-Javadoc)
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    @Override
    public boolean accept(File dir, String name) {
        boolean let = name.endsWith(".java");
        return let;
    }
}
