
# Preface

> When using Bloc, there is a problem that I have been very concerned about so far, it is impossible to truly cross- page interaction! After repeatedly consulting official documents, a global Bloc method was used to achieve "pseudo" cross- page interaction. For details, please see: [flutter_bloc usage analysis](https://juejin.cn/post/6856268776510504968); fish_redux broadcast The mechanism is perfect to achieve cross- page interaction. I also wrote a nearly 10,000- character introduction on how to use the framework: [fish_redux detailed explanation](https://juejin.cn/post/6860029460524040199), for small and medium- sized projects fish_redux, this will reduce development efficiency to a certain extent. Recently, I tried GetX related functions, which solved a lot of my pain points.

> After writing the entire article, I immediately replaced all the Bloc code in one of my demos with GetX, and removed the Fluro framework; I feel that although using Getx will save a lot of template code, there is still some repetitive work: Create Folder, create a few necessary files, write the initialization code and classes that must be written; slightly cumbersome, ** in order to be worthy of the great convenience GetX brings to my development, I spent some time and wrote a Plugin! ** The above repeated codes, files and folders can all be generated with one click!

**GetX related advantages**

- The build refresh method is extremely simple!
  - getx: Obx(() => Text())
  - This is an aspect that I am very concerned about, because bloc's build refresh component method needs to pass two generics, plus two parameters in the build method, resulting in a build method that does not use the arrow method shorthand, which accounts for almost four or five lines , It is really egg- shell, which leads me to write the `BlocBuilder` method directly on the top of the page (not advocating writing the top layer). I only need to write a page once, and I don’t need to write `BlocBuilder` everywhere. Manually funny. jpg
- Cross- page interaction
  - This is definitely an advantage of GetX! For complex production environments, cross- page interaction scenarios are too common. GetX's cross- page interaction is almost as simple as fish_redux. I love it.
- Route management
  - Yes, getx implements routing management internally, and it is simple to use! Bloc did not implement routing management, which made me have to find a routing management framework with a high number of stars, so I chose fluro, but let me have to say that this fluro is really a tormentor to use, and I create a new page every time , The thing I resist most is to write fluro routing code, writing back and forth across several files, it really hurts
  - GetX implements dynamic routing parameter transfer, that is to say directly spelling parameters on the named route, and then you can get the parameters spelled on the route, that is to say, write H5 with flutter, and directly pass the value through Url (fluro can also Do it), OMG! You can abandon complicated fluro without thinking
- Implemented the global BuildContext
- Internationalization, theme realization

The above alone is the advantage of the build abbreviation, it will make me consider whether to use it, and also realize the cross- page function, what else to consider, start!

The following will give a comprehensive introduction to the use of GetX. The article is not divided into articles. I will try my best to write an article clearly so that everyone can refer to it at any time.

# Preparation

## Introduction

- First import the GetX plugin

```dart
# getx State management framework https://pub.flutter- io.cn/packages/get
get: ^3.24.0
```

**GetX address**

- Github: [jonataslaw/getx](https://github.com/jonataslaw/getx)
- Pub: [get](https://pub.dev/packages/get)

## Main entrance configuration

- Just change `MaterialApp` to `GetMaterialApp`

```dart
void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      home: CounterGetPage(),
    );
  }
}
```

- Each module guide package, just use the bread

```dart
import'package:get/get.dart';
```

## Plugin

> In the process of writing plug- ins under Tucao, it is not difficult to actually write this kind of template code to generate plug- ins. There are many people on the Internet who have written examples. Refer to the reference ideas, and they can be compiled quickly, but some configurations are more egg- shaped.
>
> At the beginning, I chose the Plugin DevKit mode and it has been written, but when I read the official website document, the official document said at the beginning: It is recommended to use the Gradle mode to develop plug- ins, and Barabara listed a lot of benefits; after a long time of consideration, I decided to use Gradle Mode rewriting.
>
> In this Gradle mode, the most annoying thing is that when you create a new project at the beginning, the Gradle cannot be downloaded all the time, and the Internet is not available in the scientific global. Then you manually download Gradle, specify the local Gradle, and when you start the global synchronization again, it will download a larger one Community version of IDEA, but after loading using local Gradle, there is a big bug! Under the main folder, the Java folder will not be automatically generated! I am really a buddha. Click on other folders and right- click: New - > Plugin DevKit. There is no Action option. I almost persuaded me to quit. I changed to seven or eight versions of IDEA and tried it! The Action option does not come out. After two days, I accidentally tried to create a new Java file under the main folder at night, and then right- clicked on the java file: New - > Plugin DevKit, the Action option appeared! Really a few Buddhas. . .
>
> There is a huge pit problem. If you develop a plug- in in Gradle mode, put the template code file under the main file, under the src, and under the root directory, and you can’t get the contents of the file. This is really a pit I searched a lot of blogs for a long time, and found that I didn’t write about this issue. I read the official document example for a few times and I didn’t find any explanation. Later, I found a project three years ago. I looked through the code and found out all the resources. Files must be placed in the resources folder to read the contents of the file. . . What the hell. . .

### Description

- Plug- in address
  - Github: [getx_template](https://github.com/CNAD666/getx_template)
  - Jetbrains: [getx_template](https://plugins.jetbrains.com/plugin/15919- getx/versions)
- Plug- in effects
  - Look at the renderings used by the plug- in, the style refers to the fish_redux plug- in style
  - There are some optional functions, so it is made into a multi- button style, and everyone can operate according to their needs
- Talk about the functional meaning of the plug- in
  - Model: Generate GetX model,
    - Default: default mode, three files are generated: state, logic, view
    - Easy: Simple mode, generate two files: logic, view
  - Function: Function selection
    - useFolder: Use a file, a folder will be generated after selection, and the big hump name will be automatically converted to: lowercase + underscore
    - usePrefix: Use the prefix, add the prefix before the generated file, the prefix is: Big Camel Name is automatically converted to: lowercase + underscore
  - Module Name: The name of the module, please use the big camel case name

### Effect picture

- Take a look at the renderings used

![getx_template](https://cdn.jsdelivr.net/gh/CNAD666/MyData/pic/flutter/blog/getx_plugin_show.gif)

### Installation

- Select in the settings: Plugins - - - > Enter "getx" to search - - - > Choose the name: "GeX" - - - > Then install - - - > Finally, remember to click "Apply"
- If you have any problems in the process of using the plug- in, please submit an issue to me on the github of the project, and I will deal with it as soon as I see it

![image- 20210130182527494](https://cdn.jsdelivr.net/gh/CNAD666/MyData/pic/flutter/blog/20210130182833.png)



# Counter

## Effect picture

- [Experience it](https://cnad666.github.io/flutter_use/web/index.html#/counterGet)

![counter_getx](https://cdn.jsdelivr.net/gh/CNAD666/MyData/pic/flutter/blog/counter_getx.gif)

## Implementation

**The home page, of course, is to implement a simple counter to see how GetX decouples the logic layer from the interface layer**

- Use the plugin to generate a simple file
  - Mode selection: Easy
  - Function selection: useFolder

![image- 20210126175019383](https://cdn.jsdelivr.net/gh/CNAD666/MyData/pic/flutter/blog/getx_plugin_user01.png)

Take a look at the generated default code, the default code is very simple, the detailed explanation is placed in the two state management

- logic

```dart
import'package:get/get.dart';

class CounterGetLogic extends GetxController {

}
```

- view

```dart
import'package:flutter/material.dart';
import'package:get/get.dart';

import'logic.dart';

class CounterGetPage extends StatelessWidget {
  final CounterGetLogic logic = Get.put(CounterGetLogic());

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}
```

### Responsive state management

> When the data source changes, the method of refreshing the component will be executed automatically

- logic layer
  - Because it handles page logic, plus the Controller word is too long to prevent confusion with some of the control controllers that come with Flutter, so this layer ends with `logic`, here is set as `logic` layer, of course this point You can write Event or Controller as you like
  - Write `.obs` operation after the variable value, which means that the variable is defined as a responsive variable. When the value of the variable changes, the refresh method of the page will be automatically refreshed; basic types, List, and classes can all add `.obs `To make it a reactive variable

```dart
class CounterGetLogic extends GetxController {
  var count = 0.obs;

  ///Increase
  void increase() => ++count;
}
```

- view layer

  - After obtaining the instance of the Logic layer here, you can operate it. You may think: WTF, why is the operation of the instance placed in the build method? Are you kidding me? - - - - - - - - -  Actually otherwise, stl is a stateless component, which means that it will not be reorganized twice, so the instance operation will only be executed once, and the Obx() method can refresh the component, which is perfect Solve the problem of refreshing components

    ```dart
    class CounterGetPage extends StatelessWidget {
      @override
      Widget build(BuildContext context) {
        CounterGetLogic logic = Get.put(CounterGetLogic());
    
        return Scaffold(
          appBar: AppBar(title: const Text('GetX counter')),
          body: Center(
            child: Obx(
              () => Text('Clicked ${logic.count.value} times',
                  style: TextStyle(fontSize: 30.0)),
            ),
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: () => logic.increase(),
            child: const Icon(Icons.add),
          ),
        );
      }
    }
    ```

  - Of course, you can also write like this

    ```dart
    class CounterGetPage extends StatelessWidget {
      final CounterGetLogic logic = Get.put(CounterGetLogic());
    
      @override
      Widget build(BuildContext context) {
        return Scaffold(
          appBar: AppBar(title: const Text('GetX counter')),
          body: Center(
            child: Obx(
              () => Text('Clicked ${logic.count.value} times',
                  style: TextStyle(fontSize: 30.0)),
            ),
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: () => logic.increase(),
            child: const Icon(Icons.add),
          ),
        );
      }
    }
    ```

  - It can be found that the method to refresh the component is extremely simple: `Obx()`, so that you can write fixed- point refresh operations everywhere happily

- Obx() method refresh conditions

  - Only when the value of the response variable changes, the refresh operation will be performed. When the initial value of a variable is: "test", then the value is assigned: "test", and the refresh operation will not be performed
  - When you define a responsive variable and the responsive variable changes, the Obx() method that wraps the responsive variable will perform the refresh operation, and other Obx() methods that do not wrap the responsive variable will not be executed Refresh operation, Cool!

- Let's take a look at how to implement the update operation if the entire class object is set to the response type?

  - The following explanation comes from the official README document
  - I tried it here. Set the entire class object as the response type. When you change one of the variables of the class and then perform the update operation, `As long as the Obx() of the response class variable is wrapped, the refresh operation will be performed`, and the entire The class sets the response type, which needs to be combined with actual scenarios

```dart
// model
// We will make the entire class observable, not every attribute.
class User() {
    User({this.name ='', this.age = 0});
    String name;
    int age;
}

// controller
final user = User().obs;
//When you need to update the user variable.
user.update( (user) {// This parameter is the class itself you want to update.
    user.name ='Jonny';
    user.age = 18;
});
// Another way to update the user variable.
user(User(name:'João', age: 35));

// view
Obx(()=> Text("Name ${user.value.name}: Age: ${user.value.age}"))
// You can also access the model value without using .value.
user().name; // Note that it is a user variable, not a class variable (the first letter is lowercase).
```

### Simple state management

> GetBuilder: This is an extremely lightweight state manager that takes up very little resources!

- logic: Let’s look at the logic layer

```dart
class CounterEasyGetLogic extends GetxController {
  var count = 0;

  void increase() {
    ++count;
    update();
  }
}
```

- view

```dart
class CounterEasyGetPage extends StatelessWidget {
  final CounterEasyGetLogic logic = Get.put(CounterEasyGetLogic());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Counter- Simple')),
      body: Center(
        child: GetBuilder<CounterEasyGetLogic>(
          builder: (logicGet) => Text(
            'Clicked ${logicGet.count} times',
            style: TextStyle(fontSize: 30.0),
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => logic.increase(),
        child: const Icon(Icons.add),
      ),
    );
  }
}
```

- Analysis: GetBuilder method
  - init: Although the above code is not used, this parameter exists in GetBuilder, because it uses `Get.put()` to generate the `CounterEasyGetLogic` object when loading variables, and GetBuilder will automatically find the object. Therefore, you can not use the init parameter
  - builder: method parameter, has an input parameter, the type is the generic type passed in by GetBuilder
  - initState, dispose, etc.: GetBuilder has all periodic callbacks of StatefulWidget and can do some operations in the corresponding callbacks

## to sum up

**analysis**

- `GetBuilder` is actually a package of StatefulWidget, so it takes up very little resources
- Responsive variable, because it uses `StreamBuilder`, it will consume certain resources

**scenes to be used**

- Generally speaking, responsive variables can be used for most scenarios
- However, in a List that contains a large number of objects, reactive variables are used, which will generate a large number of `StreamBuilder`, which will inevitably cause greater pressure on memory. In this case, consider using simple state management

# Cross- page interaction

> Cross- page interaction is a very important function in complex scenarios. Let's see how GetX realizes cross- page event interaction

## Effect picture

- [Experience it](https://cnad666.github.io/flutter_use/web/index.html#/jumpOne)
- Cool, this is the real cross- page interaction! The lower- level page can call the upper- level page event at will, and after the page is closed, the next time it is reentered, the data will be reset naturally (the global Bloc will not be reset, and it needs to be reset manually)

![jump_getx](https://cdn.jsdelivr.net/gh/CNAD666/MyData/pic/flutter/blog/jump_getx.gif)

## Implementation

### Page One

Regular code

- logic
  - The self- increment event here is for other pages to call, the page itself is not used

```dart
class JumpOneLogic extends GetxController {
  var count = 0.obs;

  ///Jump to cross page
  void toJumpTwo() {
    Get.toNamed(RouteConfig.jumpTwo, arguments: {'msg':'I am the data passed from the previous page'));
  }

  ///Jump to cross page
  void increase() => count++;
}
```

- view
  - Here is a display text and jump function

```dart
class JumpOnePage extends StatelessWidget {
  /// Use Get.put() to instantiate your class to make it available to all current sub- routes.
  final JumpOneLogic logic = Get.put(JumpOneLogic());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(title: Text('Cross- Page- One')),
      floatingActionButton: FloatingActionButton(
        onPressed: () => logic.toJumpTwo(),
        child: const Icon(Icons.arrow_forward_outlined),
      ),
      body: Center(
        child: Obx(
          () => Text('Cross- page - Two clicked ${logic.count.value} times',
              style: TextStyle(fontSize: 30.0)),
        ),
      ),
    );
  }
}
```

### Page Two

This page is the focus

- logic
  - Will demonstrate how to call the event of the previous page
  - How to receive the last page data
  - Please note that `GetxController` contains a relatively complete life cycle callback, which can accept the passed data in `onInit()`; if the received data needs to be refreshed to the interface, please receive the data operation in the `onReady` callback, `onReady `Is called in the callback of `addPostFrameCallback`, and the operation of refreshing the data is performed in `onReady`, which can ensure that the page refresh operation is performed after the initial loading of the interface

```dart
class JumpTwoLogic extends GetxController {
  var count = 0.obs;
  var msg =''.obs;

  @override
  void onReady() {
    var map = Get.arguments;
    msg.value = map['msg'];

    super.onReady();
  }

  ///Jump to cross page
  void increase() => count++;
}
```

- view
  - The click event of the plus sign, when clicked, can realize the transformation of the two page data
  - Here comes the important point. Here, through `Get.find()`, you can get the GetXController instantiated before, and get the GetXController of a certain module. You can use this GetXController to call the corresponding event, or through it, Get the data of the module!

```dart
class JumpTwoPage extends StatelessWidget {
  final JumpOneLogic oneLogic = Get.find();
  final JumpTwoLogic twoLogic = Get.put(JumpTwoLogic());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(title: Text('Cross- Page- Two')),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          oneLogic.increase();
          twoLogic.increase();
        },
        child: const Icon(Icons.add),
      ),
      body: Center(
        child: Column(mainAxisSize: MainAxisSize.min, children: [
          //Count display
          Obx(
            () => Text('Cross page - Two clicked ${twoLogic.count.value} times',
                style: TextStyle(fontSize: 30.0)),
          ),

          //Transfer data
          Obx(
            () => Text('Data passed: ${twoLogic.msg.value}',
                style: TextStyle(fontSize: 30.0)),
          ),
        ]),
      ),
    );
  }
}
```

## to sum up

Cross- page interaction events such as GetX are really very simple and very low intrusive. There is no need to configure anything in the main entrance. In complex business scenarios, such a simple cross- page interaction method can achieve a lot It's up

# Advance it! counter

> We may have encountered many complex business scenarios. In complex business scenarios, there may be a lot of initialization operations for variables in a module alone. At this time, if the state (state layer) and logic (logic Layers) are written together, it may look awkward to maintain. Here, the state layer and the logic layer are separated, so that using GetX in a slightly larger project can also ensure that the structure is clear enough!

Let's continue to use counter examples here!

## Implementation

Three structures need to be divided here: state (state layer), logic (logic layer), view (interface layer)

- Use the plugin to generate the template code here
  - Model: select Default (default)
  - Function: useFolder (default)

![image- 20210127093925934](https://cdn.jsdelivr.net/gh/CNAD666/MyData/pic/flutter/blog/getx_plugin_use02.png)

**Look at the generated template code**

- state

```dart
class CounterHighGetState {
  CounterHighGetState() {
    ///Initialize variables
  }
}
```

- logic

```dart
import'package:get/get.dart';

import'state.dart';

class CounterHighGetLogic extends GetxController {
  final state = CounterHighGetState();
}
```

- view

```dart
import'package:flutter/material.dart';
import'package:get/get.dart';

import'logic.dart';
import'state.dart';

class CounterHighGetPage extends StatelessWidget {
  final CounterHighGetLogic logic = Get.put(CounterHighGetLogic());
  final CounterHighGetState state = Get.find<CounterHighGetLogic>().state;

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}
```

Why do you write such three modules, you need to put State separately, please browse below

### Retrofit

- state
  - It can be found here that the count type uses `RxInt` instead of `var`. The reason for using this variable type is that all operations are initialized in the constructor. If you use `var` directly, there is no immediate Assignment cannot be deduced as `Rx` type, so it is directly defined as `RxInt`. It is actually very simple. The basic type will be capitalized at the beginning and then prefixed with `Rx`
  - In fact, it is possible to use `var` directly, but when using the response variable, `.value` cannot be prompted, you need to write by yourself, so let’s write down the specific type of Rx honestly.
  - For details, please view: [Declare Reactive Variables](https://github.com/jonataslaw/getx/blob/master/documentation/zh_CN/state_management.md#%E5%A3%B0%E6%98%8E%E4 %B8%80%E4%B8%AA%E5%93%8D%E5%BA%94%E5%BC%8F%E5%8F%98%E9%87%8F)

```dart
class CounterHighGetState {
  RxInt count;

  CounterHighGetState() {
    count = 0.obs;
  }
}
```

- logic
  - The logic layer is relatively simple. It should be noted that the state class needs to be instantiated at the beginning

```dart
class CounterHighGetLogic extends GetxController {
  final state = CounterHighGetState();

  ///Increase
  void increase() => ++state.count;
}
```

- view
  - In fact, the view layer is almost the same as the previous one, the difference is that the state layer is independent
  - Because `CounterHighGetLogic` is instantiated, you can directly use `Get.find<CounterHighGetLogic>()` to get the logic layer just instantiated, then get the state, and use a separate variable to receive it
  - ok, at this time: logic only focuses on triggering event interaction, state only focuses on data

```dart
class CounterHighGetPage extends StatelessWidget {
  final CounterHighGetLogic logic = Get.put(CounterHighGetLogic());
  final CounterHighGetState state = Get.find<CounterHighGetLogic>().state;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Counter- Responsive')),
      body: Center(
        child: Obx(
              () => Text('Clicked ${state.count.value} times',
              style: TextStyle(fontSize: 30.0)),
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => logic.increase(),
        child: const Icon(Icons.add),
      ),
    );
  }
}
```

### Compared

> After reading the above transformation, you may want to spit out in front of the screen: pit bi, the previous simple logic layer was split into two, and it was so troublesome. Are you the monkey invited?
>
> Don’t rush to complain. When the business is too complex, the state layer will maintain a lot of things. Let’s take a look at the following example code. The following example code cannot be run directly. If you want to see the detailed running code, please check Project: [flutter_use](https://github.com/CNAD666/flutter_use)

- state

```dart
class MainState {
  ///Select index- responsive
  RxInt selectedIndex;

  ///Control whether to expand- responsive
  RxBool isUnfold;

  ///Classification button data source
  List<BtnInfo> list;

  ///Navigation item information
  List<BtnInfo> itemList;

  ///PageView page
  List<Widget> pageList;
  PageController pageController;

  MainState() {
    //Initialize index
    selectedIndex = 0.obs;
    //Not expanded by default
    isUnfold = false.obs;
    //PageView page
    pageList = [
      keepAliveWrapper(FunctionPage()),
      keepAliveWrapper(ExamplePage()),
      keepAliveWrapper(Center(child: Container())),
    ];
    //item column
    itemList = [
      BtnInfo(
        title: "Function",
        icon: Icon(Icons.bubble_chart),
      ),
      BtnInfo(
        title: "Example",
        icon: Icon(Icons.opacity),
      ),
      BtnInfo(
        title: "Settings",
        icon: Icon(Icons.settings),
      ),
    ];
    //Page controller
    pageController = PageController();
  }
}
```

- logic

```dart
class MainLogic extends GetxController {
  final state = MainState();

  ///Switch tab
  void switchTap(int index) {
    state.selectedIndex.value = index;
  }

  ///Whether to expand the sidebar
  void onUnfold(bool unfold) {
    state.isUnfold.value = !state.isUnfold.value;
  }
}
```

- view

```dart
class MainPage extends StatelessWidget {
  final MainLogic logic = Get.put(MainLogic());
  final MainState state = Get.find<MainLogic>().state;

  @override
  Widget build(BuildContext context) {
    return BaseScaffold(
      backgroundColor: Colors.white,
      body: Row(children: [
        ///Sidebar area
        Obx(
          () => SideNavigation(
            selectedIndex: state.selectedIndex.value,
            sideItems: state.itemList,
            onItem: (index) {
              logic.switchTap(index);
              state.pageController.jumpToPage(index);
            },
            isUnfold: state.isUnfold.value,
            onUnfold: (unfold) {
              logic.onUnfold(unfold);
            },
          ),
        ),

        ///Expanded takes up the remaining space
        Expanded(
          child: PageView.builder(
            physics: NeverScrollableScrollPhysics(),
            itemCount: state.pageList.length,
            itemBuilder: (context, index) => state.pageList[index],
            controller: state.pageController,
          ),
        )
      ]),
    );
  }
}
```

As can be seen from the above, there are already more states in the state layer. When some modules involve a large number of: submit form data, jump data, display data, etc., there will be quite a lot of code in the state layer, believe me, It’s really a lot. Once the business changes and frequent maintenance and modification are needed, it’s an egg cone.

In a complex business, it is definitely a wise move to separate the state layer (state) from the business logic layer (logic)

## At last

- The rendering of this module will not be released, it is exactly the same as the counter effect above. If you want to experience it, click: [Experience](https://cnad666.github.io/flutter_use/web/index.html#/counterHighGet)
- For simple business modules, two- tier structure can be used: logic, view; for complex business modules, three- tier structure is recommended: state, logic, view

# Route management

**GetX implements a very simple route management, you can use a very simple way to navigate, you can also use named route navigation**

## Simple routing

- Main entrance configuration

```dart
void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      home: MainPage(),
    );
  }
}
```

- Related use of routing
  - It is very simple to use, just use api like Get.to(), here is a simple demonstration, detailed api description, put at the end of this section

```dart
//Jump to new page
Get.to(SomePage());
```

## Naming route navigation

**Here is the recommended way to navigate using named routes**

- Unified management of all pages
- You may not feel it in the app, but on the web side, the url address of the loaded page is the string you set for the named route, that is to say, in the web, you can directly navigate to the relevant page through the url

The following explains how to use

- First, under the main entrance and exit configuration

```dart
void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      initialRoute: RouteConfig.main,
      getPages: RouteConfig.getPages,
    );
  }
}
```

- RouteConfig class
  - The following are my related pages and their mapped pages, please write them according to your own pages

```dart
class RouteConfig{
  ///main page
  static final String main = "/";

  ///dialog page
  static final String dialog = "/dialog";

  ///bloc counter module
  static final String counter = "/counter";

  ///Test layout page
  static final String testLayout = "/testLayout";

  ///Demo SmartDialog control
  static final String smartDialog = "/smartDialog";

  ///Bloc passes events across pages
  static final String spanOne = "/spanOne";
  static final String spanTwo = "/spanOne/spanTwo";

  ///GetX counter cross- page interaction
  static final String counterGet = "/counterGet";
  static final String jumpOne = "/jumpOne";
  static final String jumpTwo = "/jumpOne/jumpTwo";

  ///Alias ​​mapping page
  static final List<GetPage> getPages = [
    GetPage(name: main, page: () => MainPage()),
    GetPage(name: dialog, page: () => Dialog()),
    GetPage(name: counter, page: () => CounterPage()),
    GetPage(name: testLayout, page: () => TestLayoutPage()),
    GetPage(name: smartDialog, page: () => SmartDialogPage()),
    GetPage(name: spanOne, page: () => SpanOnePage()),
    GetPage(name: spanTwo, page: () => SpanTwoPage()),
    GetPage(name: counterGet, page: () => CounterGetPage()),
    GetPage(name: jumpOne, page: () => JumpOnePage()),
    GetPage(name: jumpTwo, page: () => JumpTwoPage()),
  ];
}
```

## Routing API

**Please pay attention to named routes, just add `Named` at the end of the api, for example:**

- Default: Get.to(SomePage());
- Named route: Get.toNamed("/somePage");

**Detailed Api introduction, the following content comes from GetX's README document, and has been sorted out**

- Navigate to a new page

```dart
Get.to(NextScreen());
Get.toNamed("/NextScreen");
```

- Close SnackBars, Dialogs, BottomSheets, or anything you would normally close with Navigator.pop (context)

```dart
Get.back();
```

- Go to the next page, but there is no option to return to the previous page (used for SplashScreens, login page, etc.)

```dart
Get.off(NextScreen());
Get.offNamed("/NextScreen");
```

- Go to the next interface and cancel all previous routes (useful in shopping carts, voting and testing)

```dart
Get.offAll(NextScreen());
Get.offAllNamed("/NextScreen");
```

- Send data to other pages

Just send the parameters you want. Get here accepts anything, whether it is a string, a Map, a List, or even an instance of a class.

```dart
Get.to(NextScreen(), arguments:'Get is the best');
Get.toNamed("/NextScreen", arguments:'Get is the best');
```

On your class or controller:

```dart
print(Get.arguments);
//print out: Get is the best
```

- To navigate to the next route and receive or update data immediately after returning

```dart
var data = await Get.to(Payment());
var data = await Get.toNamed("/payment");
```

- On another page, send the data of the previous route

```dart
Get.back(result:'success');
// and use it, for example:
if(data =='success') madeAnything();
```

- If you don't want to use GetX syntax, just change Navigator (uppercase) to navigator (lowercase), you can have all the functions of standard navigation without using context, for example:

```dart
// Default Flutter navigation
Navigator.of(context).push(
  context,
  MaterialPageRoute(
    builder: (BuildContext context) {
      return HomePage();
    },
  ),
);

// Obtained using Flutter syntax without context.
navigator.push(
  MaterialPageRoute(
    builder: (_) {
      return HomePage();
    },
  ),
);

// get syntax
Get.to(HomePage());
```

**Dynamic web link**

- This is a very important feature. On the web side, you can `guarantee to pass parameters to the page` via url

Get provides advanced dynamic URLs, just like on the Web. Web developers may already want this feature on Flutter, and Get solves this problem.

```dart
Get.offAllNamed("/NextScreen?device=phone&id=354&name=Enzo");
```

On your controller/bloc/stateful/stateless class:

```dart
print(Get.parameters['id']);
// out: 354
print(Get.parameters['name']);
// out: Enzo
```

You can also use Get to easily receive NamedParameters.

```dart
void main() {
  runApp(
    GetMaterialApp(
      initialRoute:'/',
      getPages: [
      GetPage(
        name:'/',
        page: () => MyHomePage(),
      ),
      GetPage(
        name:'/profile/',
        page: () => MyProfile(),
      ),
       //You can define a different page for routes with parameters, or you can define a different page for routes without parameters, but you must use slashes "/" on routes that do not receive parameters, as mentioned above Like that.
       GetPage(
        name:'/profile/:user',
        page: () => UserProfile(),
      ),
      GetPage(
        name:'/third',
        page: () => Third(),
        transition: Transition.cupertino
      ),
     ],
    )
  );
}
```

Send named route data

```dart
Get.toNamed("/profile/34954");
```

On the second page, get data through parameters

```dart
print(Get.parameters['user']);
// out: 34954
```

Now, all you need to do is to use Get.toNamed() to navigate your named route without any context (you can call your route directly from your BLoC or Controller class), when your application is compiled to On the web, your route will appear in the URL.

# At last

## Related address

- DEMO address in the text: [flutter_use](https://github.com/CNAD666/flutter_use)
- GetX plug- in address: [getx_template](https://github.com/CNAD666/getx_template)

## Series of articles

> Drained, manual funny.jpg

-  **solution**
  - Dialog solution, wall crack recommendation: [A more elegant Flutter Dialog solution](https://juejin.cn/post/6902331428072390663)

- **Status Management**
  - Recommended for large projects: [fish_redux use detailed explanation- - - you will use it after reading it! ](https://juejin.cn/post/6860029460524040199)
  - [flutter_bloc usage analysis- - - Sao Nian, are you still building bloc! ](https://juejin.cn/post/6856268776510504968)
