package kanjava;

import org.bytedeco.javacpp.opencv_core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static org.bytedeco.javacpp.opencv_imgproc.INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

@SpringBootApplication
@RestController
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(App.class);

    // プロパティファイルの読み込み or デフォルト
    @Value("${faceduker.width:200}")
    int resizedWidth;

    // FaceDetectorをインジェクション
    @Autowired
    FaceDetector faceDetector;

    // メッセージ操作用APIのJMSラッパー
    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(value = "/")
    String hello() {
        return "Hello World!";
    }

    // curl -v -F 'file=@hoge.jpg' http://localhost:8080/duker > after.jpg という風に使えるようにする
    // POSTで/dukerへのリクエストに対する処理
    @RequestMapping(value = "/duker", method = RequestMethod.POST)
    BufferedImage duker(
            /* パラメータ名fileのマルチパートリクエストのパラメータを取得 */
            @RequestParam Part file
    ) throws IOException {
        // Part -> BufferedImage -> Matと変換
        opencv_core.Mat source = opencv_core.Mat.createFrom(ImageIO.read(file.getInputStream()));
        // 対象のMatに対して顔認識。認識結果に対してduker関数を適用する。
        faceDetector.detectFaces(source, FaceTranslator::duker);
        // Mat -> BufferedImage
        return source.getBufferedImage();
    }

    @RequestMapping(value = "/send")
    String send(
            @RequestParam String msg
    ) {
        Message message = MessageBuilder
                .withPayload(msg)
                .build();
        jmsMessagingTemplate.send("hello", message);
        return "OK";
    }

    @RequestMapping(value = "/queue")
    String queue(
            @RequestParam Part file
    ) throws IOException {
        byte[] src = StreamUtils.copyToByteArray(file.getInputStream());
        Message<byte[]> message = MessageBuilder.withPayload(src).build();
        jmsMessagingTemplate.send("faceConverter", message);
        return "OK";
    }

    @JmsListener(destination = "faceConverter", concurrency = "1-5")
    void convertFaces(Message<byte[]> message) throws IOException {
        log.info("received {}", message);
        try (InputStream stream = new ByteArrayInputStream(message.getPayload())) {
            opencv_core.Mat source = opencv_core.Mat.createFrom(ImageIO.read(stream));
            faceDetector.detectFaces(source, FaceTranslator::duker);
            // resizing from here
            double ratio = ((double) resizedWidth) / source.cols();
            int resizedHeight = (int) (ratio * source.rows());
            opencv_core.Mat out = new opencv_core.Mat(resizedHeight, resizedWidth, source.type());
            resize(source, out, new opencv_core.Size(), ratio, ratio, INTER_LINEAR);
            // resizing to here
            BufferedImage image = out.getBufferedImage();

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(image, "png", outputStream);
                outputStream.flush();
                simpMessagingTemplate.convertAndSend(
                        "/topic/faces",
                        Base64.getEncoder().encodeToString(outputStream.toByteArray())
                );
            }
        }
    }

    @MessageMapping(value = "/greet")
    @SendTo(value = "/topic/greetings")
    String greet(String name) {
        log.info("received {}", name);
        return "Hello " + name;
    }
}