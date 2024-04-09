# polaris-toolkit

一个纯净的Java工具类库，提供一套标准SDK库与三方依赖的扩展库，力求功能丰富，不断优化和借鉴吸收各类优秀开源实现。

- polaris-core

  常用核心工具库，只存在Slf4j日志、JSR标准依赖，无更多的三方依赖，以保待纯净

- polaris-extra-*

  扩展工具库，针对特定用途或功能的扩展工具，有具体的三方库依赖

- polaris-builder

  代码生成器工具

## 依赖引入

### maven

- 只依赖核心库
```xml
<dependency>
  <groupId>cn.pivoto.polaris.toolkit</groupId>
  <artifactId>polaris-core</artifactId>
  <version>${polaris-version}</version>
</dependency>
```
- 依赖通用库
```xml
<dependency>
  <groupId>cn.pivoto.polaris.toolkit</groupId>
  <artifactId>polaris-all</artifactId>
  <type>pom</type>
  <version>${polaris-version}</version>
</dependency>
```

## 版本变更说明

- [计划](TODO.md)


### 3.1.25
- 增强Copier工具，增加key匹配模式参数

### 3.1.24
- 修复LocalNode获取IP节点错误

### 3.1.23
- 移除DecoderException类
- 优化部分工具类的命名与注释文字
- 修改SQL引用表达式解析方法
- 添加Mybatis的LanguageDriver实现TableRefResolvableDriver
- 优化Mybatis的LanguageDriver实现ProviderSqlSourceDriver
- 其他代码优化与测试

### 3.1.22
- 添加工具函数接口`Callable*`、`Executable*`
- 调整日志工具，增加方法，优化输出
- 优化Jdbc工具方法与注解处理
- 优化Mybatis扩展方法，修改Mapper实体声明注解
- 添加对SQL绑定取值时的异常处理
- 添加列别名前缀后缀注解处理，区分查询与更新语句的主键条件，查询时去除乐观锁字段条件
- 其他代码优化与测试

### 3.1.21
- 优化脚本引擎处理，调整类与方法签名
- 优化注解属性工具类`AnnotationAttributes`，添加工具方法
- 优化元属性工具类`MetaObject`，尽可能支持运行期属性读写
- 修改测试日志配置
- 修改`SqlNode`换行符，默认为空格以保持SQL日志在同行
- 优化`TextNode`，添加空值判断
- 增加实体表与字段的引用表达式的解析
- 增强Jdbc工具方法，添加接口执行器代理工具，添加多种sql构建与配置注解
- 原始SQL尽量支持引用表达式解析，顶层对象添加TableAccessible实现
- 优化注解处理器，添加`InsertStatement`相关方法
- Mybatis扩展增加自定义`LanguageDriver`实现以支持非`Map`类型参数
- 其他代码优化与测试

### 3.1.20
- 添加`polaris-json`子模块，提供工具类`Jsons`
- 优化字符串工具类`Strings`，添加工具方法，调整部分方法命名与参数类型
- 优化断言工具类`Assertions`、`Arguments`，添加工具方法
- 优化SQL工具类，添加对`&{tableAlias.tableField}`引用范式的处理，添加绑定变量取值的缓存支持
- 其他代码优化与测试

### 3.1.19
- 优化Sql构建条件处理
- 调整部分代码注释内容
- 其他代码优化与测试

### 3.1.18
- 添加参数断言工具类`Arguments`，区别于`Assertions`，统一抛出`IllegalArgumentException`异常
- 调整字符串工具类`Strings`部分方法命名
- 其他代码优化与测试

### 3.1.17
- 优化日志工具类方法中对无参日志内容的处理
- 其他代码优化与测试

### 3.1.16
- 添加Sql相关注解与其处理工具方法
- 优化Mybatis扩展处理，添加对SQL相关注解的支持
- 增强Jdbc注解处理器，添加字段的`groupBy`、`orderBy`、`having`工具方法
- 优化日志工具类，调整命名，添加缓存
- 其他代码优化与测试

### 3.1.15
- 修改密文处理工具类，封装统一异常类`CryptoRuntimeException`
- 添加日期格式化类`DateFormats`
- 优化代码生成器模板
- 其他代码优化与测试

### 3.1.14
- 修复JDBC工具类中对`in`子查询的处理问题
- 添加JDBC工具类对查询列别名的前缀与后缀支持
- 其他代码优化与测试

### 3.1.13
- 修复`SelectSegment`函数列别名处理问题
- 优化工具类`ServiceLoader`对`ServiceProperty`属性注解的读取
- 其他代码优化与测试

### 3.1.12
- 优化代码生成器模板
- 优化字符串处理工具类`Strings`，添加工具方法
- 其他代码优化与测试

### 3.1.11
- 优化日志类`StdoutLogger`，调整扩展方法
- 其他代码优化与测试

### 3.1.10
- 优化代码生成器模板
- 优化日志工具类，添加工具类`ILoggers`，优化日志组件判断方式与内容输出方法
- 其他代码优化与测试

### 3.1.9
- 优化代码生成器模板
- 优化反射处理工具类`Reflects`
- 优化断言工具类`Assertions`
- 调整`ConverterRegistry`，添加公开构造器
- 优化注解处理工具`Annotations`，提供原生与合并式的两类注解处理方法
- 其他代码优化与测试

### 3.1.8
- 优化类型转换工具类，修改实现，添加`Converters`工具
- 优化Jdbc相关工具类，添加集合操作支持
- 修改JUnit测试注解
- 添加Bean处理工具类`MetaObject`
- 其他代码优化与测试

### 3.1.7
- 优化代码生成器模板
- 添加工具类`StopWatch`
- 其他代码优化与测试

### 3.1.6
- 优化代码生成器模板
- 其他代码优化与测试

### 3.1.5
- 优化代码生成器模板与工具类
- 优化`BeanMap`工具类
- 其他代码优化与测试

### 3.1.4
- 优化代码生成器模板
- 其他代码优化与测试

### 3.1.3
- 优化代码生成器模板
- 优化Jdbc相关工具类
- 其他代码优化与测试

### 3.1.2
- 优化代码生成器模板
- 修复`MergeStatement`SQL语法错误
- 其他代码优化与测试

### 3.1.1
- 代码生成器优化
- 开发注解处理工具类
- 其他代码优化与测试

### 3.1.0
- 基于优化后的工程结构发布基础版本

### 3.0.1
- 优化工程结构，分离bom工程，开发工具库独立版本

### 1.x、2.x
- 已弃用，工具库历史过渡版本
