#### 项目搭建

父工程

添加<packaging>pom</packaging>

与子工程搭建关系

```xml
<modules>   
    <module>yeb-server</module>  
    <module>yeb-generator</module>
</modules>
```

在子工程中添加父工程信息

```xml
<parent>  
    <groupId>com.xxxx</groupId>   
    <artifactId>yeb</artifactId> 
    <version>1.0-SNAPSHOT</version>
</parent>
```

#### 逆向工程

```Java
package com.xxxx.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 *
 * @author zhoubin
 * @since 1.0.0
 */

public class CodeGenerator {

	/**
	 * <p>
	 * 读取控制台内容
	 * </p>
	 */
	public static String scanner(String tip) {
		Scanner scanner = new Scanner(System.in);
		StringBuilder help = new StringBuilder();
		help.append("请输入" + tip + "：");
		System.out.println(help.toString());
		if (scanner.hasNext()) {
			String ipt = scanner.next();
			if (StringUtils.isNotEmpty(ipt)) {
				return ipt;
			}
		}
		throw new MybatisPlusException("请输入正确的" + tip + "！");
	}

	public static void main(String[] args) {
		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		gc.setOutputDir(projectPath + "/yeb-generator/src/main/java");
		//作者
		gc.setAuthor("zhoubin");
		//打开输出目录
		gc.setOpen(false);
		//xml开启 BaseResultMap
		gc.setBaseResultMap(true);
		//xml 开启BaseColumnList
		gc.setBaseColumnList(true);
		// 实体属性 Swagger2 注解
		gc.setSwagger2(true);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl("jdbc:mysql://localhost:3306/yeb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia" +
				"/Shanghai");
		dsc.setDriverName("com.mysql.cj.jdbc.Driver");
		dsc.setUsername("epetadmin");
		dsc.setPassword("0000");
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent("com.xxxx")
				.setEntity("pojo")
				.setMapper("mapper")
				.setService("service")
				.setServiceImpl("service.impl")
				.setController("controller");
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};

		// 如果模板引擎是 freemarker
		String templatePath = "/templates/mapper.xml.ftl";
		// 如果模板引擎是 velocity
		// String templatePath = "/templates/mapper.xml.vm";

		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig(templatePath) {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
				return projectPath + "/yeb-generator/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper"
						+ StringPool.DOT_XML;
			}
		});
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 配置模板
		TemplateConfig templateConfig = new TemplateConfig();

		templateConfig.setXml(null);
		mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		//数据库表映射到实体的命名策略
		strategy.setNaming(NamingStrategy.underline_to_camel);
		//数据库表字段映射到实体的命名策略
		strategy.setColumnNaming(NamingStrategy.no_change);
		//lombok模型
		strategy.setEntityLombokModel(true);
		//生成 @RestController 控制器
		strategy.setRestControllerStyle(true);
		strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
		strategy.setControllerMappingHyphenStyle(true);
		//表前缀
		strategy.setTablePrefix("t_");
		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
		mpg.execute();
	}

}
```

#### 谷歌验证码

##### 所需依赖

```xml
<dependency> 
    <groupId>com.github.axet</groupId>  
    <artifactId>kaptcha</artifactId>   
    <version>0.0.9</version>
</dependency>
```

##### 验证码配置类

```Java
package com.xxxx.server.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置类
 */
@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha defaultKaptcha() {
        // 验证码生成器
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        // 配置
        Properties properties = new Properties();
        // 是否有边框
        properties.setProperty("kaptcha.border", "yes");
        // 设置边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        // 边框粗细度，默认为1
        //properties.setProperty("kaptcha.border.thickness", "1");
        // 验证码
        properties.setProperty("kaptcha.session.key", "code");
        // 验证码文本字符颜色，默认黑色
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        // 设置字体样式
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        // 字体大小，默认为40
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        // 验证码文本字符内容范围 默认为abcd2345678gfynmnpwx
        // properties.setProperty("kaptcha.textproducer.char.string", "");
        // 字符长度 默认为5
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 字符间距 默认为2
        properties.setProperty("kaptcha.textproducer.char.space", "4");
        // 验证码图片宽度 默认为200
        properties.setProperty("kaptcha.image.width", "100");
        // 验证码图片高度 默认为40
        properties.setProperty("kaptcha.image.height", "40");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}

```

