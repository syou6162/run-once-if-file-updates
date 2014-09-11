# run-once-if-file-updates
ファイルが監視しておいて、更新されたら任意のコマンドを実行するスクリプト。以下のようなケースで多分役に立ちます(随時追加)。

- 機械学習で学習が終わってモデルファイルを吐き出したら、評価もやっておいてくれ

## Usage
以下のようなタスクファイルを用意します。

```
hoge.txt ls /
fuga.txt date
piyo.txt cal
```

一行毎に監視するファイルと実行したいコマンドを書いていきます。空白区切りです。ファイルが生成されるか最終更新日が新しくなったらコマンドを「一度だけ」実行します。

```sh
% cat taskfile | lein run -m run-once-if-file-updates.core
% cat taskfile | java -jar target/run-once-if-file-updates-0.0.1-standalone.jar
```

## License

Copyright © 2014 Yasuhisa Yoshida

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
