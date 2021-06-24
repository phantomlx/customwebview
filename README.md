# customwebview

- Dùng để hiển thị webview
- có thể nhúng Js vào webview bằng

Or use Gradle:


dependencies {
  implementation 'com.custom.webview:webview:1.0.2'
}

DialogFragment webFragment = WebFragment.newInstance(< Map<String,String> >, url);
((WebFragment) webFragment).setJsCustom(<class> extend JsCustomBase);