##### 请求验证码

```Java
package com.xxxx.server.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码
 */
@RestController
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "验证码")
    @GetMapping(value = "/captcha",produces = "image/jpeg")
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        // 生成验证码图片必要的设置
        // 定义response输出类型为image/jpeg类型
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader)
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");

        //---------------生成验证码开始 begin --------------
        //获取验证码内容
        String text = defaultKaptcha.createText();
        System.out.println("验证码内容"+text);
        //将验证码文本内容放入session
        request.getSession().setAttribute("captcha",text);
        //根据文本验证码内容创建图形验证码
        BufferedImage image=defaultKaptcha.createImage(text);
        //用流的形式传出去
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //输出流输出图片，格式为jpg
            ImageIO.write(image,"jpg",outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(null != outputStream){
                try{
                    outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        //---------------生成验证码开始 begin --------------
    }

}

```

#### 存储过程

![1642577750090](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642577750090.png)

中括号里面的参数都是可选可不选的

![1642578878255](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642578878255.png)

![1642578905852](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642578905852.png)

![1642579113686](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1642579113686.png)

存储过程的基础使用

```sql
#变量必须使用@开头，并且不能滥用，可能会导致程序难以理解和管理
#创建一个存储过程
CREATE PROCEDURE GreetWorld() SELECT CONCAT(@grettion,'world');
#为变量赋值，范围变量
SET @grettion='Hello';
#调用存储过程
CALL GreetWorld();
#设置存储变量
CREATE PROCEDURE p1() SET @last_procedure='p1';
CREATE PROCEDURE p2() SELECT CONCAT('last procedure was ',@last_procedure);
CALL p1();
CALL p2();
```

##### 添加部门的存储过程

```sql
DELIMITER $$

USE `yeb`$$

DROP PROCEDURE IF EXISTS `addDep`$$

CREATE DEFINER=`epetadmin`@`localhost` PROCEDURE `addDep`(IN depName VARCHAR(32),IN parentId INT,IN enabled BOOLEAN,OUT result INT,OUT result2 INT)
BEGIN
  DECLARE did INT;#申明一个变量，新增生成id
  DECLARE pDepPath VARCHAR (64);#申明一个变量，路径
  #新增部门
  INSERT INTO t_department SET NAME=depName,parentId=parentId,enabled=enabled;
  #查询受影响的行数
  SELECT ROW_COUNT() INTO result;
  #获取最后一次生成的id，也就是刚新增部门生成的id
  SELECT LAST_INSERT_ID() INTO did;
  #赋值
  SET result2=did;
  #根据父id查询父级的路径
  SELECT depPath INTO pDepPath FROM t_department WHERE id=parentId;
  #修改路径为父级路劲加.自身id
  UPDATE t_department SET depPath=CONCAT(pDepPath,'.',did) WHERE id=did;
  #修改父级的部门是否有子级，改为1有
  UPDATE t_department SET isParent=TRUE WHERE id=parentId;
END$$

DELIMITER ;
```

##### 删除部门的存储过程

```SQL

USE `yeb`$$

DROP PROCEDURE IF EXISTS `deleteDep`$$

CREATE DEFINER=`epetadmin`@`localhost` PROCEDURE `deleteDep`(IN did INT,OUT result INT)
BEGIN
  DECLARE ecount INT;
  DECLARE pid INT;
  DECLARE pcount INT;
  DECLARE a INT;
  #查询删除部门是否有子级，如果有不允许删除，
  #查到result=-2，否者查找该部门下面是否有员工，有员工也不能删
  #如果有result=-1，在否者查到他的父id赋值给pid,直接删除该部门
  SELECT COUNT(*) INTO a FROM t_department WHERE id=did AND isParent=FALSE;
  IF a=0 THEN SET result=-2;
  ELSE
  SELECT COUNT(*) INTO ecount FROM t_employee WHERE departmentId=did;
  IF ecount>0 THEN SET result=-1;
  ELSE 
  SELECT parentId INTO pid FROM t_department WHERE id=did;
  DELETE FROM t_department WHERE id=did AND isParent=FALSE;
  #查询受影响的行数，出参
  SELECT ROW_COUNT() INTO result;
  #查找父id，为删除部门的父id有多少，如果没有则把id为pid的有没有子部门改为没有
  SELECT COUNT(*) INTO pcount FROM t_department WHERE parentId=pid;
  IF pcount=0 THEN UPDATE t_department SET isParent=FALSE WHERE id=pid;
  END IF;
  END IF;
  END IF;
END$$

DELIMITER ;
```

