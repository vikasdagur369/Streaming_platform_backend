package com.netflix.videoservice.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netflix.videoservice.event.VideoUploadedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoService {

    private final S3Client s3Client;
    private final KafkaTemplate<String, VideoUploadedEvent> kafkaTemplate;

    // Reads value from application.properties
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private static final String VIDEO_UPLOADED_TOPIC = "video.uploaded";

    /*
     * upload video to AWS s3 and publish videoUploadedEvent to kafka.
     * FLOW :-
     * 1. Recieve multipart file
     * 2. generate unique s3 key
     * 3. upload to s3
     * 4. publish video uploaded event to kafka
     * 5. encoding service picks up and start processing.
     */

    public String uploadVideo(String movieId, MultipartFile file) throws IOException {
        log.info("Starting video upload for movie: {} file : {}",
                movieId, file.getOriginalFilename());

        // generate unique s3 key for raw video
        // Format :- raw/movieId/uuid_filename
        String videoKey = "raw/" + movieId + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Preparing metadata
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(videoKey)
                .contentType(file.getContentType()).contentLength(file.getSize()).build();

        // File goes from backend server → AWS S3
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        log.info("video is uploaded to s3 successfully!, key : {}", videoKey);

        // publishing event to kafka
        // encoding service will consume this and start FFmpeg processing.

        VideoUploadedEvent event = new VideoUploadedEvent(
                movieId,
                videoKey,
                bucketName,
                file.getOriginalFilename(),
                file.getSize());

        kafkaTemplate.send(VIDEO_UPLOADED_TOPIC, movieId, event);
        log.info("VideoUploadEvent published for movie : {}", movieId);
        return videoKey;
    }

}
