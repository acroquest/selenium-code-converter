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
@rem seleniumCodeBuilder �ɂ��ϊ����������s���܂��B
@rem ============================================================================

@rem --- ���ϐ���J�����g�f�B���N�g�������[�J�������܂��B
setlocal

rem --- groovy �X�N���v�g�̊i�[�f�B���N�g���ɕύX���܂��B
cd %~dp0\src\main\groovy
echo ------------------------------------------
echo --- �u�����������{���܂�...
echo ------------------------------------------
java -Dfile.encoding=UTF-8 -cp .;../../../lib/* groovy.ui.GroovyMain SeleniumCodeBuilder.groovy %~dp0/input %~dp0/output %~dp0/setting.yaml
echo.
echo ------------------------------------------
echo --- �p�b�P�[�W�K�w��ݒ肵�܂�...
echo ------------------------------------------
java -Dfile.encoding=UTF-8 -cp .;../../../lib/* groovy.ui.GroovyMain PackageModifier.groovy %~dp0/output %~dp0/output

@rem --- �����t���Ŗ{�o�b�`���N�����ꂽ�ꍇ�́Apause���s���܂���B
@if "%1"=="" (
pause
)

@rem --- ���ϐ����̃��[�J�������I�����܂��B
endlocal 

@rem --- ����I�����܂��B
exit /b 0

