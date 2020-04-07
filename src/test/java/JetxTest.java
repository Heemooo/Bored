import cn.hutool.setting.dialect.Props;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;

import java.io.StringWriter;
import java.util.*;

public class JetxTest {

    public static void main(String args[]) {
        List<User> users = Arrays.asList(
                new User("张三", "zhangsan@qq.com"),
                new User("李四", "lisi@qq.com"),
                new User("王五", "wangwu@qq.com")
        );
        Props props = new Props("template.properties");
        System.out.println(props);
        props.setProperty("$loader.root", "D:/DevPs/Bored/target/test-classes");
        System.out.println(props);
        JetEngine engine = JetEngine.create(props);
        JetTemplate template = engine.getTemplate("/users.jetx");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("users", users);
        StringWriter writer = new StringWriter();
        template.render(context, writer);
        System.out.println(writer.toString());
    }
}