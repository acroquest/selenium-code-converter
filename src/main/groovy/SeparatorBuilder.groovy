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

/**
 * 改行コードを生成するためのクラス。
 *
 */
class SeparatorBuilder {


    /** carriage return(0x0D(13)). */
    private static final char CR = '\r';

    /** line feed code(0x0A(10)). */
    private static final char LF = '\n';

    /** 改行の種類を保持する。 */
    private Separator separator = null;

    /**
     * デフォルトコンストラクタ
     */
    public SeparatorBuilder() {
    }

    public void inputCharCode(int charCode) {
        if (this.separator == Separator.CR_LF) {
            return;
        }
        if (charCode == CR ) {
            if (this.separator == null) {
                this.separator = Separator.CARRIGE_RETURN;
            }
            else if (this.separator == Separator.LINE_FEED) {
                this.separator = Separator.CR_LF;
            }
        }
        else if (charCode == LF) {
            if (this.separator == null) {
                this.separator = Separator.LINE_FEED;
            }
            else if (this.separator == Separator.CARRIGE_RETURN) {
                this.separator = Separator.CR_LF;
            }
        }
    }

    /**
     * 改行コードを取得する。
     * @return 改行コード
     */
    public String getSeparatorCode () {
        if (this.separator == Separator.CR_LF) {
            return CR + LF;
        }
        else if (this.separator == Separator.CARRIGE_RETURN) {
            return CR;
        }
        else if (this.separator == Separator.LINE_FEED) {
            return LF;
        }
    }

    private enum Separator {
        /** CR */
        CARRIGE_RETURN,

        /** LF */
        LINE_FEED,

        /** CR+LF */
        CR_LF
    }
}
