# Easy Rest

#### 介绍
参考Feign 代理方式，实现Spring 的简易的Bean 管理，实现简单的Rest 接口

#### 软件架构
- easy-rest 
-- easy-common
-- easy-config
-- easy-start
-- sample


#### 注解

1.  @AppStarting 启动类直接
2.  @RequestMapping 同Spring Controller 的注解
3.  @Component 同Spring 注解，作为组件
4. @DelMapping,@GetMapping,@PostMapping,@PutMapping 为@RequestMapping 的包装注解

#### 使用说明

##### 启动类

```java
package io.github.jinlongliao.easy.sample;

import io.github.jinlongliao.easy.common.annotation.AppStarting;
import io.github.jinlongliao.easy.start.AppRun;

/**
 * @author liaojinlong
 * @since 2020/7/10 15:56
 */
@AppStarting()
public class AppStart {
    public static void main(String[] args) {
        AppRun.run(AppStart.class, args);
    }
}

```

设置扫描资源的范围（默认为启动类的所在包路径及一下）

- scanner
- packages
```java
public @interface AppStarting {
    String[] scanner() default {};

    Class[] packages() default {};
}


```



