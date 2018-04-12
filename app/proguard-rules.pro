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

-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }

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

-keep class com.moengage.pushbase.activities.PushTracker
-keep class com.moengage.pushbase.activities.SnoozeTracker
-keep class com.moengage.pushbase.push.MoEPushWorker
-keep class com.moe.pushlibrary.MoEWorker
-keep class com.moe.pushlibrary.AppUpdateReceiver
-keep class com.moengage.core.MoEAlarmReceiver

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

#Start paytm
-keepclassmembers class com.paytm.pgsdk.PaytmWebView$PaytmJavaScriptInterface {
   public *;
}
#end paytm

-keep class com.lsjwzh.widget.recyclerviewpager.**
-dontwarn com.lsjwzh.widget.recyclerviewpager.**

#Loading Animation
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

#Start Aws
-keep class org.apache.commons.logging.**               { *; }
-keep class com.amazonaws.services.sqs.QueueUrlHandler  { *; }
-keep class com.amazonaws.javax.xml.transform.sax.*     { public *; }
-keep class com.amazonaws.javax.xml.stream.**           { *; }
-keep class com.amazonaws.services.**.model.*Exception* { *; }
-keep class org.codehaus.**                             { *; }
-keepattributes Signature,*Annotation*

-dontwarn javax.xml.stream.events.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.apache.commons.logging.impl.**
-dontwarn org.apache.http.conn.scheme.**
-dontwarn org.apache.http.annotation.**
-dontwarn com.amazonaws.**

# Class names are needed in reflection
-keepnames class com.amazonaws.**
-keepnames class com.amazon.**
# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
# The following are referenced but aren't required to run
-dontwarn com.fasterxml.jackson.**
-dontwarn org.apache.commons.logging.**
# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**
# The SDK has several references of Apache HTTP client
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**
#End Aws