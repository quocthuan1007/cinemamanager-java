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
        String videoID = "HXWRTGbhb4U";  // ID video YouTube
        String embedHTML = """
        <html>
            <body style='margin:0px;padding:0px;'>
                <iframe width='100%%' height='100%%' 
                        src='https://www.youtube.com/embed/%s?autoplay=1'
                        frameborder='0' allow='autoplay; encrypted-media' allowfullscreen>
                </iframe>
            </body>
        </html>
        """.formatted(videoID);

        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(embedHTML);
    }
}
