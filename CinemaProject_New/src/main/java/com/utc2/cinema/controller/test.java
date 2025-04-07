package com.utc2.cinema.controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class test {

    @FXML
    private WebView webView;  // WebView để hiển thị video

    // Phương thức xử lý khi nút được nhấn
    @FXML
    private void handleVideoButtonClick() {
        // URL nhúng của video YouTube
        String videoURL = "https://www.youtube.com/embed/abc123";  // Thay "abc123" bằng ID video thực tế

        // Lấy WebEngine từ WebView
        WebEngine webEngine = webView.getEngine();

        // Tải URL video vào WebView
        webEngine.load(videoURL);
    }
}
