package com.netflix.encodingservice.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.netflix.encodingservice.Event.VideoEncodedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@Slf4j
@RequiredArgsConstructor
public class EncodingService {

    private final S3Client ss3Client;
    private final KafkaTemplate<String, VideoEncodedEvent> kafkaTemplate;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    private static final String VIDEO_ENCODED_TOPIC = "video-encoded";

    // video qualities to encode
    // Format : resolution, bitrate, height

    private final List<int[]> VIDEO_QUALITIES = Arrays.asList(
            new int[] { 1920, 5000, 1080 }, // 1080p - 5000k bitrate
            new int[] { 1280, 2800, 720 }, // 720p - 2800k bitrate
            new int[] { 854, 1200, 480 }, // 480p - 1200k bitrate
            new int[] { 640, 800, 360 } // 360p - 800k bitrate
    );
}