##### 添加部门使用mapper.xml文件调用存储过程

```xml
<!--添加部门-->
    <select id="addDep" statementType="CALLABLE">
        call addDep(#{name,mode=IN,jdbcType=VARCHAR},
        #{parentId,mode=IN,jdbcType=INTEGER},
        #{enabled,mode=IN,jdbcType=BOOLEAN},
        #{result,mode=OUT,jdbcType=INTEGER},
        #{id,mode=OUT,jdbcType=INTEGER})
    </select>
```

#### jdk操作集合的新特性

##### Stream

JDK8 中的 Stream 是对集合（Collection）对象功能的增强，它借助于lambda表达式，更优雅的表达风格，极大的提高编程效率和程序可读性。它针对于对集合对象进行各种非常便利、高效的聚合操作，或者大批量数据操作。

##### forEach()

用于遍历流中的对象，forEach()相对于map()，前者适合只对原数据读操作比如将list中的数据遍历出来，后者是把数据遍历出来，然后再生成新数据。

```java
    @Test
    public void testForEach(){
        // java 8 前
        System.out.println("java 8 前");
        for(User user: list){
            System.out.println(user);
        }

        // java 8 lambda
        System.out.println("java 8 lambda");
        list.forEach(user -> System.out.println(user));
        // java 8 stream lambda
        System.out.println("java 8 stream lambda");
        list.stream().forEach(user -> System.out.println(user));
    }

```

#####    map把数据遍历出来，再生成新的数据

```java
@Test
public void testMap() {
    // 小写转大写
    List<String> words = Arrays.asList("aaa", "vvvv", "cccc");
    System.out.println("全部大写---->");
    // collect(Collectors.toList()) 意为收集处理完后的数据再转换成List集合。
List<String> collect = words.stream().map(s -> s.toUpperCase()).collect(Collectors.toList());
    collect.forEach(s -> System.out.println(s));
    //转为角色名，String类型的数组
    String[] str=menu.getRoles().stream().map(Role::getName)
                        .toArray(String[]::new);
}

```

##### filter()

```java 
@Test
public void testFilter() {
    // 输出年龄大于50的人
    System.out.println("-----过滤前-----");
    list.forEach(user -> System.out.println(user));
    System.out.println("-----过滤后-----");
    // java 8 前
    System.out.println("java 8 前");
    for(User user: list){
        if (user.age > 50) {
            System.out.println(user);
        }
    }
    // java 8 stream
    System.out.println("java 8 stream");
    list.stream().filter(user -> user.age > 50).forEach(user -> System.out.println(user));
}

```

Apache POI简介

使用Excel处理数据

相关依赖

```xml
		<!--easy poi依赖-->
        <dependency>
            <groupId>cn.afterturn</groupId>
            <artifactId>easypoi-spring-boot-starter</artifactId>
            <version>4.1.3</version>
        </dependency>
```

#### 邮箱配置

##### 依赖

```xml
<dependencies>
    <!--rabbitmq 依赖-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    <!--mail 依赖-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-mail</artifactId>
      <version>2.6.2</version>
    </dependency>

    <!--thymeleaf 依赖-->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-thymeleaf</artifactId>
      </dependency>
    <!--server 依赖-->
    <dependency>
      <groupId>com.xxxx</groupId>
      <artifactId>yeb-server</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
  </dependencies>
```

##### 配置

![1643119296044](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1643119296044.png)

