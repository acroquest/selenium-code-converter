# ----------------------------------------------------------------------------
# Copyright (c) Acroquest Technology Co, Ltd. All Rights Reserved.
# Please read the associated COPYRIGHTS file for more details.
# 
# THE SOFTWARE IS PROVIDED BY Acroquest Technology Co., Ltd.,
# WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
# BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
# CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
# OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
# ----------------------------------------------------------------------------

# ============================================================================
# seleniumCodeBuilder による変換処理を実行します。
# ============================================================================

# --- groovy スクリプトの格納ディレクトリに変更します。
cd $(dirname $0)
cd src/main/groovy
echo ------------------------------------------
echo --- 置換処理を実施します...
echo ------------------------------------------
java -cp .:../../../lib/* groovy.ui.GroovyMain SeleniumCodeBuilder.groovy ../../../input ../../../output ../../../setting.yaml

echo ------------------------------------------
echo --- パッケージ階層を設定します...
echo ------------------------------------------
java -cp .:../../../lib/* groovy.ui.GroovyMain PackageModifier.groovy ../../../output ../../../output

# --- 正常終了します。
exit 0

