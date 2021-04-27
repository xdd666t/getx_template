import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'logic.dart';

class $namePage extends StatefulWidget {
  @override
  _$namePageState createState() => _$namePageState();
}

class _$namePageState extends State<$namePage> {
  final $nameLogic logic = Get.put($nameLogic());

  @override
    Widget build(BuildContext context) {
      return Container();
    }

  @override
  void dispose() {
    super.dispose();
    logic.dispose();
  }
}