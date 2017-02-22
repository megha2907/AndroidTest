# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/Jeeva/android/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#Start Dto#
-keep public class in.sportscafe.nostragamus.module.** { *; }
#End Dto#

#Start Jackson#
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keepnames class com.fasterxml.jackson.** { *; }
-keepattributes *Annotation*,EnclosingMethod,Signature
-dontwarn java.beans.Transient
-dontwarn java.beans.ConstructorProperties
#End Jackson#

#Start Okhttp#
-dontwarn java.nio.file.**
-dontwarn java.lang.invoke.**
-dontwarn java.lang.reflect.Method
#End Okhttp#

#Start Retrofit#
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
#End Retrofit#

#Start Crashlytics#
-keepattributes SourceFile,LineNumberTable
#End Crashlytics#

#Start MoEngage#
-dontwarn com.google.android.gms.location.**
-dontwarn com.google.android.gms.gcm.**
-dontwarn com.google.android.gms.iid.**

-keep class com.google.android.gms.gcm.** { *; }
-keep class com.google.android.gms.iid.** { *; }
-keep class com.google.android.gms.location.** { *; }

-keep class com.moe.pushlibrary.activities.** { *; }
-keep class com.moengage.locationlibrary.GeofenceIntentService
-keep class com.moe.pushlibrary.InstallReceiver
-keep class com.moengage.push.MoEPushWorker
-keep class com.moe.pushlibrary.providers.MoEProvider
-keep class com.moengage.receiver.MoEInstanceIDListener
-keep class com.moengage.worker.MoEGCMListenerService
-keep class com.moe.pushlibrary.models.** { *;}
-keep class com.moengage.core.GeoTask
-keep class com.moengage.location.GeoManager
-keep class com.moengage.inapp.InAppManager
-keep class com.moengage.push.PushManager
-keep class com.moengage.inapp.InAppController


-dontwarn com.moengage.location.GeoManager
-dontwarn com.moengage.core.GeoTask
-dontwarn com.moengage.receiver.*
-dontwarn com.moengage.worker.*
-dontwarn com.moengage.ViewEngine

-keep class com.delight.**  { *; }
#End MoEngage#

#Start Facebook#
-keep class com.facebook.drawee.view.SimpleDraweeView
-keep class com.facebook.drawee.backends.pipeline.Fresco
-keep class com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder
-keep class com.facebook.drawee.interfaces.DraweeController

-dontwarn com.facebook.drawee.view.SimpleDraweeView
-dontwarn com.facebook.drawee.backends.pipeline.Fresco
-dontwarn com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder
-dontwarn com.facebook.drawee.interfaces.DraweeController

-keep class com.facebook.applinks.** { *; }
-keepclassmembers class com.facebook.applinks.** { *; }
-keep class com.facebook.FacebookSdk { *; }
#End Facebook#

#Start Jsoup#
-keeppackagenames org.jsoup.nodes
#End Jsoup#

#Start Branch.io
-keep class com.google.android.gms.ads.identifier.** { *; }
#End Branch.io

-keep class com.lsjwzh.widget.recyclerviewpager.**
-dontwarn com.lsjwzh.widget.recyclerviewpager.**