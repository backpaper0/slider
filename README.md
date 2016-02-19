# Slider

勉強会で発表の際にAndroid端末からスライドのページをめくりたくて作ったWebアプリです。

## 使い方

まずビルドしてください。

```
gradle build
```

次にWebアプリを起動してください。

```
java -jar buils/libs/slider.jar
```

Android端末で `http://<ホストマシンのIPアドレス>:8080/` を開いてください。

スライドにフォーカスを当てたらAndroid端末でスライドのページをめくれます。
ページをめくると今のデスクトップがどんな状態なのかスクリーンショットを撮ってAndroid端末に返します。

## License

Licensed under [The MIT License](https://opensource.org/licenses/MIT)
