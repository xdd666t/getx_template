# getx_template

语言: [English](https://github.com/CNAD666/getx_template/blob/main/README.md) | [中文简体](https://github.com/CNAD666/getx_template/blob/main/docs/README-ZH.md)

## GetX使用

- 掘金：[Flutter GetX使用---简洁的魅力！](https://juejin.cn/post/6924104248275763208)

## 效果图

- 插件效果 
  - 看下插件使用的效果图吧，样式参考了fish_redux插件样式
  - 有一些可选择的功能，所以做成多按钮的样式，大家可以按照自己的需求进行操作

![getx_template](https://cdn.jsdelivr.net/gh/CNAD666/MyData/pic/flutter/blog/getx_plugin_show.gif)

## 说明

说下插件的功能含义
- Model：生成GetX的模式，
  - Default：默认模式，生成三个文件：state，logic，view
  - Easy：简单模式，生成三个文件：logic，view
- Function：功能选择
  - useFolder：使用文件，选择后会生成文件夹，大驼峰命名自动转换为：小写+下划线
  - usePrefix：使用前缀，生成的文件前加上前缀，前缀为：大驼峰命名自动转换为：小写+下划线
- Module Name：模块的名称，请使用大驼峰命名
