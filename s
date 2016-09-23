[33mcommit 9fc3888afb88f188e942bca51a6e7ee21748a22a[m
Author: 王朋飞 <www.wangpengfei99@gmail.com>
Date:   Wed Sep 21 12:51:23 2016 +0800

    Update ignore file

[1mdiff --git a/.gitignore b/.gitignore[m
[1mindex c6cbe56..3dedb52 100644[m
[1m--- a/.gitignore[m
[1m+++ b/.gitignore[m
[36m@@ -1,8 +1,9 @@[m
[31m-*.iml[m
 .gradle[m
[32m+[m
[32m+[m[32m# local config[m
 /local.properties[m
[31m-/.idea/workspace.xml[m
[31m-/.idea/libraries[m
[31m-.DS_Store[m
 /build[m
[31m-/captures[m
[32m+[m
[32m+[m[32m# Intellij project files[m
[32m+[m[32m*.iml[m
[32m+[m[32m.idea/[m
\ No newline at end of file[m
[1mdiff --git a/.idea/misc.xml b/.idea/misc.xml[m
[1mindex 5d19981..fbb6828 100644[m
[1m--- a/.idea/misc.xml[m
[1m+++ b/.idea/misc.xml[m
[36m@@ -37,7 +37,7 @@[m
     <ConfirmationsSetting value="0" id="Add" />[m
     <ConfirmationsSetting value="0" id="Remove" />[m
   </component>[m
[31m-  <component name="ProjectRootManager" version="2" languageLevel="JDK_1_7" default="true" assert-keyword="true" jdk-15="true" project-jdk-name="1.8" project-jdk-type="JavaSDK">[m
[32m+[m[32m  <component name="ProjectRootManager" version="2" languageLevel="JDK_1_8" default="true" assert-keyword="true" jdk-15="true" project-jdk-name="1.8" project-jdk-type="JavaSDK">[m
     <output url="file://$PROJECT_DIR$/build/classes" />[m
   </component>[m
   <component name="ProjectType">[m
[1mdiff --git a/app/.gitignore b/app/.gitignore[m
[1mindex 796b96d..e738160 100644[m
[1m--- a/app/.gitignore[m
[1m+++ b/app/.gitignore[m
[36m@@ -1 +1,4 @@[m
[31m-/build[m
[32m+[m[32m# 不忽略outputs，方便从github上面下载apk[m
[32m+[m[32m/build/generated[m
[32m+[m[32m/build/intermediates[m
[32m+[m[32m/build/tmp[m
[1mdiff --git a/app/build/outputs/apk/app-debug-unaligned.apk b/app/build/outputs/apk/app-debug-unaligned.apk[m
[1mnew file mode 100644[m
[1mindex 0000000..872c08a[m
Binary files /dev/null and b/app/build/outputs/apk/app-debug-unaligned.apk differ
[1mdiff --git a/app/build/outputs/apk/app-debug.apk b/app/build/outputs/apk/app-debug.apk[m
[1mnew file mode 100644[m
[1mindex 0000000..f28a9c9[m
Binary files /dev/null and b/app/build/outputs/apk/app-debug.apk differ
[1mdiff --git a/app/build/outputs/logs/manifest-merger-debug-report.txt b/app/build/outputs/logs/manifest-merger-debug-report.txt[m
[1mnew file mode 100644[m
[1mindex 0000000..877bb12[m
[1m--- /dev/null[m
[1m+++ b/app/build/outputs/logs/manifest-merger-debug-report.txt[m
[36m@@ -0,0 +1,477 @@[m
[32m+[m[32m-- Merging decision tree log ---[m
[32m+[m[32mmanifest[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:1:1-238:12[m
[32m+[m	[32mpackage[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:2:5-35[m
[32m+[m		[32mINJECTED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml[m
[32m+[m		[32mINJECTED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml[m
[32m+[m	[32mandroid:versionName[m
[32m+[m		[32mINJECTED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml[m
[32m+[m		[32mINJECTED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml[m
[32m+[m	[32mxmlns:android[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:1:11-69[m
[32m+[m	[32mandroid:versionCode[m
[32m+[m		[32mINJECTED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml[m
[32m+[m		[32mINJECTED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml[m
[32m+[m[32muses-permission#android.permission.INTERNET[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:5:5-67[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:5:22-64[m
[32m+[m[32muses-permission#android.permission.ACCESS_NETWORK_STATE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:6:5-79[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:6:22-76[m
[32m+[m[32muses-permission#android.permission.CHANGE_NETWORK_STATE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:7:5-79[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:7:22-76[m
[32m+[m[32muses-permission#android.permission.ACCESS_WIFI_STATE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:9:5-76[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:9:22-73[m
[32m+[m[32muses-permission#android.permission.CHANGE_WIFI_STATE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:10:5-76[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:10:22-73[m
[32m+[m[32muses-permission#android.permission.MOUNT_UNMOUNT_FILESYSTEMS[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:12:5-84[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:12:22-81[m
[32m+[m[32muses-permission#android.permission.WRITE_EXTERNAL_STORAGE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:13:5-81[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:13:22-78[m
[32m+[m[32muses-permission#android.permission.READ_EXTERNAL_STORAGE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:14:5-80[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:14:22-77[m
[32m+[m[32muses-permission#android.permission.VIBRATE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:16:5-66[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:16:22-63[m
[32m+[m[32muses-permission#android.permission.RECORD_AUDIO[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:18:5-71[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:18:22-68[m
[32m+[m[32muses-permission#android.permission.READ_PHONE_STATE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:20:5-75[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:20:22-72[m
[32m+[m[32muses-permission#android.permission.READ_CONTACTS[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:22:5-72[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:22:22-69[m
[32m+[m[32mpermission#com.example.econonew.permission.JPUSH_MESSAGE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:24:5-26:47[m
[32m+[m	[32mandroid:protectionLevel[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:26:9-44[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:25:9-69[m
[32m+[m[32muses-permission#com.example.econonew.permission.JPUSH_MESSAGE[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:28:5-85[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:28:22-82[m
[32m+[m[32muses-permission#android.permission.RECEIVE_USER_PRESENT[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:29:5-79[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:29:22-76[m
[32m+[m[32muses-permission#android.permission.WAKE_LOCK[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:30:5-68[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:30:22-65[m
[32m+[m[32muses-permission#android.permission.WRITE_SETTINGS[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:31:5-73[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:31:22-70[m
[32m+[m[32muses-permission#android.permission.SYSTEM_ALERT_WINDOW[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:32:5-78[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:32:22-75[m
[32m+[m[32muses-permission#android.permission.ACCESS_COARSE_LOCATION[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:33:5-81[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:33:22-78[m
[32m+[m[32muses-permission#android.permission.ACCESS_FINE_LOCATION[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:34:5-79[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:34:22-76[m
[32m+[m[32muses-permission#android.permission.ACCESS_LOCATION_EXTRA_COMMANDS[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:35:5-89[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:35:22-86[m
[32m+[m[32mapplication[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:37:5-237:19[m
[32m+[m[32mMERGED from [com.android.support:appcompat-v7:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\appcompat-v7\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-vector-drawable:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-vector-drawable\24.2.0\AndroidManifest.xml:23:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:animated-vector-drawable:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\animated-vector-drawable\24.2.0\AndroidManifest.xml:22:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-vector-drawable:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-vector-drawable\24.2.0\AndroidManifest.xml:23:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-v4:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-v4\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-core-utils:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-core-utils\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-media-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-media-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-core-ui:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-core-ui\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-fragment:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-fragment\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-core-utils:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-core-utils\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-media-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-media-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-core-ui:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-core-ui\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:support-compat:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\support-compat\24.2.0\AndroidManifest.xml:25:5-20[m
[32m+[m[32mMERGED from [com.android.support:cardview-v7:24.2.0] D:\download\Application\Finance_App\app\build\intermediates\exploded-aar\com.android.support\cardview-v7\24.2.0\AndroidManifest.xml:22:5-20[m
[32m+[m	[32mandroid:label[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:40:9-41[m
[32m+[m	[32mandroid:supportsRtl[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:41:9-35[m
[32m+[m	[32mandroid:allowBackup[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:38:9-35[m
[32m+[m	[32mandroid:icon[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:39:9-40[m
[32m+[m	[32mandroid:theme[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:43:9-51[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:42:9-57[m
[32m+[m[32mactivity#com.example.econonew.view.activity.main.SplashActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:44:9-53:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:47:13-49[m
[32m+[m	[32mandroid:label[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:46:13-45[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:45:13-62[m
[32m+[m[32mintent-filter#android.intent.action.MAIN+android.intent.category.LAUNCHER[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:48:13-52:29[m
[32m+[m[32maction#android.intent.action.MAIN[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:49:17-69[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:49:25-66[m
[32m+[m[32mcategory#android.intent.category.LAUNCHER[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:51:17-77[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:51:27-74[m
[32m+[m[32mactivity#com.example.econonew.view.activity.channel.ChannelAddActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:54:9-57:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:56:13-49[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:55:13-69[m
[32m+[m[32mactivity#com.example.econonew.view.activity.main.MainActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:58:9-61:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:60:13-49[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:59:13-60[m
[32m+[m[32mactivity#com.example.econonew.view.activity.User.UserActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:62:9-66:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:64:13-49[m
[32m+[m	[32mandroid:theme[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:65:13-44[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:63:13-60[m
[32m+[m[32mactivity#com.example.econonew.view.activity.User.UserSetPwdActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:67:9-71:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:70:13-49[m
[32m+[m	[32mandroid:theme[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:69:13-44[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:68:13-66[m
[32m+[m[32mactivity#com.example.econonew.view.activity.User.UserLoginActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:72:9-76:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:74:13-49[m
[32m+[m	[32mandroid:theme[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:75:13-44[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:73:13-65[m
[32m+[m[32mactivity#com.example.econonew.view.activity.User.UserRegistActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:77:9-81:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:80:13-49[m
[32m+[m	[32mandroid:theme[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:79:13-44[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:78:13-66[m
[32m+[m[32mactivity#com.example.econonew.view.activity.channel.ExchangeActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:82:9-85:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:84:13-49[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:83:13-67[m
[32m+[m[32mactivity#com.example.econonew.view.activity.channel.StockActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:86:9-89:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:88:13-49[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:87:13-64[m
[32m+[m[32mactivity#com.example.econonew.view.activity.channel.FundsActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:90:9-93:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:92:13-49[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:91:13-64[m
[32m+[m[32mactivity#com.example.econonew.view.activity.channel.MoneyActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:94:9-97:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:96:13-49[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:95:13-64[m
[32m+[m[32mactivity#com.example.econonew.view.activity.channel.FuturesActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:98:9-101:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:100:13-49[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:99:13-66[m
[32m+[m[32mactivity#com.example.econonew.view.activity.channel.SpecialActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:102:9-105:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:104:13-49[m
[32m+[m	[32mandroid:name[m
[32m+[m		[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:103:13-66[m
[32m+[m[32mactivity#com.example.econonew.server.jpush.SettingActivity[m
[32m+[m[32mADDED from D:\download\Application\Finance_App\app\src\main\AndroidManifest.xml:106:9-109:20[m
[32m+[m	[32mandroid:screenOrientation[m
[32m+[m		[32mADDED from D:\download\Application\Finance