# getx_template

语言: [English](https://github.com/CNAD666/getx_template/blob/main/README.md) | [中文简体](https://juejin.cn/post/6924104248275763208)

## GetX usage

- Gold digging:  [Flutter GetX use --- simple charm!](https://github.com/CNAD666/getx_template/blob/main/docs/Use%20of%20Flutter%20GetX---simple%20charm!.md)

# some statement

- the fast code snippet prompt  come from [getx-snippets-intelliJ](https://github.com/cjamcu/getx-snippets-intelliJ/blob/master/src/main/resources/liveTemplates/getx.xml)

## Description

- Plugin effect

  - Take a look at the effect diagram used by the plugin. The style refers to the fish_redux plugin style.
  - There are some optional functions, so make it into a multi-button style, you can operate according to your own needs

![getx](https://cdn.jsdelivr.net/gh/CNAD666/MyData@master/pic/flutter/blog/20210719162534.gif)

- Support to modify suffix

![image-20210608153359876](https://cdn.jsdelivr.net/gh/CNAD666/MyData@master/pic/flutter/blog/20210608153418.png)

- Alt + Enter ： GetBuilder、GetBuilder（Auto Dispose）、Obx、GetX

![image-20210802160603092](https://cdn.jsdelivr.net/gh/CNAD666/MyData@master/pic/flutter/blog/20210802162033.png)

- Enter the **get**  prefix

![image-20210605150122801](https://cdn.jsdelivr.net/gh/CNAD666/MyData@master/pic/android/flutter/blog/20210605150851.png)

## Features

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

