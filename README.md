[![plugin](https://img.shields.io/badge/jetbrain-plugin-red)](https://plugins.jetbrains.com/plugin/15919-getx) [![stars](https://img.shields.io/github/stars/xdd666t/getx_template?logo=github)](https://github.com/CNAD666/getx_template) [![issues](https://img.shields.io/github/issues/xdd666t/getx_template?logo=github)](https://github.com/xdd666t/getx_template/issues) [![commit](https://img.shields.io/github/last-commit/xdd666t/getx_template?logo=github)](https://github.com/xdd666t/getx_template/commits) [![release](https://img.shields.io/github/v/release/xdd666t/getx_template)](https://github.com/xdd666t/getx_template/releases)

Language: English | [中文（详细讲解）](https://juejin.cn/post/7005003323753365517)
# Statement

- Part of fast code snippet prompt  come from [getx-snippets-intelliJ](https://github.com/cjamcu/getx-snippets-intelliJ/blob/master/src/main/resources/liveTemplates/getx.xml)

# Description

- install

![install](https://cdn.jsdelivr.net/gh/xdd666t/MyData@master/pic/flutter/blog/20220206120123.png)

- Plugin effect

  - Take a look at the effect diagram used by the plugin. The style refers to the fish_redux plugin style.
  - There are some optional functions, so make it into a multi-button style, you can operate according to your own needs

![useFolder](https://cdn.jsdelivr.net/gh/xdd666t/MyData@master/pic/flutter/blog/20210907092614.gif)

- Support to modify suffix

![image-20210926111944785](https://cdn.jsdelivr.net/gh/CNAD666/MyData@master/pic/flutter/blog/20210926112248.png)

- Alt + Enter ： GetBuilder、GetBuilder（Auto Dispose）、Obx、GetX

![GetBuilder](https://cdn.jsdelivr.net/gh/xdd666t/MyData@master/pic/flutter/blog/20210907092748.gif)

![image-20210802160603092](https://cdn.jsdelivr.net/gh/CNAD666/MyData@master/pic/flutter/blog/20210802162033.png)

![image-20210802160631405](https://cdn.jsdelivr.net/gh/CNAD666/MyData@master/pic/flutter/blog/20210802162043.png)

- Enter the **getx**  prefix

![getxroutepagemap](https://cdn.jsdelivr.net/gh/xdd666t/MyData@master/pic/flutter/blog/20210907092900.gif)

![image-20210922111700625](https://cdn.jsdelivr.net/gh/CNAD666/MyData@master/pic/flutter/blog/20210922111709.png)

# Features

- Model: Generate the GetX model
  - Default: Default mode, three files are generated: state, logic, view
  - Easy: Simple mode, two files are generated: logic, view

- Function: Function selection
  - useFolder: Use a file, a folder will be generated after selection, and the big hump name will be automatically converted to: lowercase + underscore

- usePrefix: Use the prefix, add the prefix before the generated file, the prefix is: Big Camel Name is automatically converted to: lowercase + underscore

- autoDispose: If you find that a page cannot automatically recycle GetxController, you can turn on this function, refer to How to automatically recycle GetXController; under normal circumstances, there is no need to turn on this function

- addLifecycle: Automatically add the life cycle callback method in GetXController, and enable it on demand

- addBinding: automatically add binding files
  - If you know what binding is, it is recommended to enable this function
  - If you don't understand the concept and function of binding, it is not recommended to turn it on; not using binding will not affect development

- Module Name: The name of the module, please use the big camel case as much as possible; capitalize the first letter


# Run this project

**If you want to run this project，please confirm some of your configuration**

- File ---> Project Structure ---> Project Settings：Project（SDK must use  jdk11）

![image-20211208095732612](https://cdn.jsdelivr.net/gh/xdd666t/MyData@master/pic/flutter/blog/20211208100315.png)

- Build, Execution, Deployment ---> Build Tools ---> Gradle
  - gradle download: https://services.gradle.org/distributions/ 

![image-20211208100031275](https://cdn.jsdelivr.net/gh/xdd666t/MyData@master/pic/flutter/blog/20211208100323.png)
