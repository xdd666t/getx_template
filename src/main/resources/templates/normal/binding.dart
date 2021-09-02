import 'package:get/get.dart';

import 'logic.dart';

class @nameBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => @nameLogic());
  }
}
