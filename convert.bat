@rem ----------------------------------------------------------------------------
@rem Copyright (c) Acroquest Technology Co, Ltd. All Rights Reserved.
@rem Please read the associated COPYRIGHTS file for more details.
@rem 
@rem THE SOFTWARE IS PROVIDED BY Acroquest Technology Co., Ltd.,
@rem WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
@rem BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
@rem FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
@rem IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
@rem CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
@rem OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
@rem ----------------------------------------------------------------------------

@echo off
@rem ============================================================================
@rem seleniumCodeBuilder による変換処理を実行します。
@rem ============================================================================

@rem --- 環境変数やカレントディレクトリをローカル化します。
setlocal

rem --- groovy スクリプトの格納ディレクトリに変更します。
cd %~dp0\src\main\groovy
echo ------------------------------------------
echo --- 置換処理を実施します...
echo ------------------------------------------
java -Dfile.encoding=UTF-8 -cp .;../../../lib/* groovy.ui.GroovyMain SeleniumCodeBuilder.groovy %~dp0/input %~dp0/output %~dp0/setting.yaml
echo.
echo ------------------------------------------
echo --- パッケージ階層を設定します...
echo ------------------------------------------
java -Dfile.encoding=UTF-8 -cp .;../../../lib/* groovy.ui.GroovyMain PackageModifier.groovy %~dp0/output %~dp0/output

@rem --- 引数付きで本バッチが起動された場合は、pauseを行いません。
@if "%1"=="" (
pause
)

@rem --- 環境変数等のローカル化を終了します。
endlocal 

@rem --- 正常終了します。
exit /b 0

