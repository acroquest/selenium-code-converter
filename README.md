# Selenium Code Converter
本ツールは、Selenium IDEからエクスポートされるWebDriverのソースコード（Java）の置換を行い、環境依存値の修正などを実施します。本ツールを利用することで、Selenium IDEからソースコードのエクスポートをした後、同じような修正を毎回実施しなくていけないという煩わしさから解放されます。

## 前提条件
以下のものがインストールされており、パスが設定されていることが必要となります。
- Java 1.8 以上
- Groovy 2.4 以上

## 実行方法
使い方は非常に簡単です。以下の手順でバッチファイルを実行してください。

1.  本ツールのzipファイルをダウロードし、任意のディレクトリで解凍してください。
1.  inputディレクトリ内に、変換対象のWebDriverのソースコードを置いてください。
1.  convert.bat を実行してください。

以上で実行は完了です。
実行が完了すると、outputディレクトリ内に、変換後のWebDriverのソースコードが出力されるため、それを利用してテストを実施してください。

## 設定内容

解凍して得られるディレクトリの直下に、setting.yaml というファイルが存在します。
このファイルの内容を変更することで、置換する内容をカスタマイズすることができます。

### 設定変更方法
以下の2種類の設定が変更できます。
1. 置換対象文字列
- before : '<置換対象文字>'
1. 置換後文字列
 - after : '<置換後文字列>'

設定を追加する場合は、以下の2行を任意の箇所に挿入してください。
- before : '<置換対象文字>'
- after : '<置換後文字列>'

## 注意事項
- 著作権について
    - このプログラムの著作権は [Acroquest Technology 社](http://www.acroquest.co.jp/) が保有しています。
- 事故、故障など
    - 本ツールを使用して起こった何らかの事故、故障などの責任は負いかねますので、ご使用の際はこのことを承諾したうえでご使用ください。

## ライセンス
本プロジェクトは、MIT Licenseの規約に基づき、複製や再配布、改変が許可されます。


Copyright(c) Acroquest Technology Co.,Ltd. All Rights Reserved.
