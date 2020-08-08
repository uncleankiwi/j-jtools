# j-jtools:Eplus用翻訳差し替えツール
![ss](https://i.imgur.com/PYIxSZs.png)
## 使い方
1. EplusReplace.jarをダブルクリックして実行する  
2. ソースコードを開く
3. 翻訳ファイルを開く
4. ｱｲﾃﾉｺﾞｰﾙﾆｼｭｩｰ

## 開けない・実行することができない場合
windows環境なら幾つの方法があります
- EplusReplace.jarを右クリックして  
	プログラムから開く >  
	Java Platform SE binaryを選択  

- コマンドプロンプトで  
	```java -jar EplusReplace.jar```  
	を入力して実行

## 注意点
- 出力されたファイルは所存するファイルを上書きしないように、  
  ファイルネームに日付や数字を付けていますが、  
  始まる前にソースコードの***バックアップを取ってください***。 

- 出力ファイルを指定できないのは間違えて上書きしてしまって  
  大惨事になるのを避けたかったからです。
  
- ANSI (正確に言うとwindows-31j)、UTF-8、Shift_JIS 以外のエンコーディングを使っているファイルは  
  読み込めません。

## 既知の不具合
今の所なし。不具合があれば報告してね！…してね！

## 仕組み解説
翻訳ファイルに2列がある場合：
- lang( )に入っているテキストだけを対応
- 1列目を使ってソースコードを検索
- 見つかった場合、2列目で差し替え
- 変数・定数の名前が違ってもちゃんと機能するはず

翻訳ファイルに7か8列がある場合：
- アイテム説明文だけを対応
- 1-4列目を使ってソースコードを検索
- 見つかった場合、5列目以上で差し替え

列の間にTabキャラクターが入っているので  
翻訳ファイルは.tsvとしてExcelやLibreOffice Calcで開けられます。

## 更新履歴
#### 1.1
- windows-31j だけではなく UTF-8 と Shift_JIS エンコーディングを使っている  
    ファイルも開けられるように。  
- 出力されるファイルのエンコーディングはソースコードファイルのを  
    使うように。  
#### 1.0 (無印)  
- でたぁ～
