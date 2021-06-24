# customwebview

- Dùng để hiển thị webview
- có thể nhúng Js vào webview bằng

Or use Gradle:


dependencies {
  implementation 'com.custom.webview:webview:1.0.2'
}

DialogFragment webFragment = WebFragment.newInstance(DashBoardUtils.getHeader(activity), url);
((WebFragment) webFragment).setJsCustom(<class> extend JsDashBoardBase);
