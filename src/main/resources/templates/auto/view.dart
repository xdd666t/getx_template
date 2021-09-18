import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'logic.dart';
import 'state.dart';

class @namePage extends StatefulWidget {
  const @namePage({Key? key}) : super(key: key);

  @override
  _@namePageState createState() => _@namePageState();
}

class _@namePageState extends State<@namePage> {
  final @nameLogic logic = Get.put(@nameLogic());
  final @nameState state = Get.find<@nameLogic>().state;

  @override
  Widget build(BuildContext context) {
    return Container();
  }

  @override
  void dispose() {
    Get.delete<@nameLogic>();
    super.dispose();
  }
}