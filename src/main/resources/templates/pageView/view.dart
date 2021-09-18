import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'logic.dart';
import 'state.dart';

class @namePage extends StatelessWidget {
  const @namePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final @nameLogic logic = Get.put(@nameLogic());
    final @nameState state = Get.find<@nameLogic>().state;

    return Container();
  }
}
