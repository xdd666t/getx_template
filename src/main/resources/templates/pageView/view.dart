import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'logic.dart';
import 'state.dart';

class @namePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final @nameLogic logic = Get.put(@nameLogic());
    final @nameState state = Get.find<@nameLogic>().state;

    return Container();
  }
}
