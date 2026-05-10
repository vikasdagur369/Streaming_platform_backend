package com.netflix.videoservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadedEvent {
    /*
     * Event published to kafka when a video is uploaded to S3.
     * Encoding Service consume this to start FFmpeg processing.
     * TOPIC :- Video uploaded
     */

    private String movieId;
    private String videokey;
    private String bucketName;
    private String originalFileName;
    private Long fileSizeBytes;

}
