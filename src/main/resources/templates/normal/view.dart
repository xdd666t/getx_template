import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'logic.dart';

class @namePage extends StatelessWidget {
  final logic = Get.put(@nameLogic());
  final state = Get.find<@nameLogic>().state;

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}
