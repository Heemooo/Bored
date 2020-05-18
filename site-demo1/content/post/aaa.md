[^_^]:<>(---)
[^_^]:<>(title= "Effective Java阅读笔记")
[^_^]:<>(date = "2020-05-14")
[^_^]:<>(draft= true           #是否为草稿)
[^_^]:<>(---)
# Effective Java阅读笔记(一)

## 创建和销毁对象
### 1.考虑使用静态工厂方法替代构造器
- (1)使用静态工厂方法替代构造器，可以用名称更有意义的静态方法代替一些具有默认行为的构造器，例如：
```
private URL(Context context, String contentType, String outPutPath, byte[] bytes) {
    this.outPutPath = Paths.convertCorrectPath(outPutPath);
    this.context = context;
    this.contentType = contentType;
    this.bytes = bytes;
}

public static URL createHTMLURL(Context context, String outPutPath) {
    var contentType = "text/html;charset=utf-8";
    return new URL(context, contentType, outPutPath, null);
}

public static URL createDefaultURL(Context context, String outPutPath, byte[] bytes) {
    var contentType = "text/html;charset=utf-8";
    return new URL(context, contentType, outPutPath, bytes);
}

public static URL createStaticURL(Context context, String contentType, String outPutPath, byte[] bytes) {
    return new URL(context, contentType, outPutPath, bytes);
}
```

这样使用这个类的人就不用在众多构造器中挑选，避免调用错误的构造器
